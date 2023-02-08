package com.slinkdigital.wedding.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import java.time.LocalDateTime;
import java.util.Map;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.http.HttpStatus;

/**
 *
 * @author TEGA
 */
@Data
@SuperBuilder
@JsonInclude(NON_NULL)
@NoArgsConstructor
public class ApiResponse {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    protected LocalDateTime timeStamp;
    protected HttpStatus status;
    protected String message;
    protected String developerMessage;
    protected Map<?,?> data;   
}
