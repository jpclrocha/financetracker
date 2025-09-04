package com.jope.financetracker.dto.api_error;

import lombok.*;
import org.springframework.http.HttpStatus;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ApiErrorDTO {
    private Integer status;
    private String message;
    private String path;
    private Instant timestamp;

    public ApiErrorDTO(HttpStatus status, String message, String path,
                    Instant timestamp){
        setStatus(status.value());
        setMessage(message);
        setPath(path);
        setTimestamp(timestamp);
    }
}
