package com.slinkdigital.mail.exception;

import com.slinkdigital.mail.dto.ApiResponse;
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

    @ExceptionHandler(value = {RestClientException.class, RuntimeException.class, MailServiceException.class, MethodArgumentNotValidException.class})
    public ResponseEntity<ApiResponse> handleApiRequestException(MailServiceException e) {
        return ResponseEntity.badRequest().body(
                ApiResponse.builder()
                        .timeStamp(LocalDateTime.now())
                        .message(e.getMessage())
                        .status(HttpStatus.BAD_REQUEST)
                        .build());
    }
}
