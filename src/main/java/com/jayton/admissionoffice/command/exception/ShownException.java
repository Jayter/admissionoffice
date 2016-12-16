package com.jayton.admissionoffice.command.exception;

import com.jayton.admissionoffice.service.exception.ServiceException;

public class ShownException extends ServiceException {
    public ShownException(String message) {
        super(message);
    }
}