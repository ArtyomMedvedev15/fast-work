package io.project.fastwork.repositories;

import io.project.fastwork.domains.Role;
import io.project.fastwork.domains.Users;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Testcontainers
@Sql(value = "classpath:/sql/initDataBefore.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = "classpath:/sql/clearDataAfter.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class UserRepositoryTest {

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
    void SaveUserTest_ThenReturnTrue() {
        Users user_valid = Users.builder()
                .username("JHaris12")
                .userOriginalName("JHarissd")
                .userSoname("Bronson")
                .userPassword("QwErTy132!z")
                .userEmail("monim@gmail.com")
                .userRole(Role.WORKER)
                .build();
        Users savedUser = userRepository.save(user_valid);
        assertEquals(savedUser.getUsername(), user_valid.getUsername());
    }

    @Test
    void FindUserByLoginTest_ThenReturnTrue() {
        Users findByLogin = userRepository.findByLogin("Logi124s");
        assertEquals("Logi124s",findByLogin.getUsername());
    }

    @Test
    void GetUserByIdTest_WithId781_ReturnTrue() {
        Users getById = userRepository.getUserById(781L);
        assertEquals(781L,getById.getId());
    }

    @Test
    void FindByEmailTest_ReturnTrue() {
     Users findByEmail = userRepository.findbyEmail("user4@mail.tex");
     assertEquals("user4@mail.tex",findByEmail.getUserEmail());
    }

    @Test
    void UpdateUserTest_ReturnTrue(){
        Users userById = userRepository.getUserById(781L);
        userById.setUserSoname("UpdatedSoname");
        Users updatedUser = userRepository.save(userById);
        assertEquals(userById.getUserSoname(), updatedUser.getUserSoname());
    }

}