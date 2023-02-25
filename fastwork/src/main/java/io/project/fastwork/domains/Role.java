package io.project.fastwork.domains;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    WORKER,HIRER,ADMIN,MODERATOR;

    @Override
    public String getAuthority() {
        return name();
    }
}
