package com.slinkdigital.fileservice.exception;

import com.slinkdigital.fileservice.dto.ApiResponse;
import java.time.LocalDateTime;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.RestClientException;

/**
 *
 * @author TEGA
 */
@ControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(value = {FileServiceException.class, RestClientException.class, RuntimeException.class, MethodArgumentNotValidException.class})
    public ResponseEntity<ApiResponse> handleApiRequestException(FileServiceException e) {
        return ResponseEntity.badRequest().body(
                ApiResponse.builder()
                        .timeStamp(LocalDateTime.now())
                        .message(e.getMessage())
                        .status(HttpStatus.BAD_REQUEST)
                        .build());
    }
}
