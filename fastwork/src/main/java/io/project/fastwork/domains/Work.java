package io.project.fastwork.domains;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.util.List;

@Data
@EqualsAndHashCode
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Work {
    @Id
    @GeneratedValue
    private Long id;
    private String workName;
    private String workDescribe;
    private Integer workCountPerson;
    private Float workPrice;
    @OneToOne(fetch = FetchType.LAZY)
    private TypeWork workType;
    private Timestamp workDateCreate;
    @Enumerated(EnumType.STRING)
    private StatusWork workStatus;
    @OneToOne(fetch = FetchType.LAZY)
    private Users workHirer;
}
