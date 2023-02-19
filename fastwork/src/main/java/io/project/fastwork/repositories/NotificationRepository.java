package io.project.fastwork.repositories;

import io.project.fastwork.domains.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends JpaRepository<Notification,Long> {
    @Query("select n from Notification n where n.id=:id_notification")
    Notification getNotificationById(@Param("id_notification")Long id_notification);

}
