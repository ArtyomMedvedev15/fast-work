package io.project.fastwork.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ErrorMessageResponse{
    private int errro_statusCode;
    private Date timestamp;
    private String error_message;
    private String error_description;
}
