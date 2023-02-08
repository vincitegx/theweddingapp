package com.slinkdigital.wedding.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 *
 * @author TEGA
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class WeddingException extends RuntimeException{

    public WeddingException(String message) {
        super(message);
    }

    public WeddingException(String message, Throwable cause) {
        super(message, cause);
    }
    
}
