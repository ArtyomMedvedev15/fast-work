package io.project.fastwork.controller;

import io.project.fastwork.controller.advice.exception.RestUserAlreadyExistedException;
import io.project.fastwork.controller.advice.exception.RestUserInvalidDataParemeterException;
import io.project.fastwork.controller.advice.exception.TokenRefreshException;
import io.project.fastwork.domains.RefreshToken;
import io.project.fastwork.domains.Role;
import io.project.fastwork.domains.Users;
import io.project.fastwork.dto.request.AuthenticationRequest;
import io.project.fastwork.dto.request.RegistrationRequest;
import io.project.fastwork.dto.request.TokenRefreshRequest;
import io.project.fastwork.dto.response.AuthenticationResponse;
import io.project.fastwork.dto.response.MessageResponse;
import io.project.fastwork.dto.response.TypeWorkResponse;
import io.project.fastwork.services.api.RefreshTokenServiceApi;
import io.project.fastwork.services.api.UserServiceApi;
import io.project.fastwork.services.exception.UserAlreadyExisted;
import io.project.fastwork.services.exception.UserInvalidDataParemeter;
import io.project.fastwork.services.exception.UserNotFound;
import io.project.fastwork.services.jwt.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/auth/")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Authentication", description = "Authentication request to API")
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final RefreshTokenServiceApi refreshTokenService;
    private final UserServiceApi userService;

    @Operation(
            summary = "Authentication user",
            description = "Allows you to authenticate.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully authenticate.", content = {@Content(schema = @Schema(implementation = AuthenticationResponse.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "400", description = "Bad credentials", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "500", description = "Error on server side.", content = {@Content(schema = @Schema())})})
    @PostMapping(value = "/signin", consumes = "application/json")
    public ResponseEntity<?> authenticateUser(@Parameter(name = "Credentials user", required = true,
            description = "Credentials user for authenticate")
                                              @RequestBody AuthenticationRequest authenticationRequest) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getUserlogin(), authenticationRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String jwt = jwtService.generateToken(userDetails);

        RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getUsername());
        List<String> roles = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();

        return ResponseEntity.ok(AuthenticationResponse.builder()
                .token(jwt)
                .refreshToken(refreshToken.getToken())
                .username(userDetails.getUsername())
                .type("Bearer ")
                .email(refreshToken.getUser().getUserEmail())
                .roles(Role.valueOf(roles.get(0))).build());
    }

    @Operation(
            summary = "Registration user",
            description = "Allows you to registration.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully registration.", content = {@Content(schema = @Schema(), mediaType = "application/json")}),
            @ApiResponse(responseCode = "400", description = "Invalid parameter for registration or user with username already created", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "500", description = "Error on server side.", content = {@Content(schema = @Schema())})})
    @PostMapping("/signup")
    public ResponseEntity<?> registrationUser(@Parameter(name = "Request for registration", required = true,
            description = "User data for registration")
                                              @RequestBody RegistrationRequest registrationRequest) {
        try {
            Users user_registraton = Users.builder()
                    .userOriginalName(registrationRequest.getUsername())
                    .userSoname(registrationRequest.getUsersoname())
                    .username(registrationRequest.getUserlogin())
                    .userEmail(registrationRequest.getUseremail())
                    .userRole(Role.valueOf(registrationRequest.getUserrole()))
                    .userPassword(registrationRequest.getUserpassword())
                    .build();
            userService.saveUser(user_registraton);
        } catch (UserAlreadyExisted e) {
            throw new RestUserAlreadyExistedException(String.format("User with username %s already exists!", registrationRequest.getUsername()));
        } catch (UserInvalidDataParemeter e) {
            throw new RestUserInvalidDataParemeterException(e.getMessage());
        }
        return ResponseEntity.ok(MessageResponse.builder().message("User registration successfully!"));
    }

    @Operation(
            summary = "Refresh jwt token",
            description = "Allows you to update your token.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully refreshed token.", content = {@Content(schema = @Schema(implementation = TokenRefreshRequest.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "400", description = "Missing refresh token. Please login again.", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "500", description = "Error on server side.", content = {@Content(schema = @Schema())})})
    @PostMapping("/refreshtoken")
    public ResponseEntity<?> refreshToken(@RequestBody TokenRefreshRequest tokenRefreshRequest) {
        String refreshTokenRequest = tokenRefreshRequest.getRefreshToken();
        Optional<RefreshToken> refreshTokenfind = refreshTokenService.findByToken(refreshTokenRequest);
        Optional<String> token = Optional.of(refreshTokenService.findByToken(refreshTokenRequest)
                .map(refreshToken -> {
                    refreshTokenService.verifyExpiration(refreshToken);
                    return refreshToken;
                })
                .map(RefreshToken::getUser)
                .map(u -> jwtService.generateTokenFromUsername(u.getUsername()))
                .orElseThrow(() -> new TokenRefreshException(refreshTokenRequest, "Missing refresh token in database. Please login again")));

        return ResponseEntity.ok(AuthenticationResponse.builder()
                .token(token.get())
                .type("Bearer ")
                .refreshToken(refreshTokenRequest)
                .username(refreshTokenfind.get().getUser().getUsername())
                .email(refreshTokenfind.get().getUser().getUserEmail())
                .roles(refreshTokenfind.get().getUser().getUserRole()).build());

    }

    @Operation(
            summary = "Logout",
            description = "Allows you to logout.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully logout from service.", content = {@Content(schema = @Schema(implementation = TokenRefreshRequest.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", description = "User with username not found", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "500", description = "Error on server side.", content = {@Content(schema = @Schema())})})
    @PostMapping("/signout")
    public ResponseEntity<?> logoutUser() {
        Authentication loggeg_user = SecurityContextHolder.getContext().getAuthentication();
        String username = loggeg_user.getName();
        try {
            refreshTokenService.deleteTokenByUserId(username);
        } catch (UserNotFound e) {
            return ResponseEntity.badRequest().body(MessageResponse.builder().message(e.getMessage()).build());
        }
        return ResponseEntity.ok(new MessageResponse("Log out successful!"));
    }


}
