package io.project.fastwork.services.util;

import io.project.fastwork.domains.Notification;
import io.project.fastwork.services.exception.NotificationInvalidParameterException;

import java.util.regex.Pattern;

public class NotificationValidator {

    public static boolean NotificationValidDataValues(Notification notification_valid) throws NotificationInvalidParameterException {
        if ((Pattern.matches("^[a-zA-Z]{5,80}+$", notification_valid.getNameTopicNotification())) &&
                (Pattern.matches("^[a-zA-Z]{15,256}+$", notification_valid.getMessageNotification()))){
            return true;
        }else{
            throw new NotificationInvalidParameterException("Check string parameters, something was wrong!");
        }

    }
}
