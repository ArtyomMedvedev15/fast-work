package io.project.fastwork.services.api;

import io.project.fastwork.domains.Notification;
import io.project.fastwork.domains.StatusNotification;
import io.project.fastwork.services.exception.NotificationInvalidParameterException;
import io.project.fastwork.services.exception.NotificationNotFound;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface NotificationServiceApi {
    Notification saveNotification(Notification savedNotification) throws NotificationInvalidParameterException;
    List<Notification>findAll();
    List<Notification>findByStatus(StatusNotification statusNotification);
    Notification deleteNotification(Notification notification) throws NotificationNotFound;

}
