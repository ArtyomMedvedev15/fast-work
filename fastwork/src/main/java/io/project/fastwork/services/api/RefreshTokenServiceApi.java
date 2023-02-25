package io.project.fastwork.services.api;

import io.project.fastwork.domains.RefreshToken;
import io.project.fastwork.services.exception.TokenRefreshException;
import io.project.fastwork.services.exception.UserNotFound;

import java.util.Optional;

public interface RefreshTokenServiceApi {
    Optional<RefreshToken>findByToken(String token);
    RefreshToken createRefreshToken(Long userId) throws UserNotFound;
    RefreshToken verifyExpiration(RefreshToken refreshTokenExpiry) throws TokenRefreshException;
    int deleteTokenByUserId(Long userId) throws UserNotFound;
}
