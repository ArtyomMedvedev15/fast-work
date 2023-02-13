package io.project.fastwork.domains;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

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
    private String userLogin;
    private String userName;
    private String userSoname;
    private String userEmail;
    private String userPassword;
    @Enumerated(EnumType.STRING)
    private Role userRole;
    @Enumerated(EnumType.STRING)
    private StatusUser userStatus;
    private Timestamp userDateCreate;
    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JoinTable(name = "users_works")
    private List<Work> userWorks =new ArrayList<>();

    public void addWork(Work work){
        this.userWorks.add(work);
    }
    public void removeWork(Work work){
        this.userWorks.remove(work);
    }
}
