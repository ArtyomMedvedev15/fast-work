package io.project.fastwork.services.api;

import io.project.fastwork.controller.exception.TokenRefreshException;
import io.project.fastwork.domains.RefreshToken;
import io.project.fastwork.services.exception.UserNotFound;

import java.util.Optional;

public interface RefreshTokenServiceApi {
    Optional<RefreshToken>findByToken(String token);
    RefreshToken createRefreshToken(String username);
    RefreshToken verifyExpiration(RefreshToken refreshTokenExpiry) throws TokenRefreshException;
    void deleteTokenByUserId(String username) throws UserNotFound;
}
