package com.jayton.admissionoffice.command.exception;

import com.jayton.admissionoffice.util.exception.ApplicationException;

public class VerificationException extends ApplicationException {

    public VerificationException(String message) {
        super(message);
    }

    public VerificationException(String message, Throwable cause) {
        super(message, cause);
    }
}