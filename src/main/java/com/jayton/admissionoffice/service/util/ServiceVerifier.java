package com.jayton.admissionoffice.service.util;

import com.jayton.admissionoffice.service.exception.ServiceException;
import com.jayton.admissionoffice.service.exception.ServiceVerificationException;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Created by Jayton on 30.11.2016.
 */
public class ServiceVerifier {

    public static final String INCORRECT_COEF = "Coefficient must be less than 1 and greater than 0, but was: %f.";
    public static final String INCORRECT_RESULT = "Exam result must be less than 200 and greater than 100, but was: %f.";
    public static final String INCORRECT_ID = "Id must be positive, but was: %d.";
    public static final String INCORRECT_PASSWORD = "Password must be longer than 5 symbols..";
    public static final String INCORRECT_EMAIL = "Incorrect email.";
    public static final String NULLABLE = "%s must not be null.";
    public static final String EMPTY = "%s must not be empty.";

    private ServiceVerifier(){}

    public static void verifyPassword(String password) throws ServiceVerificationException {
        if(Objects.isNull(password)) {
            throw new ServiceVerificationException(String.format(NULLABLE, "Password"));
        }
        if(password.length() < 6) {
            throw new ServiceVerificationException(INCORRECT_PASSWORD);
        }
    }

    public static void verifyEmail(String email) throws ServiceVerificationException {
        if(Objects.isNull(email)) {
            throw new ServiceVerificationException(String.format(NULLABLE, "Email"));
        }
        if(email.indexOf('@') == -1) {
            throw new ServiceVerificationException(INCORRECT_EMAIL);
        }
    }

    public static void verifyResult(BigDecimal result) throws ServiceException {
        if(Objects.isNull(result)) {
            throw new ServiceVerificationException(String.format(NULLABLE, "Exam result"));
        }
        if(result.compareTo(BigDecimal.valueOf(100)) <= 0 && result.compareTo(BigDecimal.valueOf(200)) > 0) {
            throw new ServiceVerificationException(String.format(INCORRECT_RESULT, result.doubleValue()));
        }
    }

    public static void verifyCoef(BigDecimal coef) throws ServiceVerificationException {
        if(Objects.isNull(coef)) {
            throw new ServiceVerificationException(String.format(NULLABLE, "Coefficient"));
        }
        if(coef.compareTo(BigDecimal.ZERO) <= 0 || coef.compareTo(BigDecimal.ONE) <= 0) {
            throw new ServiceVerificationException(String.format(INCORRECT_COEF, coef.doubleValue()));
        }
    }

    public static void verifyId(Long id) throws ServiceException {
        if(Objects.isNull(id)) {
            throw new ServiceVerificationException(String.format(NULLABLE, "Id"));
        }
        if(id < 0) {
            throw new ServiceVerificationException(String.format(INCORRECT_ID, id));
        }
    }

    public static void verifyString(String input) throws ServiceException {
        if(Objects.isNull(input)) {
            throw new ServiceVerificationException(String.format(NULLABLE, "Parameter"));
        }
        if(input.isEmpty()) {
            throw new ServiceVerificationException(String.format(EMPTY, "Parameter"));
        }
    }

    public static void verifyIds(Long... ids) throws ServiceException {
        for(Long id: ids) {
            verifyId(id);
        }
    }

    public static void verifyStrings(String... strings) throws ServiceException {
        for(String input: strings) {
            verifyString(input);
        }
    }
}
