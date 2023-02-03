package io.project.fastwork.domains;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@Builder
@Entity
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String userName;
    private String userSoname;
    private String userEmail;
    private String userPassword;
    private Role userRole;
    private StatusUser userStatus;
    private Timestamp userDateCreate;
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_works",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "work_id"))
    private Set<Work>userWorks;
}
