package io.project.fastwork.services.util;

import io.project.fastwork.domains.Work;
import io.project.fastwork.services.exception.WorkInvalidDataValues;

import java.util.regex.Pattern;

public class WorkValidator {
    public static boolean WorkValidDataValues(Work work_valid) throws WorkInvalidDataValues {
        if ((Pattern.matches("(([a-zA-Z]+\\s)*[a-zA-Z]){5,80}", work_valid.getWorkName())) &&
                (Pattern.matches("(([a-zA-Z0-9]+\\s)*[a-zA-Z0-9]){10,512}", work_valid.getWorkDescribe())) &&
                        (work_valid.getWorkPrice()>0) &&
                        (work_valid.getWorkCountPerson()>=1 && work_valid.getWorkCountPerson()<=10)){
            return true;
        }else{
            throw new WorkInvalidDataValues("Invalid data values for work, check string parameters");
        }
    }
}
