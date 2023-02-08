package com.slinkdigital.user.exception;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.slinkdigital.user.dto.ApiResponse;
import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.NoResultException;
import javax.validation.ConstraintViolationException;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.METHOD_NOT_ALLOWED;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.RestClientException;
import org.springframework.web.servlet.NoHandlerFoundException;

/**
 *
 * @author TEGA
 */
@RestControllerAdvice
public class ApiExceptionHandler {

    private static final String ACCOUNT_LOCKED = "Your account has been locked. Please contact administration";
    private static final String METHOD_IS_NOT_ALLOWED = "This request method is not allowed on this endpoint. Please send a '%s' request";
    private static final String INTERNAL_SERVER_ERROR_MSG = "An error occurred while processing the request";
    private static final String INCORRECT_CREDENTIALS = "Username / password incorrect. Please try again";
    private static final String ACCOUNT_DISABLED = "Your account has been disabled. If this is an error, please contact administration";
    private static final String ERROR_PROCESSING_FILE = "Error occurred while processing file";
    private static final String NOT_ENOUGH_PERMISSION = "You do not have enough permission";
    public static final String ERROR_PATH = "/error";
    
    @ExceptionHandler(value = RuntimeException.class)
    public ResponseEntity<ApiResponse> handleApiRequestException(Exception e) {
        return ResponseEntity.badRequest().body(
                ApiResponse.builder()
                        .timeStamp(LocalDateTime.now())
                        .message(e.getMessage())
                        .status(HttpStatus.BAD_REQUEST)
                        .build());
    }
    
    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<ApiResponse> accountDisabledException() {
        return createApiResponse(BAD_REQUEST, ACCOUNT_DISABLED);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiResponse> badCredentialsException() {
        return createApiResponse(BAD_REQUEST, INCORRECT_CREDENTIALS);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse> accessDeniedException() {
        return createApiResponse(FORBIDDEN, NOT_ENOUGH_PERMISSION);
    }

    @ExceptionHandler(LockedException.class)
    public ResponseEntity<ApiResponse> lockedException() {
        return createApiResponse(UNAUTHORIZED, ACCOUNT_LOCKED);
    }

    @ExceptionHandler(TokenExpiredException.class)
    public ResponseEntity<ApiResponse> tokenExpiredException(TokenExpiredException exception) {
        return createApiResponse(UNAUTHORIZED, exception.getMessage());
    }

    @ExceptionHandler(UserException.class)
    public ResponseEntity<ApiResponse> userException(UserException exception) {
        return createApiResponse(BAD_REQUEST, exception.getMessage());
    }
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse> methodArgumentNotValidException(MethodArgumentNotValidException exception) {
        return createApiResponse(HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage());
    }
    
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse> constraintException(ConstraintViolationException exception) {
        return createApiResponse(BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ApiResponse> usernameNotFoundException(UsernameNotFoundException exception) {
        return createApiResponse(BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(RestClientException.class)
    public ResponseEntity<ApiResponse> restClientException(RestClientException exception) {
        return createApiResponse(BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ApiResponse> noHandlerFoundException(NoHandlerFoundException e) {
        return createApiResponse(BAD_REQUEST, "There is no mapping for this URL");
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiResponse> methodNotSupportedException(HttpRequestMethodNotSupportedException exception) {
        HttpMethod supportedMethod = Objects.requireNonNull(exception.getSupportedHttpMethods()).iterator().next();
        return createApiResponse(METHOD_NOT_ALLOWED, String.format(METHOD_IS_NOT_ALLOWED, supportedMethod));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse> internalServerErrorException(Exception exception) {
        return createApiResponse(INTERNAL_SERVER_ERROR, INTERNAL_SERVER_ERROR_MSG);
    }

    @ExceptionHandler(NoResultException.class)
    public ResponseEntity<ApiResponse> notFoundException(NoResultException exception) {
        return createApiResponse(NOT_FOUND, exception.getMessage());
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<ApiResponse> iOException(IOException exception) {
        return createApiResponse(INTERNAL_SERVER_ERROR, ERROR_PROCESSING_FILE);
    }

    private ResponseEntity<ApiResponse> createApiResponse(HttpStatus httpStatus, String message) {
        return new ResponseEntity<>(
                new ApiResponse(LocalDateTime.now(),httpStatus,httpStatus.getReasonPhrase().toUpperCase(), message,null),
                httpStatus);
    }

    @RequestMapping(ERROR_PATH)
    public ResponseEntity<ApiResponse> notFound404() {
        return createApiResponse(NOT_FOUND, "There is no mapping for this URL");
    }

    public String getErrorPath() {
        return ERROR_PATH;
    }
}
