package io.project.fastwork.services.implementation;

import io.project.fastwork.domains.StatusWorkApplication;
import io.project.fastwork.domains.Users;
import io.project.fastwork.domains.Work;
import io.project.fastwork.domains.WorkApplication;
import io.project.fastwork.repositories.UserRepository;
import io.project.fastwork.repositories.WorkAppllicationRepository;
import io.project.fastwork.repositories.WorkRepository;
import io.project.fastwork.services.api.MailServiceApi;
import io.project.fastwork.services.api.UserServiceApi;
import io.project.fastwork.services.api.WorkApplicationServiceApi;
import io.project.fastwork.services.exception.*;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class WorkApplicationServiceImplTest {

    @Autowired
    private WorkApplicationServiceApi workApplicationService;

    @MockBean
    private WorkRepository workRepository;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private WorkAppllicationRepository workAppllicationRepository;

    @MockBean
    private MailServiceApi mailService;

    @Autowired
    private UserServiceApi userService;

    @Test
    @Transactional
    void SaveWorkApplication_WithValidWorkApp_ReturnTrue() throws WorkApplicationAlreadySend, UserNotFound {
        Users worker_by_id = Users.builder()
                .id(777L)
                .userOriginalName("Test")
                .userSoname("Test")
                .build();
        Work work_by_id = Work.builder()
                .id(777L)
                .workHirer(worker_by_id)
                .build();

        Mockito.when(workRepository.getWorkById(777L)).thenReturn(work_by_id);
        Mockito.when(userRepository.getUserById(777L)).thenReturn(worker_by_id);

        Work work = workRepository.getWorkById(777L);
        Users worker = userService.getById(777L);
        WorkApplication valid_work_application = WorkApplication.builder()
                .work(work)
                .worker(worker)
                .build();

        Mockito.when(workAppllicationRepository.save(valid_work_application)).thenReturn(valid_work_application);

        WorkApplication workApplication_saved = workApplicationService.saveWorkApplication(valid_work_application);

        Mockito.verify(workRepository, Mockito.times(1)).getWorkById(777L);
        Mockito.verify(userRepository, Mockito.times(1)).getUserById(777L);
        Mockito.verify(workAppllicationRepository, Mockito.times(1)).save(valid_work_application);
        assertNotNull(workApplication_saved);
    }

    @Test
    @Transactional
    void SaveWorkApplication_WithWorkAppExisted_ThrowException() throws WorkNotFound {
        Work work_by_id = Work.builder()
                .id(777L)
                .build();
        Users worker_by_id = Users.builder()
                .id(777L)
                .build();
        Mockito.when(workRepository.getWorkById(777L)).thenReturn(work_by_id);
        Mockito.when(userRepository.getUserById(777L)).thenReturn(worker_by_id);
        WorkApplication invalid_work_application = WorkApplication.builder()
                .work(Work.builder().id(777L).build())
                .worker(Users.builder().id(777L).build())
                .build();
        Mockito.when(workAppllicationRepository.findByWorkerId(777L)).thenReturn(List.of(invalid_work_application));
        WorkApplicationAlreadySend workApplicationAlreadySend = assertThrows(
                WorkApplicationAlreadySend.class,
                () -> workApplicationService.saveWorkApplication(invalid_work_application)
        );
        Mockito.verify(workRepository, Mockito.times(0)).getWorkById(777L);
        Mockito.verify(userRepository, Mockito.times(0)).getUserById(777L);
        Mockito.verify(workAppllicationRepository, Mockito.times(1)).findByWorkerId(777L);
        Mockito.verify(workAppllicationRepository, Mockito.times(0)).save(invalid_work_application);
        assertTrue(workApplicationAlreadySend.getMessage().contentEquals("Work application already send!"));
    }

    @Test
    @Transactional
    void RejectedWorkApplication_WithValidWorkApp_ReturnTrue() throws WorkApplicationNotFound {
        WorkApplication work_application_by_id = WorkApplication.builder()
                .id(777L)
                .work(Work.builder().id(777L).build())
                .worker(Users.builder().id(777L).build())
                .build();

        Mockito.when(workAppllicationRepository.getWorkApplicationById(777L)).thenReturn(work_application_by_id);
        Mockito.when(workAppllicationRepository.save(work_application_by_id)).thenReturn(work_application_by_id);

        WorkApplication work_application_rejected = workApplicationService.rejectedWorkApplication(777L);

        Mockito.verify(workAppllicationRepository, Mockito.times(1)).save(work_application_by_id);
        Mockito.verify(workAppllicationRepository, Mockito.times(1)).getWorkApplicationById(777L);

        assertEquals(work_application_rejected.getStatusWorkApplication(), StatusWorkApplication.REJECT);
    }

    @Test
    void RejectedWorkApplication_WithInValidWorkApp_ThrowException() throws WorkApplicationNotFound {
        WorkApplicationNotFound workApplicationNotFound = assertThrows(
                WorkApplicationNotFound.class,
                () -> workApplicationService.rejectedWorkApplication(9123L)
        );

        Mockito.verify(workAppllicationRepository, Mockito.times(0)).save(WorkApplication.builder().id(9123L).build());
        Mockito.verify(workAppllicationRepository, Mockito.times(1)).getWorkApplicationById(9123L);
        assertTrue(workApplicationNotFound.getMessage().contentEquals("Work application with id 9123 not found!"));
    }

    @Test
    @Transactional
    void ApprovedWorkApplication_WithValidWorkApp_ReturnTrue() throws WorkApplicationNotFound, WorkAlreadyAdded {
        WorkApplication work_application_by_id = WorkApplication.builder()
                .id(777L)
                .work(Work.builder().id(777L).build())
                .worker(Users.builder().id(777L)
                        .userWorks(new ArrayList<>())
                        .build())
                .build();


        Mockito.when(workAppllicationRepository.getWorkApplicationById(777L)).thenReturn(work_application_by_id);
        Mockito.when(workAppllicationRepository.save(work_application_by_id)).thenReturn(work_application_by_id);

        WorkApplication work_application_approved = workApplicationService.approvedWorkApplication(777L);

        Mockito.verify(workAppllicationRepository, Mockito.times(1)).save(work_application_by_id);
        Mockito.verify(workAppllicationRepository, Mockito.times(1)).getWorkApplicationById(777L);
        assertEquals(work_application_approved.getStatusWorkApplication(), StatusWorkApplication.APPROVE);

    }

    @Test
    void ApproveWorkApplication_WithInValidWorkApp_ThrowException() throws WorkApplicationNotFound {
        WorkApplicationNotFound workApplicationNotFound = assertThrows(
                WorkApplicationNotFound.class,
                () -> workApplicationService.approvedWorkApplication(9123L)
        );
        Mockito.verify(workAppllicationRepository, Mockito.times(0)).save(WorkApplication.builder().id(9123L).build());
        Mockito.verify(workAppllicationRepository, Mockito.times(1)).getWorkApplicationById(9123L);
        assertTrue(workApplicationNotFound.getMessage().contentEquals("Work application with id 9123 not found!"));
    }

    @Test
    void FindWorkApplicationByWorkId_WithValidWorkId_ReturnTrue() throws WorkNotFound {
        WorkApplication work_application_by_id = WorkApplication.builder()
                .id(778L)
                .work(Work.builder().id(777L).build())
                .worker(Users.builder().id(777L).build())
                .build();
        Work work_by_id = Work.builder()
                .id(778L)
                .build();

        Mockito.when(workRepository.getWorkById(778L)).thenReturn(work_by_id);
        Mockito.when(workAppllicationRepository.findByWorkId(778L)).thenReturn(List.of(work_application_by_id));

        List<WorkApplication> workApplicationList = workApplicationService.findByWorkId(778L);

        Mockito.verify(workRepository, Mockito.times(1)).getWorkById(778L);
        Mockito.verify(workAppllicationRepository, Mockito.times(1)).findByWorkId(778L);
        assertFalse(workApplicationList.isEmpty());
    }

    @Test
    void FindWorkApplicationByWorkId_WithNonExistedWork_ThrowException() {
        Mockito.when(workRepository.getWorkById(9123L)).thenReturn(null);
        WorkNotFound workNotFound = assertThrows(
                WorkNotFound.class,
                () -> workApplicationService.findByWorkId(9123L)
        );
        Mockito.verify(workRepository, Mockito.times(1)).getWorkById(9123L);
        Mockito.verify(workAppllicationRepository, Mockito.times(0)).findByWorkId(9123L);
        assertTrue(workNotFound.getMessage().contentEquals("Work with id 9123 not found!"));
    }


    @Test
    void FindWorkApplicationByWorkerid_WithValidWorkerId_ReturnTrue() throws WorkerNotFound, UserNotFound {
        WorkApplication work_application_by_id = WorkApplication.builder()
                .id(778L)
                .work(Work.builder().id(777L).build())
                .worker(Users.builder().id(777L).build())
                .build();
        Users worker_by_id = Users.builder()
                .id(777L)
                .build();

        Mockito.when(userRepository.getUserById(777L)).thenReturn(worker_by_id);
        Mockito.when(workAppllicationRepository.findByWorkerId(777L)).thenReturn(List.of(work_application_by_id));

        List<WorkApplication> workApplicationList = workApplicationService.findByWorkerid(777L);

        Mockito.verify(userRepository, Mockito.times(1)).getUserById(777L);
        Mockito.verify(workAppllicationRepository, Mockito.times(1)).findByWorkerId(777L);

        assertFalse(workApplicationList.isEmpty());
    }

    @Test
    void FindWorkApplicationByWorkId_WithNonExistedWorker_ThrowException() {
        UserNotFound workerkNotFound = assertThrows(
                UserNotFound.class,
                () -> workApplicationService.findByWorkerid(91223L)
        );

        Mockito.verify(userRepository, Mockito.times(1)).getUserById(91223L);
        Mockito.verify(workAppllicationRepository, Mockito.times(0)).findByWorkerId(91223L);
        assertTrue(workerkNotFound.getMessage().contentEquals("User with id 91223 not found"));
    }
}