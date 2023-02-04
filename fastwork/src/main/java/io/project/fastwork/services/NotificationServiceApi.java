package io.project.fastwork.services;

import io.project.fastwork.domains.Notification;
import io.project.fastwork.domains.StatusNotification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface NotificationServiceApi {
    Notification saveNotification(Notification savedNotification);
    List<Notification>findAll();
    List<Notification>findByStatus(StatusNotification statusNotification);
}
