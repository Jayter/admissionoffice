package com.jayton.admissionoffice.command.util;

import com.jayton.admissionoffice.command.exception.VerificationException;

import java.math.BigDecimal;
import java.util.Objects;

public class Verifier {

    public static final String INCORRECT_COEF = "Coefficient must be less than 1 and greater than 0, but was: %f.";
    public static final String INCORRECT_NUMBER = "Number must not be negative, but was: %d.";
    public static final String INCORRECT_MARK = "Mark must be less than 12 and greater than 0, but was: %d.";
    public static final String INCORRECT_RESULT = "Exam result must be less than 200 and greater than 100, but was: %d.";
    public static final String INCORRECT_ID = "Id must be positive, but was: %d.";
    public static final String INCORRECT_PASSWORD = "Password must be longer than 5 symbols..";
    public static final String INCORRECT_EMAIL = "Incorrect email.";
    public static final String NULLABLE = "%s must not be null.";
    public static final String EMPTY = "%s must not be empty.";

    private Verifier(){}

    public static void verifyNonNegative(Long number) throws VerificationException {
        if(number < 0) {
            throw new VerificationException(String.format(INCORRECT_NUMBER, number));
        }
    }

    public static void verifyObject(Object o) throws VerificationException {
        if(o == null) {
            throw new VerificationException(String.format(NULLABLE, "Param"));
        }
    }

    public static void verifyPassword(String password) throws VerificationException {
        if(Objects.isNull(password)) {
            throw new VerificationException(String.format(NULLABLE, "Password"));
        }
        if(password.length() < 6) {
            throw new VerificationException(INCORRECT_PASSWORD);
        }
    }

    public static void verifyEmail(String email) throws VerificationException {
        if(Objects.isNull(email)) {
            throw new VerificationException(String.format(NULLABLE, "Email"));
        }
        if(email.indexOf('@') == -1) {
            throw new VerificationException(INCORRECT_EMAIL);
        }
    }

    public static void verifyResult(Short result) throws VerificationException {
        if(Objects.isNull(result)) {
            throw new VerificationException(String.format(NULLABLE, "Exam result"));
        }
        if(result <= 100 || result > 200) {
            throw new VerificationException(String.format(INCORRECT_RESULT, result));
        }
    }

    public static void verifyMark(Byte mark) throws VerificationException {
        if(Objects.isNull(mark)) {
            throw new VerificationException(String.format(NULLABLE, "Mark"));
        }
        if(mark <= 0 || mark > 12) {
            throw new VerificationException(String.format(INCORRECT_MARK, mark));
        }
    }

    public static void verifyCoef(BigDecimal coef) throws VerificationException {
        if(Objects.isNull(coef)) {
            throw new VerificationException(String.format(NULLABLE, "Coefficient"));
        }
        if(coef.compareTo(BigDecimal.ZERO) <= 0 || coef.compareTo(BigDecimal.ONE) >= 0) {
            throw new VerificationException(String.format(INCORRECT_COEF, coef.doubleValue()));
        }
    }

    public static void verifyCoefs(BigDecimal... coefs) throws VerificationException {
        for(BigDecimal coef: coefs) {
            verifyCoef(coef);
        }
    }

    public static void verifyId(Long id) throws VerificationException {
        if(Objects.isNull(id)) {
            throw new VerificationException(String.format(NULLABLE, "Id"));
        }
        if(id < 0) {
            throw new VerificationException(String.format(INCORRECT_ID, id));
        }
    }

    public static void verifyString(String input) throws VerificationException {
        if(Objects.isNull(input)) {
            throw new VerificationException(String.format(NULLABLE, "Parameter"));
        }
        if(input.isEmpty()) {
            throw new VerificationException(String.format(EMPTY, "Parameter"));
        }
    }

    public static void verifyIds(Long... ids) throws VerificationException {
        for(Long id: ids) {
            verifyId(id);
        }
    }

    public static void verifyStrings(String... strings) throws VerificationException {
        for(String input: strings) {
            verifyString(input);
        }
    }
}