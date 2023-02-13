package io.project.fastwork.services.implementation;

import io.project.fastwork.domains.StatusWorkApplication;
import io.project.fastwork.domains.Users;
import io.project.fastwork.domains.Work;
import io.project.fastwork.domains.WorkApplication;
import io.project.fastwork.services.api.WorkApplicationServiceApi;
import io.project.fastwork.services.exception.*;
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

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Testcontainers
@Sql(value = "classpath:/sql/initDataBefore.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = "classpath:/sql/clearDataAfter.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class WorkApplicationServiceImplTest {

    @Autowired
    private WorkApplicationServiceApi workApplicationService;

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
    void SaveWorkApplication_WithValidWorkApp_ReturnTrue() throws WorkApplicationAlreadySend {
        WorkApplication valid_work_application = WorkApplication.builder()
                .work(Work.builder().id(777L).build())
                .worker(Users.builder().id(777L).build())
                .build();

        WorkApplication workApplication_saved = workApplicationService.saveWorkApplication(valid_work_application);
        assertNotNull(workApplication_saved);
    }

    @Test
    @Transactional
    void SaveWorkApplication_WithWorkAppExisted_ThrowException()throws WorkNotFound {
        WorkApplication invalid_work_application = WorkApplication.builder()
                .work(Work.builder().id(778L).build())
                .worker(Users.builder().id(778L).build())
                .build();
        WorkApplicationAlreadySend workApplicationAlreadySend = assertThrows(
                WorkApplicationAlreadySend.class,
                () -> workApplicationService.saveWorkApplication(invalid_work_application)
        );
        assertTrue(workApplicationAlreadySend.getMessage().contentEquals("Work application already send!"));
    }

    @Test
    void RejectedWorkApplication_WithValidWorkApp_ReturnTrue() throws WorkApplicationNotFound {
        WorkApplication work_application_rejected = workApplicationService.rejectedWorkApplication(777L);
        assertEquals(work_application_rejected.getStatusWorkApplication(), StatusWorkApplication.REJECT);
    }

    @Test
    void RejectedWorkApplication_WithInValidWorkApp_ThrowException() throws WorkApplicationNotFound {
        WorkApplicationNotFound workApplicationNotFound = assertThrows(
                WorkApplicationNotFound.class,
                () -> workApplicationService.rejectedWorkApplication(9123L)
        );
        assertTrue(workApplicationNotFound.getMessage().contentEquals("Work application with id 9123 not found!"));
     }

    @Test
    void ApprovedWorkApplication_WithValidWorkApp_ReturnTrue() throws WorkApplicationNotFound {
        WorkApplication work_application_rejected = workApplicationService.approvedWorkApplication(777L);
        assertEquals(work_application_rejected.getStatusWorkApplication(), StatusWorkApplication.APPROVE);
    }

    @Test
    void ApproveWorkApplication_WithInValidWorkApp_ThrowException() throws WorkApplicationNotFound {
        WorkApplicationNotFound workApplicationNotFound = assertThrows(
                WorkApplicationNotFound.class,
                () -> workApplicationService.approvedWorkApplication(9123L)
        );
        assertTrue(workApplicationNotFound.getMessage().contentEquals("Work application with id 9123 not found!"));
    }

    @Test
    void FindWorkApplicationByWorkId_WithValidWorkId_ReturnTrue() throws WorkNotFound {
        List<WorkApplication>workApplicationList = workApplicationService.findByWorkId(778L);
        assertFalse(workApplicationList.isEmpty());
    }

    @Test
    void FindWorkApplicationByWorkId_WithNonExistedWork_ThrowException(){
        WorkNotFound workNotFound = assertThrows(
                WorkNotFound.class,
                () -> workApplicationService.findByWorkId(9123L)
        );
        assertTrue(workNotFound.getMessage().contentEquals("Work with id 9123 not found!"));
    }


    @Test
    void FindWorkApplicationByWorkerid_WithValidWorkerId_ReturnTrue() throws WorkerNotFound {
        List<WorkApplication>workApplicationList = workApplicationService.findByWorkerid(778L);
        assertFalse(workApplicationList.isEmpty());
    }

    @Test
    void FindWorkApplicationByWorkId_WithNonExistedWorker_ThrowException(){
        WorkerNotFound workerkNotFound = assertThrows(
                WorkerNotFound.class,
                () -> workApplicationService.findByWorkerid(91223L)
        );
        assertTrue(workerkNotFound.getMessage().contentEquals("Worker with id 91223 not found!"));
    }
}