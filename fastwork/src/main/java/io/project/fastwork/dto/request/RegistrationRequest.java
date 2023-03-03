package io.project.fastwork.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegistrationRequest {
    private String userlogin;
    private String username;
    private String usersoname;
    private String useremail;
    private String userpassword;
    private String userrole;
}
