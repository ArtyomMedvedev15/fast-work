package io.project.fastwork.services.util;

import io.project.fastwork.domains.Users;
import io.project.fastwork.services.exception.UserInvalidDataParemeter;

import java.util.regex.Pattern;

public class UserValidator{
    public static boolean UserValidDataValues(Users user_valid) throws UserInvalidDataParemeter{
        if((Pattern.matches("^[A-Za-z][A-Za-z0-9_]{7,29}$",user_valid.getUsername())&&
                Pattern.matches("^[a-zA-Z]{5,40}+$",user_valid.getUserOriginalName())&&
                Pattern.matches("^[a-zA-Z]{5,40}+$", user_valid.getUserSoname())&&
                Pattern.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,30}$",user_valid.getUserPassword())&&
                Pattern.matches("^\\w+@[a-zA-Z_]+?\\.[a-zA-Z]{2,3}$",user_valid.getUserEmail()))){
            return true;
        }else{
            throw new UserInvalidDataParemeter("Invalid user data parameter, check parameters");
        }
    }
}
