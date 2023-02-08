package com.slinkdigital.mail.exception;

import org.springframework.mail.MailException;

/**
 *
 * @author TEGA
 */
public class MailServiceException extends RuntimeException{

    public MailServiceException(String message, MailException e) {
        super(message,e);
    }   

    public MailServiceException(String message) {
        super(message);
    }       
}
