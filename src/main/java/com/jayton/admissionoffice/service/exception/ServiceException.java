package com.jayton.admissionoffice.service.exception;

import com.jayton.admissionoffice.util.exception.ApplicationException;

public class ServiceException extends ApplicationException {
    public ServiceException() {
    }

    public ServiceException(Throwable cause) {
        super(cause);
    }

    public ServiceException(String message) {
        super(message);
    }

    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}