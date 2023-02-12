package io.project.fastwork.services.util;

import io.project.fastwork.domains.Users;
import io.project.fastwork.services.exception.LocationWithInvalidArgumentsException;
import io.project.fastwork.services.exception.UserInvalidDataParemeter;
import io.project.fastwork.util.UserArgumentProviders;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class UserValidatorTest {

    @ParameterizedTest
    @ArgumentsSource(UserArgumentProviders.UsersArgumentsValidProvider.class)
    void UserValidatorTest_WithValidData_ReturnTrue(Users user_valid_test) throws UserInvalidDataParemeter {
         assertTrue(UserValidator.UserValidDataValues(user_valid_test));
    }

    @ParameterizedTest
    @ArgumentsSource(UserArgumentProviders.UsersArgumentsInvalidProvider.class)
    void UserValidatorTest_WithInvalidData_ThrowsException(Users user_invalid_test) throws UserInvalidDataParemeter {

        UserInvalidDataParemeter userInvalidDataParemeter = assertThrows(
                UserInvalidDataParemeter.class,
                () -> UserValidator.UserValidDataValues(user_invalid_test)
        );
        assertTrue(userInvalidDataParemeter.getMessage().contentEquals("Invalid user data parameter, check parameters"));
    }

}


