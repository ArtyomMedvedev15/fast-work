package io.project.fastwork.services.api;

import io.project.fastwork.domains.Users;
import io.project.fastwork.domains.Work;
import io.project.fastwork.services.exception.UserAlreadyExisted;
import io.project.fastwork.services.exception.UserInvalidDataParemeter;
import org.springframework.stereotype.Service;

import java.util.List;

public interface UserServiceApi {
    Users saveUser(Users savedUser) throws UserAlreadyExisted, UserInvalidDataParemeter;
    Users updateUser(Users updatedUser);
    Users deleteUser(Users deletedUser);
    Users blockedUser(Users blockedUser);
    List<Users>findAll();
    Users findByUsername(String username);
    Users findByEmail(String email);
    Users getById(Long idUser);
    Work addWorkToWorker(Work added_work);
    Work removeWorkFromWorker(Work added_work);


}
