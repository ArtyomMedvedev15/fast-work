package io.project.fastwork.services.jwt;

import io.project.fastwork.controller.advice.exception.TokenRefreshException;
import io.project.fastwork.domains.RefreshToken;
import io.project.fastwork.domains.Users;
import io.project.fastwork.repositories.RefreshTokenRepository;
import io.project.fastwork.repositories.UserRepository;
import io.project.fastwork.services.api.UserServiceApi;
import io.project.fastwork.services.exception.UserAlreadyExisted;
import io.project.fastwork.services.exception.UserInvalidDataParemeter;
import io.project.fastwork.services.exception.UserNotFound;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class RefreshTokenServiceTest {

    @MockBean
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @MockBean
    private UserServiceApi userServiceApi;

    @Test
    void FindByTokenTest_ReturnTrue() {
        RefreshToken refreshTokenByToken = RefreshToken.builder()
                .token("token")
                .build();

        Mockito.when(refreshTokenRepository.findByToken("token")).thenReturn(Optional.of(refreshTokenByToken));

        Optional<RefreshToken> findbytoken = refreshTokenService.findByToken("token");
        assertEquals("token", findbytoken.get().getToken());

        Mockito.verify(refreshTokenRepository, Mockito.times(1)).findByToken("token");

    }

    @Test
    void VerifyExpirationTokenTest_ReturnTrue() {
        RefreshToken refreshTokenByToken = RefreshToken.builder()
                .token("token")
                .expiryDate(Instant.now().plusSeconds(1111111))
                .build();

        RefreshToken verifyExpiration = refreshTokenService.verifyExpiration(refreshTokenByToken);

        assertEquals("token", verifyExpiration.getToken());
    }

    @Test
    void VerifyExpirationTokenTest_ThrowException() {
        RefreshToken refreshTokenByToken = RefreshToken.builder()
                .token("token")
                .user(Users.builder().build())
                .expiryDate(Instant.now().minusMillis(1111111))
                .build();

        TokenRefreshException tokenRefreshException = assertThrows(
                TokenRefreshException.class,
                () -> refreshTokenService.verifyExpiration(refreshTokenByToken)
        );
        Mockito.verify(refreshTokenRepository, Mockito.times(1)).delete(refreshTokenByToken);
    }


    @Test
    void DeleteTokenByUserTest_ReturnTrue() throws UserNotFound, UserInvalidDataParemeter {
        Users userByLogin = Users.builder()
                .id(1L).username("test")
                .build();
        RefreshToken refreshTokenByToken = RefreshToken.builder()
                .token("token")
                .user(userByLogin)
                .expiryDate(Instant.now().minusMillis(1111111))
                .build();

        Mockito.when(userServiceApi.findByLogin("test")).thenReturn(userByLogin);

        refreshTokenService.deleteTokenByUserId("test");

        Mockito.verify(userServiceApi,Mockito.times(1)).findByLogin("test");
        Mockito.verify(refreshTokenRepository,Mockito.times(1)).deleteByUser(userByLogin);

    }
}