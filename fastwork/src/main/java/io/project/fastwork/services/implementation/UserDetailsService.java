package io.project.fastwork.services.implementation;

import io.project.fastwork.domains.Users;
import io.project.fastwork.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users user_from_db = userRepository.findByUserName(username);
        if (username == null) {
            log.error("User with username {} not found, throw exception in {}", username, new Date());
            throw new UsernameNotFoundException(String.format("User with username %s not found!", username));
        }
        return user_from_db;    }
}
