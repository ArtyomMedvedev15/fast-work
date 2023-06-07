package io.project.fastwork.repositories;

import io.project.fastwork.domains.StatusWorkApplication;
import io.project.fastwork.domains.Users;
import io.project.fastwork.domains.Work;
import io.project.fastwork.domains.WorkApplication;
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
class WorkAppllicationRepositoryTest {

    @Autowired
    private WorkAppllicationRepository workAppllicationRepository;
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
    void FindWorkApplicationByWorkIdTest_ReturnTrue(){
        List<WorkApplication> workAppllicationRepositoryByWorkId = workAppllicationRepository.findByWorkId(779L);
        assertEquals(779L,workAppllicationRepositoryByWorkId.get(0).getWork().getId());
    }

    @Test
    void FindWorkApplicationByWorkeIdTest_ReturnTrue(){
        List<WorkApplication>workApplicationList=workAppllicationRepository.findByWorkerId(881L);
        assertEquals(881L,workApplicationList.get(0).getWorker().getId());
    }

    @Test
    void GetWorkApplicationByIdTest_ReturnTrue() {
        WorkApplication workApplicationById = workAppllicationRepository.getWorkApplicationById(777L);
        assertEquals(StatusWorkApplication.EXPECTATION,workApplicationById.getStatusWorkApplication());
    }

    @Test
    void SaveWorkApplicationTest_ReturnTrue(){
        WorkApplication workApplication = WorkApplication.builder()
                .worker(Users.builder().id(881L).build())
                .work(Work.builder().id(778L).build())
                .build();
        WorkApplication savedWorkApplication = workAppllicationRepository.save(workApplication);
        assertSame(savedWorkApplication.getWorker().getId(), workApplication.getWorker().getId());
    }

    @Test
    void UpdateWorkApplicationTest_ReturnTrue(){
        WorkApplication workApplicationGetById = workAppllicationRepository.getWorkApplicationById(777L);
        workApplicationGetById.setStatusWorkApplication(StatusWorkApplication.REJECT);
        WorkApplication updatedWorkApplication = workAppllicationRepository.save(workApplicationGetById);
        assertEquals(updatedWorkApplication.getStatusWorkApplication(), StatusWorkApplication.REJECT);
    }
}