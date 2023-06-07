package io.project.fastwork.repositories;

import io.project.fastwork.domains.TypeWork;
import io.project.fastwork.domains.Users;
import io.project.fastwork.domains.Work;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
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

import java.util.List;
import java.util.Optional;

@SpringBootTest
@Testcontainers
@Sql(value = "classpath:/sql/initDataBefore.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = "classpath:/sql/clearDataAfter.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class WorkRepositoryTest {
    @Autowired
    private WorkRepository workRepository;

    @Autowired
    private UserRepository userRepository;

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
    void FindWorkByNameTest_ReturnTrue() {
        List<Work> workByName = workRepository.findWorkByName("test");
        Assertions.assertEquals("test2", workByName.get(0).getWorkName());
    }

    @Test
    @Transactional
    void FindByTypeWorkTest_ReturnTrue() {
        List<Work> workByType = workRepository.findWorkByType(777L);
        Assertions.assertEquals("Type", workByType.get(0).getWorkType().getTypeWorkName());
    }

    @Test
    void GetWorkByIdTest_ReturnTrue() {
        Work getById = workRepository.getWorkById(778L);
        Assertions.assertEquals(Long.valueOf(778), getById.getId());
    }

    @Test
    void GetAllWorkTest_ReturnTrue(){
        List<Work> workList = workRepository.findAll();
        Assertions.assertTrue(workList.size()>0);
    }

    @Test
    @Transactional
    void SaveWorkTest_ReturnTrue(){
        Work work = Work.builder()
                .workName("Test")
                .workDescribe("Test")
                .workPrice(12.0F)
                .workCountPerson(5)
                .workType(typeWorkRepository.getTypeWorkById(777L))
                .workHirer(userRepository.getUserById(778L))
                .build();
        Work savedWork = workRepository.save(work);
        Assertions.assertEquals("Test", savedWork.getWorkName());
    }

    @Test
    void UpdateWorkTest_ReturnTrue(){
        Work work = workRepository.getWorkById(778L);
        work.setWorkName("UpdatedWork");
        Work updatedWork = workRepository.save(work);
        Assertions.assertEquals(updatedWork.getWorkName(), work.getWorkName());
    }
}