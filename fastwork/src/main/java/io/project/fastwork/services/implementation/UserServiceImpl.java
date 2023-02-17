package io.project.fastwork.services.implementation;

import io.project.fastwork.domains.StatusUser;
import io.project.fastwork.domains.Users;
import io.project.fastwork.domains.Work;
import io.project.fastwork.repositories.UserRepository;
import io.project.fastwork.repositories.WorkRepository;
import io.project.fastwork.services.api.UserServiceApi;
import io.project.fastwork.services.exception.*;
import io.project.fastwork.services.util.UserValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;


@RequiredArgsConstructor
@Slf4j
@Service
public class UserServiceImpl implements UserServiceApi {

    private final UserRepository userRepository;
    private final WorkRepository workRepository;

    @Override
    public Users saveUser(Users savedUser) throws UserAlreadyExisted, UserInvalidDataParemeter {
        Users user_is_exists = userRepository.findByUserName(savedUser.getUserLogin());
        if (user_is_exists != null) {
            log.error("User with username {} already exists in {}", savedUser.getUserLogin(), new Date());
            throw new UserAlreadyExisted(String.format("User with username - %s already existed. Try yet!",user_is_exists.getUserLogin()));
        }
        if (UserValidator.UserValidDataValues(savedUser)) {
            log.info("Save new user with username {} in {}", savedUser.getUserLogin(), new Date());
            savedUser.setUserStatus(StatusUser.ACTIVE);
            savedUser.setUserDateCreate(Timestamp.valueOf(LocalDateTime.now()));
            return userRepository.save(savedUser);
        } else {
            log.error("Invalid user data parameter throw exception in {}", new Date());
            throw new UserInvalidDataParemeter("Invalid user data parameter, check parameters");
        }
    }

    @Override
    public Users updateUser(Users updatedUser) throws UserAlreadyExisted, UserInvalidDataParemeter {
        if (UserValidator.UserValidDataValues(updatedUser)) {
            log.info("Update user with id {} in {}", updatedUser.getId(), new Date());
            updatedUser.setUserDateCreate(Timestamp.valueOf(LocalDateTime.now()));
            return userRepository.save(updatedUser);
        } else {
            log.error("Invalid user data parameter throw exception in {}", new Date());
            throw new UserInvalidDataParemeter("Invalid user data parameter, check parameters");
        }
    }

    @Override
    public Users deleteUser(Users deletedUser) throws UserNotFound {
        Users user_is_exist = userRepository.getUserById(deletedUser.getId());
        if (user_is_exist != null) {
            log.warn("Delete user with id {} in {}", deletedUser.getId(), new Date());
            deletedUser.setUserStatus(StatusUser.DELETED);
            userRepository.save(deletedUser);
            return deletedUser;
        } else {
            log.error("User with id {} not found throw exception in {}", deletedUser.getId(), new Date());
            throw new UserNotFound(String.format("User with id %s not found", deletedUser.getId()));
        }
    }

    @Override
    public Users blockedUser(Users blockedUser) throws UserNotFound {
        Users user_is_exist = userRepository.getUserById(blockedUser.getId());
        if (user_is_exist != null) {
            log.warn("Delete user with id {} in {}", blockedUser.getId(), new Date());
            blockedUser.setUserStatus(StatusUser.BLOCKED);
            userRepository.save(blockedUser);
            return blockedUser;
        } else {
            log.error("User with id {} not found throw exception in {}", blockedUser.getId(), new Date());
            throw new UserNotFound(String.format("User with id %s not found", blockedUser.getId()));
        }
    }

    @Override
    public List<Users> findAllUsersByStatus(StatusUser statusUser) {
        log.info("Get all active user in {}", new Date());
        return userRepository.findAll().stream().filter(o1 -> o1.getUserStatus().equals(statusUser)).toList();
    }

    @Override
    public Users findByUsername(String username) throws UserInvalidDataParemeter, UserNotFound {
        if (username.equals("")) {
            log.info("Username for search equals empty string, throw exception in {}", new Date());
            throw new UserInvalidDataParemeter("Username for search incorrect, try yet");
        }
        Users user_by_username = userRepository.findByUserName(username);
        if (user_by_username != null) {
            log.info("Get user by username - {} in {}",user_by_username,new Date());
            return user_by_username;
        } else {
            log.warn("User with username - {} not found, throw exception in {}",username,new Date());
            throw new UserNotFound(String.format("User with username - %s not found", username));
        }
    }

    @Override
    public Users findByEmail(String email) throws UserInvalidDataParemeter, UserNotFound {
        if (email.equals("")) {
            log.info("Email for search equals empty string, throw exception in {}", new Date());
            throw new UserInvalidDataParemeter("Email for search incorrect, try yet");
        }
        Users user_by_email = userRepository.findbyEmail(email);
        if (user_by_email != null) {
            log.info("Get user by email - {} in {}",user_by_email,new Date());
            return user_by_email;
        } else {
            log.warn("User with email - {} not found, throw exception in {}",email,new Date());
            throw new UserNotFound(String.format("User with email - %s not found", email));
        }
    }

    @Override
    public Users getById(Long idUser) throws UserNotFound {
        Users user_by_id = userRepository.getUserById(idUser);
        if(user_by_id!=null){
            log.info("Get user by id - {} in {}",idUser,new Date());
            return user_by_id;
        }else{
            log.error("User with id {} not found, throw exception in {}",idUser,new Date());
            throw new UserNotFound(String.format("User with id %s not found",idUser));
        }
    }


    @Transactional
    @Override
    public Work addWorkToWorker(Work added_work, Users worker) throws WorkAlreadyAdded {
        Work work_is_existed = worker.getUserWorks().stream().filter(o1->o1.getWorkName()
                .equals(added_work.getWorkName())).findFirst().orElse(null);
        if(work_is_existed!=null){
            log.info("Work with id {} already added to worker with id {} in {}",added_work.getId(),worker.getId(),new Date());
            throw new WorkAlreadyAdded(String.format("Work with id %s already added to worker with id %s",added_work.getId(),worker.getId()));
        }
        log.info("Add new work with id {} to worker with id {} in {}",added_work.getId(),worker.getId(),new Date());
        worker.getUserWorks().add(added_work);
        userRepository.save(worker);
        return added_work;
    }

    @Transactional
    @Override
    public Work removeWorkFromWorker(Work removed_work,Users worker) throws WorkNotFound {
        Work work_is_existed = worker.getUserWorks().stream().filter(o1->o1.getWorkName()
                .equals(removed_work.getWorkName())).findFirst().orElse(null);
        if(work_is_existed==null){
            log.info("Work with id {} doesn't have in worker with id {} in {}",removed_work.getId(),worker.getId(),new Date());
            throw new WorkNotFound(String.format("Work with id %s doesn't have in worker with id %s",removed_work.getId(),worker.getId()));
        }
        log.info("Remove work with id {} to worker with id {} in {}",removed_work.getId(),worker.getId(),new Date());
        worker.removeWork(removed_work);
        userRepository.save(worker);
        return removed_work;
    }
}
