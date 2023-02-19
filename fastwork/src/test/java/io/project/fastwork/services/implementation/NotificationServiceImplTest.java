package io.project.fastwork.services.implementation;

import io.project.fastwork.domains.Notification;
import io.project.fastwork.domains.StatusNotification;
import io.project.fastwork.services.api.NotificationServiceApi;
import io.project.fastwork.services.exception.LocationWithInvalidArgumentsException;
import io.project.fastwork.services.exception.NotificationInvalidParameterException;
import io.project.fastwork.services.exception.NotificationNotFound;
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

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Testcontainers
@Sql(value = "classpath:/sql/initDataBefore.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = "classpath:/sql/clearDataAfter.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class NotificationServiceImplTest {

    @Qualifier("notificationServiceImpl")
    @Autowired
    private NotificationServiceApi notificationService;
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
    void SaveNotification_WithValidNotification_ReturnTrue() throws NotificationInvalidParameterException {
        Notification notification_valid = Notification.builder()
                .nameTopicNotification("Test notification")
                .messageNotification("Test notification")
                .dateSendNotification(Timestamp.valueOf(LocalDateTime.now()))
                .statusNotification(StatusNotification.OTHER)
                .build();
        Notification notification_result_save = notificationService.saveNotification(notification_valid);
        assertNotNull(notification_result_save);

    }

    @Test
    void SaveNotification_WithInvalidNameNotification_ThrowException() throws NotificationInvalidParameterException {
        Notification notification_invalid = Notification.builder()
                .nameTopicNotification("")
                .messageNotification("Test notification")
                .dateSendNotification(Timestamp.valueOf(LocalDateTime.now()))
                .statusNotification(StatusNotification.OTHER)
                .build();

        NotificationInvalidParameterException notificationInvalidParameterException = assertThrows(
                NotificationInvalidParameterException.class,
                () -> notificationService.saveNotification(notification_invalid)
        );

        assertTrue(notificationInvalidParameterException.getMessage().contentEquals("Invalid parameter notification, try yet"));
    }

    @Test
    void SaveNotification_WithInvalidMessageNotification_ThrowException() throws NotificationInvalidParameterException {
        Notification notification_invalid = Notification.builder()
                .nameTopicNotification("Test")
                .messageNotification("")
                .dateSendNotification(Timestamp.valueOf(LocalDateTime.now()))
                .statusNotification(StatusNotification.OTHER)
                .build();

        NotificationInvalidParameterException notificationInvalidParameterException = assertThrows(
                NotificationInvalidParameterException.class,
                () -> notificationService.saveNotification(notification_invalid)
        );

        assertTrue(notificationInvalidParameterException.getMessage().contentEquals("Invalid parameter notification, try yet"));
    }

    @Test
    void FindAllNotification_ReturnTrue() {
        List<Notification> notificationList = notificationService.findAll();
        assertTrue(notificationList.size() > 1);
    }

    @Test
    void FindAllByStatus_WithStatusOther_ReturnTrue() {
        List<Notification> notificationList = notificationService.findByStatus(StatusNotification.OTHER);
        assertEquals(2, notificationList.size());
    }

    @Test
    void DeleteNotification_WithExsitsNotification_ReturnTrue() throws NotificationNotFound {
        Notification notification_delete = Notification.builder().id(778L).build();
        notificationService.deleteNotification(notification_delete);
        List<Notification> notificationList = notificationService.findByStatus(StatusNotification.OTHER);
        assertEquals(1, notificationList.size());
    }

    @Test
    void DeleteNotification_WithNonExsitsNotification_ThrowException() throws NotificationNotFound {
        Notification notification_delete = Notification.builder().id(7799L).build();
        NotificationNotFound notificationNotFound = assertThrows(
                NotificationNotFound.class,
                () -> notificationService.deleteNotification(notification_delete)
        );

        assertTrue(notificationNotFound.getMessage().contentEquals("Notification with id 7799 not found"));
    }
}