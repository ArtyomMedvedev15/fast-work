package io.project.fastwork.domains;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@Builder
@Entity
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String nameTopicNotification;
    private String messageNotification;
    @Enumerated(EnumType.STRING)
    private StatusNotification statusNotification;
    private Timestamp dateSendNotification;
}
