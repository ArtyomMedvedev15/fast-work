package io.project.fastwork.services.implementation;

import io.project.fastwork.domains.StatusWork;
import io.project.fastwork.domains.TypeWork;
import io.project.fastwork.domains.Users;
import io.project.fastwork.domains.Work;
import io.project.fastwork.repositories.TypeWorkRepository;
import io.project.fastwork.repositories.WorkRepository;
import io.project.fastwork.services.api.TypeWorkServiceApi;
import io.project.fastwork.services.api.WorkServiceApi;
import io.project.fastwork.services.exception.WorkAlreadyAdded;
import io.project.fastwork.services.exception.WorkAlreadyExists;
import io.project.fastwork.services.exception.WorkInvalidDataValues;
import io.project.fastwork.services.exception.WorkNotFound;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class WorkServiceImplTest {

    @MockBean
    private WorkRepository workRepository;

    @Autowired
    private WorkServiceApi workService;

    @MockBean
    private TypeWorkRepository typeWorkRepository;

    @Test
    void SaveWorkTest_ReturnTrue() throws WorkInvalidDataValues, WorkAlreadyExists {
        Users userHirer = Users.builder()
                .id(123L)
                .username("Hirer")
                .build();

        TypeWork typeWork = TypeWork.builder()
                .id(123L)
                .typeWorkName("Test")
                .build();

        Work work = Work.builder()
                .workName("Testt")
                .workDescribe("TestTestTest")
                .workPrice(12.0F)
                .workCountPerson(5)
                .workType(typeWork)
                .workHirer(userHirer)
                .build();

        Mockito.when(workRepository.save(work)).thenReturn(work);

        Work savedWork = workService.saveWork(work);

        assertEquals("Testt", savedWork.getWorkName());
        Mockito.verify(workRepository, Mockito.times(1)).save(work);
    }

    @Test
    void SaveWorkTest_WithWorkInvalidReturnTrue() {
        Users userHirer = Users.builder()
                .id(123L)
                .username("Hirer")
                .build();

        TypeWork typeWork = TypeWork.builder()
                .id(123L)
                .typeWorkName("Testt")
                .build();

        Work work = Work.builder()
                .workName("Testt")
                .workDescribe("TestTestTest")
                .workPrice(12.0F)
                .workCountPerson(5)
                .workType(typeWork)
                .workHirer(userHirer)
                .build();

        Mockito.when(workRepository.findAll()).thenReturn(List.of(work));

        WorkAlreadyExists workAlreadyExists = assertThrows(
                WorkAlreadyExists.class,
                () -> workService.saveWork(work)
        );
        Mockito.verify(workRepository, Mockito.times(0)).save(work);
        Mockito.verify(workRepository, Mockito.times(1)).findAll();
    }

    @Test
    void UpdateWorkTest_ReturnTrue() throws WorkInvalidDataValues {
        Users userHirer = Users.builder()
                .id(123L)
                .username("Hirer")
                .build();

        TypeWork typeWork = TypeWork.builder()
                .id(123L)
                .typeWorkName("Test")
                .build();

        Work work = Work.builder()
                .workName("Update")
                .workDescribe("TestTestTest")
                .workPrice(12.0F)
                .workCountPerson(5)
                .workType(typeWork)
                .workHirer(userHirer)
                .build();

        Mockito.when(workRepository.save(work)).thenReturn(work);

        Work updateWork = workService.updateWork(work);

        assertEquals("Update", updateWork.getWorkName());
        Mockito.verify(workRepository, Mockito.times(1)).save(work);
    }

    @Test
    void CloseWorkTest_ReturnTrue() throws WorkNotFound {
        Users userHirer = Users.builder()
                .id(123L)
                .username("Hirer")
                .build();

        TypeWork typeWork = TypeWork.builder()
                .id(123L)
                .typeWorkName("Test")
                .build();

        Work work = Work.builder()
                .id(888L)
                .workName("Closed")
                .workDescribe("TestTestTest")
                .workPrice(12.0F)
                .workCountPerson(5)
                .workType(typeWork)
                .workHirer(userHirer)
                .workStatus(StatusWork.OPEN)
                .build();

        Mockito.when(workRepository.save(work)).thenReturn(work);
        Mockito.when(workRepository.getWorkById(888L)).thenReturn(work);

        Work closeWork = workService.closeWork(work);

        assertEquals(StatusWork.CLOSE, closeWork.getWorkStatus());
        Mockito.verify(workRepository, Mockito.times(1)).save(work);
        Mockito.verify(workRepository, Mockito.times(1)).getWorkById(888L);

    }

    @Test
    void CloseWorkTest_WithNullWork_ThrowException() throws WorkNotFound {

        Mockito.when(workRepository.getWorkById(888L)).thenReturn(null);

        WorkNotFound workNotFound = assertThrows(WorkNotFound.class,
                () -> workService.closeWork(Work.builder().id(888L).build())
        );

        Mockito.verify(workRepository, Mockito.times(0)).save(Work.builder().id(888L).build());
        Mockito.verify(workRepository, Mockito.times(1)).getWorkById(888L);

    }

    @Test
    void OpenWorkTest_ReturnTrue() throws WorkNotFound {
        Users userHirer = Users.builder()
                .id(123L)
                .username("Hirer")
                .build();

        TypeWork typeWork = TypeWork.builder()
                .id(123L)
                .typeWorkName("Test")
                .build();

        Work work = Work.builder()
                .id(888L)
                .workName("Closed")
                .workDescribe("TestTestTest")
                .workPrice(12.0F)
                .workCountPerson(5)
                .workType(typeWork)
                .workHirer(userHirer)
                .workStatus(StatusWork.CLOSE)
                .build();

        Mockito.when(workRepository.save(work)).thenReturn(work);
        Mockito.when(workRepository.getWorkById(888L)).thenReturn(work);

        Work closeWork = workService.openWork(work);

        assertEquals(StatusWork.OPEN, closeWork.getWorkStatus());
        Mockito.verify(workRepository, Mockito.times(1)).save(work);
        Mockito.verify(workRepository, Mockito.times(1)).getWorkById(888L);
    }

    @Test
    void OpenWorkTest_WithNullWork_ThrowException() throws WorkNotFound {
        Mockito.when(workRepository.getWorkById(888L)).thenReturn(null);

        WorkNotFound workNotFound = assertThrows(WorkNotFound.class,
                () -> workService.openWork(Work.builder().id(888L).build())
        );

        Mockito.verify(workRepository, Mockito.times(0)).save(Work.builder().id(888L).build());
        Mockito.verify(workRepository, Mockito.times(1)).getWorkById(888L);

    }

    @Test
    void ExceptionWorkTest_ReturnTrue() throws WorkNotFound {
        Users userHirer = Users.builder()
                .id(123L)
                .username("Hirer")
                .build();

        TypeWork typeWork = TypeWork.builder()
                .id(123L)
                .typeWorkName("Test")
                .build();

        Work work = Work.builder()
                .id(888L)
                .workName("Closed")
                .workDescribe("TestTestTest")
                .workPrice(12.0F)
                .workCountPerson(5)
                .workType(typeWork)
                .workHirer(userHirer)
                .workStatus(StatusWork.CLOSE)
                .build();

        Mockito.when(workRepository.save(work)).thenReturn(work);
        Mockito.when(workRepository.getWorkById(888L)).thenReturn(work);

        Work closeWork = workService.exceptionWork(work);

        assertEquals(StatusWork.EXPECTATION, closeWork.getWorkStatus());
        Mockito.verify(workRepository, Mockito.times(1)).save(work);
        Mockito.verify(workRepository, Mockito.times(1)).getWorkById(888L);
    }

    @Test
    void ExceptionWorkTest_WithNullWork_ThrowException() throws WorkNotFound {
        Mockito.when(workRepository.getWorkById(888L)).thenReturn(null);

        WorkNotFound workNotFound = assertThrows(WorkNotFound.class,
                () -> workService.exceptionWork(Work.builder().id(888L).build())
        );

        Mockito.verify(workRepository, Mockito.times(0)).save(Work.builder().id(888L).build());
        Mockito.verify(workRepository, Mockito.times(1)).getWorkById(888L);

    }

    @Test
    void GetWorkByIdTest_ReturnTrue() throws WorkNotFound {
        Users userHirer = Users.builder()
                .id(123L)
                .username("Hirer")
                .build();

        TypeWork typeWork = TypeWork.builder()
                .id(123L)
                .typeWorkName("Test")
                .build();

        Work work = Work.builder()
                .id(888L)
                .workName("Getbyid")
                .workDescribe("TestTestTest")
                .workPrice(12.0F)
                .workCountPerson(5)
                .workType(typeWork)
                .workHirer(userHirer)
                .workStatus(StatusWork.CLOSE)
                .build();

        Mockito.when(workRepository.getWorkById(888L)).thenReturn(work);

        Work workById = workService.getWorkById(888L);

        assertEquals("Getbyid", workById.getWorkName());
        Mockito.verify(workRepository, Mockito.times(1)).getWorkById(888L);
    }

    @Test
    void GetWorkByIdTest_WithNonExists_ThrowException() throws WorkNotFound {
        Mockito.when(workRepository.getWorkById(888L)).thenReturn(null);

        WorkNotFound workNotFound =assertThrows(WorkNotFound.class,
                ()->workService.getWorkById(888L)
        );

        Mockito.verify(workRepository, Mockito.times(1)).getWorkById(888L);
    }


    @Test
    void FindWorkByNameTest_ReturnTrue() {
        Users userHirer = Users.builder()
                .id(123L)
                .username("Hirer")
                .build();

        TypeWork typeWork = TypeWork.builder()
                .id(123L)
                .typeWorkName("Test")
                .build();

        Work work = Work.builder()
                .id(888L)
                .workName("FindTest")
                .workDescribe("TestTestTest")
                .workPrice(12.0F)
                .workCountPerson(5)
                .workType(typeWork)
                .workHirer(userHirer)
                .workStatus(StatusWork.OPEN)
                .build();

        Mockito.when(workRepository.findWorkByName("Find")).thenReturn(List.of(work));

        List<Work> workByName = workService.findWorkByName("Find");

        assertEquals("FindTest",workByName.get(0).getWorkName());
        Mockito.verify(workRepository,Mockito.times(1)).findWorkByName("Find");

    }

    @Test
    void FindWorkByTypeWorkTest_ReturnTrue() {
        Users userHirer = Users.builder()
                .id(123L)
                .username("Hirer")
                .build();

        TypeWork typeWork = TypeWork.builder()
                .id(123L)
                .typeWorkName("Test")
                .build();

        Work work = Work.builder()
                .id(888L)
                .workName("FindTest")
                .workDescribe("TestTestTest")
                .workPrice(12.0F)
                .workCountPerson(5)
                .workType(typeWork)
                .workHirer(userHirer)
                .workStatus(StatusWork.OPEN)
                .build();

        Mockito.when(typeWorkRepository.findByTypeWorkName("Test")).thenReturn(typeWork);
        Mockito.when(workRepository.findWorkByType(123L)).thenReturn(List.of(work));

        List<Work> workByTypeWork = workService.findWorkByTypeWork(typeWork);

        assertEquals("Test",workByTypeWork.get(0).getWorkType().getTypeWorkName());

        Mockito.verify(workRepository,Mockito.times(1)).findWorkByType(123L);
        Mockito.verify(typeWorkRepository,Mockito.times(1)).findByTypeWorkName("Test");
    }

    @Test
    void FindWorkByTypeWorkTest_WithTypeNull_ReturnTrue() {
        TypeWork typeWork = TypeWork.builder()
                .id(123L)
                .typeWorkName("Test")
                .build();
        Mockito.when(typeWorkRepository.findByTypeWorkName("Test")).thenReturn(null);
        Mockito.when(workRepository.findWorkByType(123L)).thenReturn(Collections.emptyList());

        List<Work> workByTypeWork = workService.findWorkByTypeWork(typeWork);

        assertEquals(0, workByTypeWork.size());

        Mockito.verify(workRepository,Mockito.times(0)).findWorkByType(123L);
        Mockito.verify(typeWorkRepository,Mockito.times(1)).findByTypeWorkName("Test");
    }

    @Test
    void FindAllWorkTest_ReturnTrue() {
        Users userHirer = Users.builder()
                .id(123L)
                .username("Hirer")
                .build();

        TypeWork typeWork = TypeWork.builder()
                .id(123L)
                .typeWorkName("Test")
                .build();

        Work work = Work.builder()
                .id(888L)
                .workName("FindTest")
                .workDescribe("TestTestTest")
                .workPrice(12.0F)
                .workCountPerson(5)
                .workType(typeWork)
                .workHirer(userHirer)
                .workStatus(StatusWork.OPEN)
                .build();

        Mockito.when(workRepository.findAll()).thenReturn(List.of(work));

        List<Work> allWork = workService.findAllWork();

        assertEquals(1, allWork.size());

        Mockito.verify(workRepository,Mockito.times(1)).findAll();
    }

    @Test
    void findAllOpenedWork() {
    }

    @Test
    void findAllClosedWork() {
    }

    @Test
    void findAllExceptionWork() {
    }
}