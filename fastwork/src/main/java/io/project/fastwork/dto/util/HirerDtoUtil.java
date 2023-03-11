package io.project.fastwork.dto.util;

import io.project.fastwork.domains.Users;
import io.project.fastwork.dto.response.HirerResponse;

public class HirerDtoUtil {
    public static HirerResponse getHirerResponse(Users hirer){
        return HirerResponse.builder()
                .id(hirer.getId())
                .workerName(hirer.getUserOriginalName())
                .workerSoname(hirer.getUserSoname())
                .workerEmail(hirer.getUserEmail())
                .build();
    }
}
