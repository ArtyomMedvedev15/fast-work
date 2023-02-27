package io.project.fastwork.services.implementation;

import io.project.fastwork.domains.Role;
import io.project.fastwork.domains.StatusUser;
import io.project.fastwork.domains.Users;
import io.project.fastwork.domains.Work;
import io.project.fastwork.repositories.WorkRepository;
import io.project.fastwork.services.api.UserServiceApi;
import io.project.fastwork.services.api.WorkServiceApi;
import io.project.fastwork.services.exception.*;
import org.apache.catalina.User;
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
import java.util.Stack;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@Testcontainers
@Sql(value = "classpath:/sql/initDataBefore.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = "classpath:/sql/clearDataAfter.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class UserServiceImplTest {

    @Autowired
    private UserServiceApi userService;

    @Autowired
    private WorkRepository workServiceApi;
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
    void SaveUser_WithValidUser_ReturnTrue() throws UserAlreadyExisted, UserInvalidDataParemeter {
        Users user_valid = Users.builder()
                .userLogin("UserLogin12")
                .userName("UserTest")
                .userSoname("UserTest")
                .userPassword("Testtest12@a")
                .userEmail("usertest@test.com")
                .userRole(Role.WORKER)
                .build();
        Users user_save_result = userService.saveUser(user_valid);
        assertNotNull(user_save_result);
    }

    @Test
    void SaveUser_WithExistedUser_ThrowException(){
        Users user_valid = Users.builder()
                .userLogin("Login123")
                .userName("UserTest")
                .userSoname("UserTest")
                .userPassword("Testtest12@a")
                .userEmail("usertest@test.com")
                .userRole(Role.WORKER)
                .build();
        UserAlreadyExisted userAlreadyExisted = assertThrows(
                UserAlreadyExisted.class,
                () -> userService.saveUser(user_valid)
        );
         assertTrue(userAlreadyExisted.getMessage().contentEquals("User with username - Login123 already existed. Try yet!"));
    }

    @Test
    void UpdateUser_WithValidUser_ReturnTrue() throws UserNotFound, UserAlreadyExisted, UserInvalidDataParemeter {
        Users user_valid = userService.getById(777L);
        user_valid.setUserName("UpdateUser");
        Users user_update = userService.updateUser(user_valid);
        assertEquals("UpdateUser", user_update.getUsername());
    }

    @Test
    void DeleteUser_WithValidUser_ReturnTrue() throws UserNotFound {
        Users user_valid = userService.getById(779L);
        Users user_deleted = userService.deleteUser(user_valid);
        assertEquals(user_deleted.getUserStatus(), StatusUser.DELETED);
    }

    @Test
    void DeleteUser_WithNonExistedUser_ThrowException(){
        UserNotFound userNotFound = assertThrows(
                UserNotFound.class,
                () ->   userService.deleteUser(Users.builder().id(75233L).build())
        );
         assertTrue(userNotFound.getMessage().contentEquals("User with id 75233 not found"));
    }

    @Test
    void BlockedUser_WithValidUser_ReturnTrue() throws UserNotFound {
        Users user_valid = userService.getById(779L);
        Users user_deleted = userService.blockedUser(user_valid);
        assertEquals(user_deleted.getUserStatus(), StatusUser.BLOCKED);
    }

    @Test
    void BlockedUser_WithNonExistedUser_ThrowException(){
        UserNotFound userNotFound = assertThrows(
                UserNotFound.class,
                () ->   userService.blockedUser(Users.builder().id(75233L).build())
        );
        assertTrue(userNotFound.getMessage().contentEquals("User with id 75233 not found"));
    }

    @Test
    void FindAllActiveUser_ReturnTrue() {
        List<Users>usersListActive = userService.findAllUsersByStatus(StatusUser.ACTIVE);
        assertFalse(usersListActive.isEmpty());
    }
    @Test
    void FindAllDeletedUser_ReturnTrue() {
        List<Users>usersListActive = userService.findAllUsersByStatus(StatusUser.DELETED);
        assertFalse(usersListActive.isEmpty());
    }
    @Test
    void FindAllBlockedUser_ReturnTrue() {
        List<Users>usersListActive = userService.findAllUsersByStatus(StatusUser.BLOCKED);
        assertFalse(usersListActive.isEmpty());
    }

    @Test
    void FindUserbyUsername_WithValidLoginCorrect_ReturnTrue() throws UserNotFound, UserInvalidDataParemeter {
        Users user_by_username = userService.findByUsername("Login123");
        assertEquals("hirer", user_by_username.getUsername());
    }

    @Test
    void FindUserbyUsername_WithInvalidLogin_ThrowException(){
        UserInvalidDataParemeter userInvalidDataParemeter = assertThrows(
                UserInvalidDataParemeter.class,
                () ->   userService.findByUsername("")
        );
        assertTrue(userInvalidDataParemeter.getMessage().contentEquals("Username for search incorrect, try yet"));
    }

    @Test
    void FindUserbyUsername_WithNotFoundUser_ThrowException(){
        UserNotFound userNotFound = assertThrows(
                UserNotFound.class,
                () ->   userService.findByUsername("Samsa")
        );
        assertTrue(userNotFound.getMessage().contentEquals("User with username - Samsa not found"));
    }

    @Test
    void FindUserByEmail_WithValidEmailCorrect_ReturnTrue() throws UserNotFound, UserInvalidDataParemeter {
        Users user_by_username = userService.findByEmail("user2@mail.tex");
        assertEquals("hirer", user_by_username.getUsername());
    }

    @Test
    void FindUserByEmail_WithInvalidEmail_ThrowException(){
        UserInvalidDataParemeter userInvalidDataParemeter = assertThrows(
                UserInvalidDataParemeter.class,
                () ->   userService.findByEmail("")
        );
        assertTrue(userInvalidDataParemeter.getMessage().contentEquals("Email for search incorrect, try yet"));
    }

    @Test
    void FindUserByEmail_WithNonExistedEmail_ThrowException(){
        UserNotFound userNotFound = assertThrows(
                UserNotFound.class,
                () ->   userService.findByEmail("asda2@asd.cas")
        );
        assertTrue(userNotFound.getMessage().contentEquals("User with email - asda2@asd.cas not found"));
    }

    @Test
    void GetUserById_WithExistedUser_ReturnTrue() throws UserNotFound {
        Users user_by_id = userService.getById(779L);
        assertEquals("Logi123s", user_by_id.getUserLogin());
    }

    @Test
    void GetUserById_WithNonExistedUser_ThrowException(){
         UserNotFound userNotFound = assertThrows(
                UserNotFound.class,
                () ->   userService.getById(727727L)
        );
        assertTrue(userNotFound.getMessage().contentEquals("User with id 727727 not found"));
    }

    @Test
    @Transactional
    void AddWorkToWorker_WithValidWork_ReturnTrue() throws UserNotFound, WorkAlreadyAdded {
        Users user_by_id=userService.getById(779L);
        Work work_added = workServiceApi.getWorkById(777L);
        userService.addWorkToWorker(work_added,user_by_id);

        assertEquals(1, userService.getById(779L).getUserWorks().size());
    }

    @Test
    @Transactional
    void AddWorkToWorker_WithExistedWork_ReturnTrue() throws UserNotFound, WorkAlreadyAdded {
        Users user_by_id=userService.getById(781L);
        Work work_added = workServiceApi.getWorkById(777L);
        WorkAlreadyAdded workAlreadyAdded = assertThrows(
                WorkAlreadyAdded.class,
                () ->   userService.addWorkToWorker(work_added,user_by_id)
        );
        assertTrue(workAlreadyAdded.getMessage().contentEquals("Work with id 777 already added to worker with id 781"));
    }

    @Test
    @Transactional
    void RemoveWorkFromWorker_WithValidWork() throws UserNotFound, WorkNotFound {
        Users user_by_id=userService.getById(780L);
        Work work_added = workServiceApi.getWorkById(778L);
        userService.removeWorkFromWorker(work_added,user_by_id);
        assertEquals(0, userService.getById(780L).getUserWorks().size());

    }


    @Test
    @Transactional
    void RemoveWorkFromWorker_WithNonExistedWork() throws UserNotFound, WorkNotFound {
        Users user_by_id=userService.getById(780L);
        Work work_added = workServiceApi.getWorkById(777L);
        userService.removeWorkFromWorker(work_added,user_by_id);
        assertEquals(1, userService.getById(780L).getUserWorks().size());

    }
}