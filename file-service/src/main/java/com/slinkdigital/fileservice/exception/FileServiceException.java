package com.slinkdigital.fileservice.exception;

/**
 *
 * @author TEGA
 */
public class FileServiceException extends RuntimeException{
 
    public FileServiceException(String message) {
        super(message);
    }

    public FileServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
