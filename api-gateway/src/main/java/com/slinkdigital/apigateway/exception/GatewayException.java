package com.slinkdigital.apigateway.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 *
 * @author TEGA
 */
@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class GatewayException extends RuntimeException {

    public GatewayException(String message) {
        super(message);
    }
}
