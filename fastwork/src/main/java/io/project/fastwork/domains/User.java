package io.project.fastwork.domains;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.sql.Timestamp;
import java.util.Set;

@Data
@EqualsAndHashCode
@ToString
@Builder
public class User {
    private Long id;
    private String userName;
    private String userSoname;
    private String userEmail;
    private String userPassword;
    private Role userRole;
    private StatusUser userStatus;
    private Timestamp userDateCreate;
    private Set<Work>userWorks;
}
