package io.project.fastwork.services.api;

import io.project.fastwork.domains.Users;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserServiceApi {
    Users saveUser(Users savedUser);
    Users updateUser(Users updatedUser);
    Users deleteUser(Users deletedUser);
    Users blockedUser(Users blockedUser);
    List<Users>findAll();
    Users findByUsername(String username);
    Users findByEmail(String email);
    Users getById(Long idUser);

}