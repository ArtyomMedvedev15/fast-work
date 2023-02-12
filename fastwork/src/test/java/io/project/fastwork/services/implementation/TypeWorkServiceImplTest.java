package io.project.fastwork.services.implementation;

import io.project.fastwork.domains.TypeWork;
import io.project.fastwork.services.api.TypeWorkServiceApi;
import io.project.fastwork.services.exception.LocationWithInvalidArgumentsException;
import io.project.fastwork.services.exception.TypeWorkAlreadyExistsException;
import io.project.fastwork.services.exception.TypeWorkInvalidParameterException;
import io.project.fastwork.services.exception.TypeWorkNotFound;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.lang.reflect.Type;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Testcontainers
@Sql(value = "classpath:/sql/initDataBefore.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = "classpath:/sql/clearDataAfter.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class TypeWorkServiceImplTest {

    @Qualifier("typeWorkServiceImpl")
    @Autowired
    private TypeWorkServiceApi typeWorkService;

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
    void SaveTypeWork_WithValidTypeWork_ReturnTrue() throws TypeWorkInvalidParameterException, TypeWorkAlreadyExistsException {
        TypeWork type_work_valid = TypeWork.builder()
                .id(777L)
                .typeWorkName("Type")
                .typeWorkDescribe("TypeWorkTypeWorkType")
                .build();

        TypeWork type_work_save = typeWorkService.saveTypeWork(type_work_valid);

        assertNotNull(type_work_save);
    }

    @Test
    void SaveTypeWork_WithInValidTypeWorkNameLessLength3_ThrowException(){
        TypeWork type_work_invalid = TypeWork.builder()
                .id(777L)
                .typeWorkName("Typ")
                .typeWorkDescribe("TypeWorkTypeWorkType")
                .build();

         TypeWorkInvalidParameterException typeWorkInvalidParameterException = assertThrows(
                TypeWorkInvalidParameterException.class,
                () -> typeWorkService.saveTypeWork(type_work_invalid)
        );
        assertTrue(typeWorkInvalidParameterException.getMessage().contentEquals("Check string parameters, something was wrong!"));
    }
    @Test
    void SaveTypeWork_WithInValidTypeWorkNameMoreLength40_ThrowException(){
        TypeWork type_work_invalid = TypeWork.builder()
                .id(777L)
                .typeWorkName("TypeTypeTypeTypeTypeTypeTypeTypeTypeTypeTypeType")
                .typeWorkDescribe("TypeWorkTypeWorkType")
                .build();

        TypeWorkInvalidParameterException typeWorkInvalidParameterException = assertThrows(
                TypeWorkInvalidParameterException.class,
                () -> typeWorkService.saveTypeWork(type_work_invalid)
        );
        assertTrue(typeWorkInvalidParameterException.getMessage().contentEquals("Check string parameters, something was wrong!"));
    }

    @Test
    void SaveTypeWork_WithInValidTypeWorkNameEmpty_ThrowException(){
        TypeWork type_work_invalid = TypeWork.builder()
                .id(777L)
                .typeWorkName("")
                .typeWorkDescribe("TypeWorkTypeWorkType")
                .build();

        TypeWorkInvalidParameterException typeWorkInvalidParameterException = assertThrows(
                TypeWorkInvalidParameterException.class,
                () -> typeWorkService.saveTypeWork(type_work_invalid)
        );
        assertTrue(typeWorkInvalidParameterException.getMessage().contentEquals("Check string parameters, something was wrong!"));
    }

    @Test
    void SaveTypeWork_WithInValidTypeWorkDescribeEmpty_ThrowException(){
        TypeWork type_work_invalid = TypeWork.builder()
                .id(777L)
                .typeWorkName("Type")
                .typeWorkDescribe("")
                .build();

        TypeWorkInvalidParameterException typeWorkInvalidParameterException = assertThrows(
                TypeWorkInvalidParameterException.class,
                () -> typeWorkService.saveTypeWork(type_work_invalid)
        );
        assertTrue(typeWorkInvalidParameterException.getMessage().contentEquals("Check string parameters, something was wrong!"));
    }
    @Test
    void SaveTypeWork_WithInValidTypeWorkDescribeLengthLess15_ThrowException(){
        TypeWork type_work_invalid = TypeWork.builder()
                .id(777L)
                .typeWorkName("Type")
                .typeWorkDescribe("Type")
                .build();

        TypeWorkInvalidParameterException typeWorkInvalidParameterException = assertThrows(
                TypeWorkInvalidParameterException.class,
                () -> typeWorkService.saveTypeWork(type_work_invalid)
        );
        assertTrue(typeWorkInvalidParameterException.getMessage().contentEquals("Check string parameters, something was wrong!"));
    }

    @Test
    void SaveTypeWork_WithInValidTypeWorkDescribeLengthMore256_ThrowException(){
        TypeWork type_work_invalid = TypeWork.builder()
                .id(777L)
                .typeWorkName("Type")
                .typeWorkDescribe("TypeWorkTypeWorkTypeWorkTypeWorkTypeWorkTypeWorkTypeWork" +
                        "ypeWorkTypeWorkTypeWorkTypeWorkTypeWorkTypeWorkTy" +
                        "peWorkTypeWorkTypeWorkTypeW" +
                        "orkTypeWorkTypeWorkTypeWorkTypeWorkTypeWorkTypeWorkTypeWorkTypeWorkT" +
                        "ypeWorkTypeWorkTypeWorkTypeWorkTypeWorkTypeWorkTypeWorkWork")
                .build();

        TypeWorkInvalidParameterException typeWorkInvalidParameterException = assertThrows(
                TypeWorkInvalidParameterException.class,
                () -> typeWorkService.saveTypeWork(type_work_invalid)
        );
        assertTrue(typeWorkInvalidParameterException.getMessage().contentEquals("Check string parameters, something was wrong!"));
    }

    @Test
    void SaveTypeWork_WithInValidTypeWorkWithExisted_ThrowException(){
        TypeWork type_work_invalid = TypeWork.builder()
                .id(777L)
                .typeWorkName("test")
                .typeWorkDescribe("TypeWorkTypeWork")
                .build();

        TypeWorkAlreadyExistsException typeWorkInvalidParameterException = assertThrows(
                TypeWorkAlreadyExistsException.class,
                () -> typeWorkService.saveTypeWork(type_work_invalid)
        );
        assertTrue(typeWorkInvalidParameterException.getMessage().contentEquals("Type work already exists, try yet."));
    }

    @Test
    void UpdateTypeWork_WithValidTypeWork_ReturnTrue() throws TypeWorkInvalidParameterException, TypeWorkAlreadyExistsException {
        TypeWork type_work_valid = typeWorkService.findAll().stream().filter(o1->o1.getTypeWorkName().equals("test"))
                .findFirst().orElse(null);
        type_work_valid.setTypeWorkName("UpdateTest");
        TypeWork updated_type_work = typeWorkService.updateTypeWork(type_work_valid);
        assertNotNull(updated_type_work);
    }

    @Test
    void UpdateTypeWork_WithInValidTypeWorkNameLengthLess4_ThrowException(){
        TypeWork type_work_Invalid = typeWorkService.findAll().stream().filter(o1->o1.getTypeWorkName().equals("test"))
                .findFirst().orElse(null);
        type_work_Invalid.setTypeWorkName("tes");
        TypeWorkInvalidParameterException typeWorkInvalidParameterException = assertThrows(
                TypeWorkInvalidParameterException.class,
                () -> typeWorkService.updateTypeWork(type_work_Invalid)
        );
        assertTrue(typeWorkInvalidParameterException.getMessage().contentEquals("Check string parameters, something was wrong!"));
    }

    @Test
    void UpdateTypeWork_WithInValidTypeWorkNameEmpty_ThrowException(){
        TypeWork type_work_Invalid = typeWorkService.findAll().stream().filter(o1->o1.getTypeWorkName().equals("test"))
                .findFirst().orElse(null);
        type_work_Invalid.setTypeWorkName("");
        TypeWorkInvalidParameterException typeWorkInvalidParameterException = assertThrows(
                TypeWorkInvalidParameterException.class,
                () -> typeWorkService.updateTypeWork(type_work_Invalid)
        );
        assertTrue(typeWorkInvalidParameterException.getMessage().contentEquals("Check string parameters, something was wrong!"));
    }

    @Test
    void UpdateTypeWork_WithInValidTypeWorkDescribeLengthLess15_ThrowException() throws TypeWorkNotFound {
        TypeWork type_work_Invalid = typeWorkService.getTypeWorkById(777L);
        type_work_Invalid.setTypeWorkDescribe("tes");
        TypeWorkInvalidParameterException typeWorkInvalidParameterException = assertThrows(
                TypeWorkInvalidParameterException.class,
                () -> typeWorkService.updateTypeWork(type_work_Invalid)
        );
        assertTrue(typeWorkInvalidParameterException.getMessage().contentEquals("Check string parameters, something was wrong!"));
    }

    @Test
    void UpdateTypeWork_WithInValidTypeWorkDescribeLengthMore256_ThrowException(){
        TypeWork type_work_Invalid = typeWorkService.findAll().stream().filter(o1->o1.getTypeWorkName().equals("test"))
                .findFirst().orElse(null);
               type_work_Invalid.setTypeWorkDescribe("TypeWorkTypeWorkTypeWorkTypeWorkTypeWorkTypeWorkTypeWork" +
                "ypeWorkTypeWorkTypeWorkTypeWorkTypeWorkTypeWorkTy" +
                "peWorkTypeWorkTypeWorkTypeW" +
                "orkTypeWorkTypeWorkTypeWorkTypeWorkTypeWorkTypeWorkTypeWorkTypeWorkT" +
                "ypeWorkTypeWorkTypeWorkTypeWorkTypeWorkTypeWorkTypeWorkWork");
        TypeWorkInvalidParameterException typeWorkInvalidParameterException = assertThrows(
                TypeWorkInvalidParameterException.class,
                () -> typeWorkService.updateTypeWork(type_work_Invalid)
        );
        assertTrue(typeWorkInvalidParameterException.getMessage().contentEquals("Check string parameters, something was wrong!"));
    }

    @Test
    void UpdateTypeWork_WithInValidTypeWorkDescribeEmpty_ThrowException(){
        TypeWork type_work_Invalid = typeWorkService.findAll().stream().filter(o1->o1.getTypeWorkName().equals("test"))
                .findFirst().orElse(null);
        type_work_Invalid.setTypeWorkDescribe("");
        TypeWorkInvalidParameterException typeWorkInvalidParameterException = assertThrows(
                TypeWorkInvalidParameterException.class,
                () -> typeWorkService.updateTypeWork(type_work_Invalid)
        );

        assertTrue(typeWorkInvalidParameterException.getMessage().contentEquals("Check string parameters, something was wrong!"));

    }

    @Test
    void DeleteTypeWork_WithValidTypeWork_ReturnTrue() throws TypeWorkNotFound {
        TypeWork type_work_valid = typeWorkService.findAll().stream().filter(o1->o1.getTypeWorkName().equals("test"))
                .findFirst().orElse(null);
        TypeWork type_work_deleted = typeWorkService.deleteTypeWork(type_work_valid);
        assertNotNull(type_work_deleted);
    }
    @Test
    void DeleteTypeWork_WithTypeWorkNull_ThrowException() {
        TypeWorkNotFound typeWorkNotFound = assertThrows(
                TypeWorkNotFound.class,
                () -> typeWorkService.deleteTypeWork(TypeWork.builder().id(12L).build())

        );
        assertTrue(typeWorkNotFound.getMessage().contentEquals("Type work with id 12 not found"));
    }
    @Test
    void FindAllTypeWork_ReturnTrue() {
        List<TypeWork>typeWorksList = typeWorkService.findAll();
        assertFalse(typeWorksList.isEmpty());
    }
}