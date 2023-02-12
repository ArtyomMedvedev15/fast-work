package io.project.fastwork.services.util;

import io.project.fastwork.domains.TypeWork;
import io.project.fastwork.services.exception.TypeWorkInvalidParameterException;

import java.util.regex.Pattern;

public class TypeWorkValidator {
    public static boolean TypeWorkValidDataValues(TypeWork typeWork) throws TypeWorkInvalidParameterException {
        if(Pattern.matches("^[a-zA-Z]{4,30}+$",typeWork.getTypeWorkName()) &&
                Pattern.matches("^[a-zA-Z]{15,256}+$",typeWork.getTypeWorkDescribe())){
           return true;
        }else{
            throw new TypeWorkInvalidParameterException("Check string parameters, something was wrong!");
        }
    }
}
