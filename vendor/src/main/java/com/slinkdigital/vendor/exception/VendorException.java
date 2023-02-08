package com.slinkdigital.vendor.exception;

/**
 *
 * @author TEGA
 */
public class VendorException extends RuntimeException{

    public VendorException(String message) {
        super(message);
    }

    public VendorException(String message, Throwable cause) {
        super(message, cause);
    }    
}
