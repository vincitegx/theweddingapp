package com.slinkdigital.apigateway.exception;

import com.slinkdigital.apigateway.dto.ErrorResponse;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
public class GatewayExceptionHandler {

    @ExceptionHandler(value = {GatewayException.class})
    public ResponseEntity<ErrorResponse> handleGatewayException(GatewayException e) {
        return createApiResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getLocalizedMessage());
    }
    
    @ExceptionHandler(value = NullPointerException.class)
    public ResponseEntity<ErrorResponse> handleNullPointerException(NullPointerException e) {
        return createApiResponse(HttpStatus.BAD_REQUEST, e.getLocalizedMessage());
    }

    private ResponseEntity<ErrorResponse> createApiResponse(HttpStatus httpStatus, String message) {
        List<String> details = new ArrayList<>();
        details.add(message);
        return new ResponseEntity<>(
                new ErrorResponse(new Date(), httpStatus.value(), httpStatus.getReasonPhrase().toUpperCase(), details, null),
                httpStatus);
    }
}
