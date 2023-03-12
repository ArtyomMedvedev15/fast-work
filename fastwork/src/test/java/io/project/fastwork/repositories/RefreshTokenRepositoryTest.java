package io.project.fastwork.repositories;

import io.project.fastwork.domains.RefreshToken;
import io.project.fastwork.domains.Users;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.Instant;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@Testcontainers
@Sql(value = "classpath:/sql/initDataBefore.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = "classpath:/sql/clearDataAfter.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class RefreshTokenRepositoryTest {

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private UserRepository userRepository;
    @Container
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:9.6.18-alpine")
            .withDatabaseName("prop")
            .withUsername("postgres")
            .withPassword("postgres")
            .withExposedPorts(5432)
            .withInitScript("sql/initDB.sql");


    @DynamicPropertySource
    static void registerPgProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url",
                () -> String.format("jdbc:postgresql://localhost:%d/prop", postgreSQLContainer.getFirstMappedPort()));
        registry.add("spring.datasource.username", () -> "postgres");
        registry.add("spring.datasource.password", () -> "postgres");
    }

    @Test
    void FindByTokenTest_ReturnTrue() {
        Optional<RefreshToken> refreshToken = refreshTokenRepository.findByToken("token");
        assertEquals("token", refreshToken.get().getToken());
    }

    @Test
    @Transactional
    void DeleteRefreshTokenTest_ReturnTrue() {
        refreshTokenRepository.deleteByUser(userRepository.getUserById(777L));
        Optional<RefreshToken> refreshTokendeleted = refreshTokenRepository.findByToken("token");
        assertTrue(refreshTokendeleted.isEmpty());
    }

    @Test
    void SaveRefreshTokenTest_ReturnTrue(){
        Users userById = userRepository.getUserById(778L);
        RefreshToken refreshTokenSave = RefreshToken.builder()
                .token("token3")
                .user(userById)
                .expiryDate(Instant.now().plusMillis(10000000))
                .build();

        RefreshToken refreshTokenSaved = refreshTokenRepository.save(refreshTokenSave);
        assertEquals("token3", refreshTokenSaved.getToken());
    }
}