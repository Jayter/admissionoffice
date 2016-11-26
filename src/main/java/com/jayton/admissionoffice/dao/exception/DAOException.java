package com.jayton.admissionoffice.dao.exception;

/**
 * Created by Jayton on 25.11.2016.
 */
public class DAOException extends Exception {
    public DAOException() {
    }

    public DAOException(String message) {
        super(message);
    }

    public DAOException(String message, Throwable cause) {
        super(message, cause);
    }
}
