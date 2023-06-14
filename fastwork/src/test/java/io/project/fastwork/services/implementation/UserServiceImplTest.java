package io.project.fastwork.services.implementation;

import io.project.fastwork.domains.Role;
import io.project.fastwork.domains.StatusUser;
import io.project.fastwork.domains.Users;
import io.project.fastwork.domains.Work;
import io.project.fastwork.repositories.UserRepository;
import io.project.fastwork.repositories.WorkRepository;
import io.project.fastwork.services.api.UserServiceApi;
import io.project.fastwork.services.exception.*;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.stubbing.OngoingStubbing;
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

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceImplTest {

    @Autowired
    private UserServiceApi userService;

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
    @MockBean
    private WorkRepository workRepository;

    @MockBean
    private UserRepository userRepository;
    @Test
    void SaveUser_WithValidUser_ReturnTrue() throws UserAlreadyExisted, UserInvalidDataParemeter {
        Users user_valid = Users.builder()
                .username("JHaris12")
                .userOriginalName("JHarissd")
                .userSoname("Bronson")
                .userPassword("QwErTy132!z")
                .userEmail("monim@gmail.com")
                .userRole(Role.WORKER)
                .build();
        Mockito.when(userRepository.save(user_valid)).thenReturn(user_valid);

        Users user_save_result = userService.saveUser(user_valid);
        Mockito.verify(userRepository, Mockito.times(1)).save(user_valid);
        Mockito.verify(userRepository, Mockito.times(1)).findByLogin("JHaris12");
        assertNotNull(user_save_result);
    }

    @Test
    void SaveUser_WithExistedUser_ThrowException() {
        Users user_exised = Users.builder()
                .username("Login123")
                .userOriginalName("UserTest")
                .userSoname("UserTest")
                .userPassword("Testtest12@a")
                .userEmail("usertest@test.com")
                .userRole(Role.WORKER)
                .build();
        Mockito.when(userRepository.findByLogin("Login123")).thenReturn(user_exised);

        UserAlreadyExisted userAlreadyExisted = assertThrows(
                UserAlreadyExisted.class,
                () -> userService.saveUser(user_exised)
        );

        Mockito.verify(userRepository, Mockito.times(0)).save(user_exised);
        Mockito.verify(userRepository, Mockito.times(1)).findByLogin("Login123");
        assertTrue(userAlreadyExisted.getMessage().contentEquals("User with username - Login123 already existed. Try yet!"));
    }

    @Test
    void UpdateUser_WithValidUser_ReturnTrue() throws UserNotFound, UserAlreadyExisted, UserInvalidDataParemeter {
        Users user_by_id = Users.builder()
                .id(777L)
                .username("Login123")
                .userOriginalName("UserTest")
                .userSoname("UserTest")
                .userPassword("Testtest12@a")
                .userEmail("usertest@test.com")
                .userRole(Role.WORKER)
                .build();
        Mockito.when(userRepository.getUserById(777L)).thenReturn(user_by_id);
        Mockito.when(userRepository.save(user_by_id)).thenReturn(user_by_id);
        Users user_valid = userService.getById(777L);
        user_valid.setUsername("UpdateUser");
        Users user_update = userService.updateUser(user_valid);

        Mockito.verify(userRepository, Mockito.times(1)).save(user_valid);
        Mockito.verify(userRepository, Mockito.times(1)).getUserById(777L);
        assertEquals("UpdateUser", user_update.getUsername());
    }

    @Test
    void DeleteUser_WithValidUser_ReturnTrue() throws UserNotFound {
        Users user_by_id = Users.builder()
                .id(777L)
                .username("Login123")
                .userOriginalName("UserTest")
                .userSoname("UserTest")
                .userPassword("Testtest12@a")
                .userEmail("usertest@test.com")
                .userRole(Role.WORKER)
                .build();
        Mockito.when(userRepository.getUserById(777L)).thenReturn(user_by_id);
        Mockito.when(userRepository.save(user_by_id)).thenReturn(user_by_id);
        Users user_valid = userService.getById(777L);
        Users user_deleted = userService.deleteUser(user_valid);

        Mockito.verify(userRepository, Mockito.times(1)).save(user_valid);
        Mockito.verify(userRepository, Mockito.times(2)).getUserById(777L);
        assertEquals(user_deleted.getUserStatus(), StatusUser.DELETED);
    }

    @Test
    void DeleteUser_WithNonExistedUser_ThrowException() {
        Mockito.when(userRepository.getUserById(75233L)).thenReturn(null);
        UserNotFound userNotFound = assertThrows(
                UserNotFound.class,
                () -> userService.deleteUser(Users.builder().id(75233L).build())
        );
        Mockito.verify(userRepository, Mockito.times(0)).save(Users.builder().id(75233L).build());
        Mockito.verify(userRepository, Mockito.times(1)).getUserById(75233L);
        assertTrue(userNotFound.getMessage().contentEquals("User with id 75233 not found"));
    }

    @Test
    void BlockedUser_WithValidUser_ReturnTrue() throws UserNotFound {
        Users user_by_id = Users.builder()
                .id(779L)
                .username("Login123")
                .userOriginalName("UserTest")
                .userSoname("UserTest")
                .userPassword("Testtest12@a")
                .userEmail("usertest@test.com")
                .userRole(Role.WORKER)
                .build();
        Mockito.when(userRepository.getUserById(779L)).thenReturn(user_by_id);
        Mockito.when(userRepository.save(user_by_id)).thenReturn(user_by_id);

        Users user_valid = userService.getById(779L);
        Users user_deleted = userService.blockedUser(user_valid);

        Mockito.verify(userRepository, Mockito.times(1)).save(user_valid);
        Mockito.verify(userRepository, Mockito.times(2)).getUserById(779L);
        assertEquals(user_deleted.getUserStatus(), StatusUser.BLOCKED);
    }

    @Test
    void BlockedUser_WithNonExistedUser_ThrowException() {
        Mockito.when(userRepository.getUserById(75233L)).thenReturn(null);
        UserNotFound userNotFound = assertThrows(
                UserNotFound.class,
                () -> userService.blockedUser(Users.builder().id(75233L).build())
        );
        Mockito.verify(userRepository, Mockito.times(0)).save(Users.builder().id(75233L).build());
        Mockito.verify(userRepository, Mockito.times(1)).getUserById(75233L);
        assertTrue(userNotFound.getMessage().contentEquals("User with id 75233 not found"));
    }

    @Test
    void FindAllActiveUser_ReturnTrue() {
        Users user_valid = Users.builder()
                .username("JHaris12")
                .userOriginalName("JHarissd")
                .userSoname("Bronson")
                .userPassword("QwErTy132!z")
                .userEmail("monim@gmail.com")
                .userRole(Role.WORKER)
                .userStatus(StatusUser.ACTIVE)
                .build();

        Mockito.when(userRepository.findAll()).thenReturn(List.of(user_valid));
        List<Users> usersListActive = userService.findAllUsersByStatus(StatusUser.ACTIVE);
        Mockito.verify(userRepository, Mockito.times(1)).findAll();
        assertFalse(usersListActive.isEmpty());
    }

    @Test
    void FindAllDeletedUser_ReturnTrue() {
        Users user_valid = Users.builder()
                .username("JHaris12")
                .userOriginalName("JHarissd")
                .userSoname("Bronson")
                .userPassword("QwErTy132!z")
                .userEmail("monim@gmail.com")
                .userRole(Role.WORKER)
                .userStatus(StatusUser.DELETED)
                .build();

        Mockito.when(userRepository.findAll()).thenReturn(List.of(user_valid));
        List<Users> usersListActive = userService.findAllUsersByStatus(StatusUser.DELETED);
        Mockito.verify(userRepository, Mockito.times(1)).findAll();
        assertFalse(usersListActive.isEmpty());
    }

    @Test
    void FindAllBlockedUser_ReturnTrue() {
        Users user_valid = Users.builder()
                .username("JHaris12")
                .userOriginalName("JHarissd")
                .userSoname("Bronson")
                .userPassword("QwErTy132!z")
                .userEmail("monim@gmail.com")
                .userRole(Role.WORKER)
                .userStatus(StatusUser.BLOCKED)
                .build();

        Mockito.when(userRepository.findAll()).thenReturn(List.of(user_valid));
        List<Users> usersListActive = userService.findAllUsersByStatus(StatusUser.BLOCKED);
        Mockito.verify(userRepository, Mockito.times(1)).findAll();
        assertFalse(usersListActive.isEmpty());
    }

    @Test
    void FindUserbyUsername_WithValidLoginCorrect_ReturnTrue() throws UserNotFound, UserInvalidDataParemeter {
        Users user_valid = Users.builder()
                .username("JHaris12")
                .userOriginalName("JHarissd")
                .userSoname("Bronson")
                .userPassword("QwErTy132!z")
                .userEmail("monim@gmail.com")
                .userRole(Role.WORKER)
                .userStatus(StatusUser.DELETED)
                .build();
        Mockito.when(userRepository.findByLogin("JHaris12")).thenReturn(user_valid);
        Users user_by_username = userService.findByLogin("JHaris12");

        Mockito.verify(userRepository, Mockito.times(1)).findByLogin("JHaris12");
        assertEquals("JHaris12", user_by_username.getUsername());
    }

    @Test
    void FindUserbyUsername_WithInvalidLogin_ThrowException() {
        UserInvalidDataParemeter userInvalidDataParemeter = assertThrows(
                UserInvalidDataParemeter.class,
                () -> userService.findByLogin("")
        );

        Mockito.verify(userRepository, Mockito.times(0)).findByLogin("JHaris12");
        assertTrue(userInvalidDataParemeter.getMessage().contentEquals("Username for search incorrect, try yet"));
    }

    @Test
    void FindUserbyUsername_WithNotFoundUser_ThrowException() {
        Mockito.when(userRepository.findByLogin("Samsa")).thenReturn(null);
        UserNotFound userNotFound = assertThrows(
                UserNotFound.class,
                () -> userService.findByLogin("Samsa")
        );
        Mockito.verify(userRepository, Mockito.times(1)).findByLogin("Samsa");
        assertTrue(userNotFound.getMessage().contentEquals("User with username - Samsa not found"));
    }

    @Test
    @Transactional
    void FindUserByEmail_WithValidEmailCorrect_ReturnTrue() throws UserNotFound, UserInvalidDataParemeter {
        Users user_valid = Users.builder()
                .username("JHaris12")
                .userOriginalName("JHarissd")
                .userSoname("Bronson")
                .userPassword("QwErTy132!z")
                .userEmail("monim@gmail.com")
                .userRole(Role.WORKER)
                .userStatus(StatusUser.DELETED)
                .build();
        Mockito.when(userRepository.findbyEmail("monim@gmail.com")).thenReturn(user_valid);
        Users user_by_username = userService.findByEmail("monim@gmail.com");
        Mockito.verify(userRepository, Mockito.times(1)).findbyEmail("monim@gmail.com");
        assertEquals("JHarissd", user_by_username.getUserOriginalName());
    }

    @Test
    void FindUserByEmail_WithInvalidEmail_ThrowException() {
        UserInvalidDataParemeter userInvalidDataParemeter = assertThrows(
                UserInvalidDataParemeter.class,
                () -> userService.findByEmail("")
        );
        Mockito.verify(userRepository, Mockito.times(0)).findbyEmail("");
        assertTrue(userInvalidDataParemeter.getMessage().contentEquals("Email for search incorrect, try yet"));
    }

    @Test
    void FindUserByEmail_WithNonExistedEmail_ThrowException() {
        Mockito.when(userRepository.findbyEmail("asda2@asd.cas")).thenReturn(null);
        UserNotFound userNotFound = assertThrows(
                UserNotFound.class,
                () -> userService.findByEmail("asda2@asd.cas")
        );
        Mockito.verify(userRepository, Mockito.times(1)).findbyEmail("asda2@asd.cas");
        assertTrue(userNotFound.getMessage().contentEquals("User with email - asda2@asd.cas not found"));
    }

    @Test
    void GetUserById_WithExistedUser_ReturnTrue() throws UserNotFound {
        Users user_valid = Users.builder()
                .id(779L)
                .username("JHaris12")
                .userOriginalName("JHarissd")
                .userSoname("Bronson")
                .userPassword("QwErTy132!z")
                .userEmail("monim@gmail.com")
                .userRole(Role.WORKER)
                .userStatus(StatusUser.DELETED)
                .build();
        Mockito.when(userRepository.getUserById(779L)).thenReturn(user_valid);
        Users user_by_id = userService.getById(779L);
        Mockito.verify(userRepository, Mockito.times(1)).getUserById(779L);
        assertEquals("JHaris12", user_by_id.getUsername());
    }

    @Test
    void GetUserById_WithNonExistedUser_ThrowException() {
        Mockito.when(userRepository.getUserById(727727L)).thenReturn(null);
        UserNotFound userNotFound = assertThrows(
                UserNotFound.class,
                () -> userService.getById(727727L)
        );

        Mockito.verify(userRepository, Mockito.times(1)).getUserById(727727L);
        assertTrue(userNotFound.getMessage().contentEquals("User with id 727727 not found"));
    }

    @Test
    @Transactional
    void AddWorkToWorker_WithValidWork_ReturnTrue() throws UserNotFound, WorkAlreadyAdded {
        Users user_valid = Users.builder()
                .id(779L)
                .username("JHaris12")
                .userOriginalName("JHarissd")
                .userSoname("Bronson")
                .userPassword("QwErTy132!z")
                .userEmail("monim@gmail.com")
                .userRole(Role.WORKER)
                .userStatus(StatusUser.DELETED)
                .userWorks(new ArrayList<>())
                .build();
        Work work_by_id = Work.builder()
                .id(777L)
                .build();

        Mockito.when(userRepository.getUserById(779L)).thenReturn(user_valid);
        Mockito.when(userRepository.save(user_valid)).thenReturn(user_valid);
        Mockito.when(workRepository.getWorkById(777L)).thenReturn(work_by_id);

        Users user_by_id = userService.getById(779L);
        Work work_added = workRepository.getWorkById(777L);
        userService.addWorkToWorker(work_added, user_by_id);

        Mockito.verify(userRepository, Mockito.times(1)).save(user_valid);
        Mockito.verify(userRepository, Mockito.times(1)).getUserById(779L);
        assertEquals(1, userService.getById(779L).getUserWorks().size());
    }

    @Test
    @Transactional
    void AddWorkToWorker_WithExistedWork_ReturnTrue() throws UserNotFound, WorkAlreadyAdded {
        Work work_by_id = Work.builder()
                .id(777L)
                .workName("test")
                .build();
        Users user_valid = Users.builder()
                .id(779L)
                .username("JHaris12")
                .userOriginalName("JHarissd")
                .userSoname("Bronson")
                .userPassword("QwErTy132!z")
                .userEmail("monim@gmail.com")
                .userRole(Role.WORKER)
                .userStatus(StatusUser.DELETED)
                .userWorks(List.of(work_by_id))
                .build();

        Mockito.when(userRepository.getUserById(779L)).thenReturn(user_valid);
        Mockito.when(workRepository.getWorkById(777L)).thenReturn(work_by_id);

        Users user_by_id = userService.getById(779L);
        Work work_added = workRepository.getWorkById(777L);
        WorkAlreadyAdded workAlreadyAdded = assertThrows(
                WorkAlreadyAdded.class,
                () -> userService.addWorkToWorker(work_added, user_by_id)
        );
        Mockito.verify(userRepository, Mockito.times(0)).save(user_valid);
        Mockito.verify(userRepository, Mockito.times(1)).getUserById(779L);
        System.out.println("MES " + workAlreadyAdded.getMessage());
        assertTrue(workAlreadyAdded.getMessage().contentEquals("Work with id 777 already added to worker with id 779"));
    }

    @Test
    @Transactional
    void RemoveWorkFromWorker_WithValidWork() throws UserNotFound, WorkNotFound {
        Work work_by_id = Work.builder()
                .id(777L)
                .workName("test")
                .build();
        Users user_valid = Users.builder()
                .id(779L)
                .username("JHaris12")
                .userOriginalName("JHarissd")
                .userSoname("Bronson")
                .userPassword("QwErTy132!z")
                .userEmail("monim@gmail.com")
                .userRole(Role.WORKER)
                .userStatus(StatusUser.DELETED)
                .userWorks(new ArrayList<>(Collections.singletonList(work_by_id)))
                .build();


        Mockito.when(userRepository.getUserById(779L)).thenReturn(user_valid);
        Mockito.when(userRepository.save(user_valid)).thenReturn(user_valid);
        Mockito.when(workRepository.getWorkById(777L)).thenReturn(work_by_id);

        Users user_by_id = userService.getById(779L);
        Work work_removed = workRepository.getWorkById(777L);
        userService.removeWorkFromWorker(work_removed, user_by_id);

        Mockito.verify(userRepository, Mockito.times(1)).save(user_valid);
        Mockito.verify(userRepository, Mockito.times(1)).getUserById(779L);
        assertEquals(0, userService.getById(779L).getUserWorks().size());
    }


    @Test
    @Transactional
    void RemoveWorkFromWorker_WithNonExistedWork() throws UserNotFound, WorkNotFound {
        Work work_by_id = Work.builder()
                .id(777L)
                .build();
        Users user_valid = Users.builder()
                .id(779L)
                .username("JHaris12")
                .userOriginalName("JHarissd")
                .userSoname("Bronson")
                .userPassword("QwErTy132!z")
                .userEmail("monim@gmail.com")
                .userRole(Role.WORKER)
                .userWorks(Collections.emptyList())
                .userStatus(StatusUser.DELETED)
                .build();

        Mockito.when(userRepository.getUserById(779L)).thenReturn(user_valid);
        Mockito.when(workRepository.getWorkById(777L)).thenReturn(work_by_id);

        Users user_by_id = userService.getById(779L);
        Work work_removed = workRepository.getWorkById(777L);
        WorkNotFound workNotFound = assertThrows(
                WorkNotFound.class,
                () -> userService.removeWorkFromWorker(work_removed, user_by_id)
        );

        Mockito.verify(userRepository, Mockito.times(0)).save(user_valid);
        Mockito.verify(userRepository, Mockito.times(1)).getUserById(779L);
        assertEquals("Work with id 777 doesn't have in worker with id 779",workNotFound.getMessage());

    }
}