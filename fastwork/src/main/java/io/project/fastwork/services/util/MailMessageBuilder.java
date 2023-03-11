package io.project.fastwork.services.util;

import io.project.fastwork.domains.StatusWorkApplication;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

@Slf4j
public class MailMessageBuilder {
    public static String messageBuildWorker(String workerInfo, StatusWorkApplication statusWorkApplication) {
        if(statusWorkApplication.equals(StatusWorkApplication.APPROVE)) {
            log.info("Build message for send to {} with status {} in {}",workerInfo,statusWorkApplication,new Date());
            return String.format("Dear %s your request for work was %s, thank you for use our platform." +
                    " Have a nice day and good luck!", workerInfo, "approved");
        }else{
            log.info("Build message for send to {} with status {} in {}",workerInfo,statusWorkApplication,new Date());
            return String.format("Dear %s your request for work was %s, thank you for use our platform." +
                    " Have a nice day and good luck!", workerInfo, "rejected");
        }
    }

    public static String messageBuildHirer(String workerInfo, String hirerInfo) {
        log.info("Build message for send to {} for {} in {}",hirerInfo,workerInfo,new Date());
        return String.format("Dear %s, on your work was send work application from %s, check your applications list, thank you for use our platform." +
                " Have a nice day and good luck!",hirerInfo,workerInfo);

    }
}
