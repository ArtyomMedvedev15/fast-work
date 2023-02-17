package io.project.fastwork.services.implementation;

import io.project.fastwork.domains.Notification;
import io.project.fastwork.domains.StatusNotification;
import io.project.fastwork.repositories.NotificationRepository;
import io.project.fastwork.services.api.NotificationServiceApi;
import io.project.fastwork.services.exception.NotificationInvalidParameterException;
import io.project.fastwork.services.exception.NotificationNotFound;
import io.project.fastwork.services.util.NotificationValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
@Service
public class NotificationServiceImpl implements NotificationServiceApi {

    private final NotificationRepository notificationRepository;

    @Override
    public Notification saveNotification(Notification savedNotification) throws NotificationInvalidParameterException {
        if(NotificationValidator.NotificationValidDataValues(savedNotification)){
            log.info("Save new notification with name topic {} in {}",savedNotification.getNameTopicNotification(),new Date());
            savedNotification.setDateSendNotification(Timestamp.valueOf(LocalDateTime.now()));
            return notificationRepository.save(savedNotification);
        }else{
            log.error("Invalid notification parameters throw exception in {}",new Date());
            throw new NotificationInvalidParameterException("Invalid parameter notification, try yet");
        }
     }

    @Override
    public List<Notification> findAll() {
        log.info("Get all notification in {}", new Date());
        return notificationRepository.findAll();
    }

    @Override
    public List<Notification> findByStatus(StatusNotification statusNotification) {
        log.info("Get all notification with status {} in {}",statusNotification.name(),new Date());
        return notificationRepository.findAll().stream().filter(o1->o1.getStatusNotification()
                .equals(statusNotification)).collect(Collectors.toList());
    }

    @Override
    public Notification deleteNotification(Notification notification_deleted) throws NotificationNotFound {
        Notification check_notification_exists = notificationRepository.getReferenceById(notification_deleted.getId());
        if(check_notification_exists!=null){
            log.warn("Delete notification with id {} in {}",notification_deleted.getId(),new Date());
            notificationRepository.delete(notification_deleted);
            return notification_deleted;
        }else{
            log.error("Notification with id {} not found, throw exception",notification_deleted.getId());
            throw new NotificationNotFound(String.format("Notification with id %s not found",notification_deleted.getId()));
        }
     }
}
