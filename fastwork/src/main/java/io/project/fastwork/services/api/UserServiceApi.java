package io.project.fastwork.services.api;

import io.project.fastwork.domains.Users;
import io.project.fastwork.domains.Work;
import io.project.fastwork.services.exception.*;
import org.springframework.stereotype.Service;

import java.util.List;

public interface UserServiceApi {
    Users saveUser(Users savedUser) throws UserAlreadyExisted, UserInvalidDataParemeter;
    Users updateUser(Users updatedUser) throws UserAlreadyExisted, UserInvalidDataParemeter;
    Users deleteUser(Users deletedUser) throws UserNotFound;
    Users blockedUser(Users blockedUser) throws UserNotFound;
    List<Users>findAll();
    Users findByUsername(String username) throws UserInvalidDataParemeter, UserNotFound;
    Users findByEmail(String email) throws UserInvalidDataParemeter, UserNotFound;
    Users getById(Long idUser) throws UserNotFound;
    Work addWorkToWorker(Work added_work,Users worker) throws WorkAlreadyAdded;
    Work removeWorkFromWorker(Work removed_work,Users worker) throws WorkNotFound;


}
