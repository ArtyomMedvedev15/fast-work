package io.project.fastwork.services.implementation;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Testcontainers
@Sql(value = "classpath:/sql/initDataBefore.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = "classpath:/sql/clearDataAfter.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class TypeWorkServiceImplTest {

    @Test
    void saveTypeWork() {
    }

    @Test
    void updateTypeWork() {
    }

    @Test
    void deleteTypeWork() {
    }

    @Test
    void findAll() {
    }
}