package com.jayton.admissionoffice.service.exception;

/**
 * Created by Jayton on 28.11.2016.
 */
public class ServiceException extends Exception {
    public ServiceException() {
    }

    public ServiceException(String message) {
        super(message);
    }

    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
