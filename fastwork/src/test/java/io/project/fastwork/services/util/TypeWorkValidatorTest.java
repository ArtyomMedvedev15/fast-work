package io.project.fastwork.services.util;

import io.project.fastwork.domains.TypeWork;
import io.project.fastwork.services.exception.LocationWithInvalidArgumentsException;
import io.project.fastwork.services.exception.TypeWorkInvalidParameterException;
import org.junit.jupiter.api.Test;

import java.util.StringJoiner;

import static org.junit.jupiter.api.Assertions.*;

class TypeWorkValidatorTest {

    @Test
    void TypeWorkValidator_WithValidParameter_ReturnTrue() throws TypeWorkInvalidParameterException {
        TypeWork type_work_valid = TypeWork.builder()
                .id(777L)
                .typeWorkName("Type")
                .typeWorkDescribe("TypeWorkTypeWorkType")
                .build();

        boolean type_work_validator_result = TypeWorkValidator.TypeWorkValidator(type_work_valid);

        assertTrue(type_work_validator_result);
    }

    @Test
    void TypeWorkValidator_WithInvalidNameLessLength4_ReturnTrue() throws TypeWorkInvalidParameterException {
        TypeWork type_work_valid = TypeWork.builder()
                .id(777L)
                .typeWorkName("Typ")
                .typeWorkDescribe("TypeWorkTypeWorkType")
                .build();

        TypeWorkInvalidParameterException typeWorkInvalidParameterException = assertThrows(
                TypeWorkInvalidParameterException.class,
                () -> TypeWorkValidator.TypeWorkValidator(type_work_valid)
        );

        assertTrue(typeWorkInvalidParameterException.getMessage().contentEquals("Check string parameters, something was wrong!"));
    }

    @Test
    void TypeWorkValidator_WithInvalidNameMoreThenLength40_ReturnTrue() throws TypeWorkInvalidParameterException {
        TypeWork type_work_valid = TypeWork.builder()
                .id(777L)
                .typeWorkName("TypeWorkTypeWorkTypeWorkTypeWorkType")
                .typeWorkDescribe("TypeWorkTypeWorkType")
                .build();

        TypeWorkInvalidParameterException typeWorkInvalidParameterException = assertThrows(
                TypeWorkInvalidParameterException.class,
                () -> TypeWorkValidator.TypeWorkValidator(type_work_valid)
        );

        assertTrue(typeWorkInvalidParameterException.getMessage().contentEquals("Check string parameters, something was wrong!"));
    }

    @Test
    void TypeWorkValidator_WithInvalidDescribeLessLength15_ReturnTrue() throws TypeWorkInvalidParameterException {
        TypeWork type_work_valid = TypeWork.builder()
                .id(777L)
                .typeWorkName("Type")
                .typeWorkDescribe("Typ")
                .build();

        TypeWorkInvalidParameterException typeWorkInvalidParameterException = assertThrows(
                TypeWorkInvalidParameterException.class,
                () -> TypeWorkValidator.TypeWorkValidator(type_work_valid)
        );

        assertTrue(typeWorkInvalidParameterException.getMessage().contentEquals("Check string parameters, something was wrong!"));
    }

    @Test
    void TypeWorkValidator_WithInvalidDescribeMoreThenLength256_ReturnTrue() throws TypeWorkInvalidParameterException {
        TypeWork type_work_valid = TypeWork.builder()
                .id(777L)
                .typeWorkName("Type")
                .typeWorkDescribe("TypeWorkTypeWorkTypeWorkTypeWorkTypeWorkTypeWorkTypeWork" +
                        "ypeWorkTypeWorkTypeWorkTypeWorkTypeWorkTypeWorkTy" +
                        "peWorkTypeWorkTypeWorkTypeW" +
                        "orkTypeWorkTypeWorkTypeWorkTypeWorkTypeWorkTypeWorkTypeWorkTypeWorkT" +
                        "ypeWorkTypeWorkTypeWorkTypeWorkTypeWorkTypeWorkTypeWorkWork")
                .build();

        TypeWorkInvalidParameterException typeWorkInvalidParameterException = assertThrows(
                TypeWorkInvalidParameterException.class,
                () -> TypeWorkValidator.TypeWorkValidator(type_work_valid)
        );

        assertTrue(typeWorkInvalidParameterException.getMessage().contentEquals("Check string parameters, something was wrong!"));
    }
}