package io.project.fastwork.controller;

import io.project.fastwork.controller.exception.TokenRefreshException;
import io.project.fastwork.domains.RefreshToken;
import io.project.fastwork.domains.Role;
import io.project.fastwork.domains.Users;
import io.project.fastwork.dto.request.AuthenticationRequest;
import io.project.fastwork.dto.request.RegistrationRequest;
import io.project.fastwork.dto.request.TokenRefreshRequest;
import io.project.fastwork.dto.response.AuthenticationResponse;
import io.project.fastwork.dto.response.MessageResponse;
import io.project.fastwork.services.api.RefreshTokenServiceApi;
import io.project.fastwork.services.api.UserServiceApi;
import io.project.fastwork.services.exception.UserAlreadyExisted;
import io.project.fastwork.services.exception.UserInvalidDataParemeter;
import io.project.fastwork.services.exception.UserNotFound;
import io.project.fastwork.services.jwt.JwtService;
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
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final RefreshTokenServiceApi refreshTokenService;
    private final UserServiceApi userService;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@RequestBody AuthenticationRequest authenticationRequest) {
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

    @PostMapping("/signup")
    public ResponseEntity<?> registrationUser(@RequestBody RegistrationRequest registrationRequest) {
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
        } catch (UserAlreadyExisted | UserInvalidDataParemeter e) {
            return ResponseEntity.badRequest().body(MessageResponse.builder().message(e.getMessage()).build());
        }
        return ResponseEntity.ok(MessageResponse.builder().message("User registration successfully!"));
    }

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
