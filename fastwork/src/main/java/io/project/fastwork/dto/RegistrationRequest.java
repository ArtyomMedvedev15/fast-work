package io.project.fastwork.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegistrationRequest {
    private String userLogin;
    private String userName;
    private String userSoname;
    private String userEmail;
    private String userPassword;
}
