package io.project.fastwork.repositories;

import io.project.fastwork.domains.TypeWork;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@Testcontainers
@Sql(value = "classpath:/sql/initDataBefore.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = "classpath:/sql/clearDataAfter.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class TypeWorkRepositoryTest {

    @Autowired
    private TypeWorkRepository typeWorkRepository;
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
    void FindByTypeWorkNameTest_ReturnTrue() {
        TypeWork findByName = typeWorkRepository.findByTypeWorkName("test");
        Assertions.assertEquals("test",findByName.getTypeWorkName());
    }

    @Test
    void GetTypeWorkTest_ReturnTrue() {
        TypeWork typeWorkById = typeWorkRepository.getTypeWorkById(777L);
        assertEquals(777L,typeWorkById.getId());
    }

    @Test
    void SaveTypeWorkTest_ReturnTrue(){
        TypeWork type_work_valid = TypeWork.builder()
                .id(7777L)
                .typeWorkName("Type")
                .typeWorkDescribe("TypeWorkTypeWorkType")
                .build();

        TypeWork typeWorkSaved = typeWorkRepository.save(type_work_valid);
        assertEquals("Type",typeWorkSaved.getTypeWorkName());
    }

    @Test
    void UpdateTypeWorkTest_ReturnTrue(){
        TypeWork typeWorkById = typeWorkRepository.getTypeWorkById(777L);
        typeWorkById.setTypeWorkName("Updated");
        TypeWork updatedTypeWork = typeWorkRepository.save(typeWorkById);
        assertEquals("Updated",updatedTypeWork.getTypeWorkName());
    }

    @Test
    void FindAllTypeWorkTest_ReturnTrue(){
        List<TypeWork> typeWorkList = typeWorkRepository.findAll();
        assertTrue(typeWorkList.size()>0);
    }

    @Test
    void DeleteTypeWork_ReturnTrue(){
        TypeWork deletedTypeWork = typeWorkRepository.getTypeWorkById(777L);
        typeWorkRepository.delete(deletedTypeWork);
        assertEquals(0, typeWorkRepository.findAll().size());
    }
}