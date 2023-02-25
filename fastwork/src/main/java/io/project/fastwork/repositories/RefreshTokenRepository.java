package io.project.fastwork.repositories;

import io.project.fastwork.domains.RefreshToken;
import io.project.fastwork.domains.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken,Long> {
    Optional<RefreshToken>findByToken(String token);
    @Modifying
    int deletebyUser(Users user);
}
