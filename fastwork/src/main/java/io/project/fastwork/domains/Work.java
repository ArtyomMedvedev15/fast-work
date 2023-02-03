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
    @OneToOne
    private Location workLocation;
    private Timestamp workDateCreate;
    private StatusWork workStatus;
    @OneToOne(fetch = FetchType.EAGER)
    private Users workHirer;
    @ManyToMany(mappedBy = "userWorks",fetch = FetchType.LAZY)
    private List<Users>workWorkers;
}
