package com.jayton.admissionoffice.dao.exception;

import com.jayton.admissionoffice.util.exception.ApplicationException;

public class DAOException extends ApplicationException {

    public DAOException() {
    }

    public DAOException(String message) {
        super(message);
    }

    public DAOException(String message, Throwable cause) {
        super(message, cause);
    }
}
