package io.project.fastwork.domains;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;

@Data
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class WorkApplication {
    @Id
    @GeneratedValue
    private Long id;
    @OneToOne(fetch = FetchType.LAZY,cascade = CascadeType.REMOVE,orphanRemoval = true)
    private Users worker;
    @OneToOne(fetch = FetchType.LAZY,cascade = CascadeType.REMOVE,orphanRemoval = true)
    private Work work;
    private Timestamp dateApplicaton;
    @Enumerated(EnumType.STRING)
    private StatusWorkApplication statusWorkApplication;
}
