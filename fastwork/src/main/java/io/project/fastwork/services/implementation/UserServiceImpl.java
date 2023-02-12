package io.project.fastwork.services.implementation;

import io.project.fastwork.domains.Users;
import io.project.fastwork.domains.Work;
import io.project.fastwork.repositories.UserRepository;
import io.project.fastwork.repositories.WorkRepository;
import io.project.fastwork.services.api.UserServiceApi;
import io.project.fastwork.services.exception.UserAlreadyExisted;
import io.project.fastwork.services.exception.UserInvalidDataParemeter;
import io.project.fastwork.services.util.UserValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;


@RequiredArgsConstructor
@Slf4j
@Service
public class UserServiceImpl implements UserServiceApi {

    private final UserRepository userRepository;
    private final WorkRepository workRepository;

    @Override
    public Users saveUser(Users savedUser) throws UserAlreadyExisted, UserInvalidDataParemeter {
        Users user_is_exists = userRepository.findByUserName(savedUser.getUserName());
        if(user_is_exists!=null){
            throw new UserAlreadyExisted("User with username - %s already existed. Try yet!");
        }
        if(UserValidator.UserValidDataValues(savedUser)){

        }
        return null;
    }

    @Override
    public Users updateUser(Users updatedUser) {
        return null;
    }

    @Override
    public Users deleteUser(Users deletedUser) {
        return null;
    }

    @Override
    public Users blockedUser(Users blockedUser) {
        return null;
    }

    @Override
    public List<Users> findAll() {
        return null;
    }

    @Override
    public Users findByUsername(String username) {
        return null;
    }

    @Override
    public Users findByEmail(String email) {
        return null;
    }

    @Override
    public Users getById(Long idUser) {
        return null;
    }

    @Override
    public Work addWorkToWorker(Work added_work) {
        return null;
    }

    @Override
    public Work removeWorkFromWorker(Work added_work) {
        return null;
    }
}
