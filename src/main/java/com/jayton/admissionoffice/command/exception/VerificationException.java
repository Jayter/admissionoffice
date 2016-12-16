package com.jayton.admissionoffice.command.exception;

public class VerificationException extends Exception {

    public VerificationException(String message) {
        super(message);
    }

    public VerificationException(String message, Throwable cause) {
        super(message, cause);
    }
}