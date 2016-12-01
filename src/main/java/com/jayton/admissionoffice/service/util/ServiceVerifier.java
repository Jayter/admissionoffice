package com.jayton.admissionoffice.service.util;

import com.jayton.admissionoffice.service.exception.ServiceException;
import com.jayton.admissionoffice.service.exception.ServiceVerificationException;

import java.math.BigDecimal;

/**
 * Created by Jayton on 30.11.2016.
 */
public class ServiceVerifier {


    public static final String INCORRECT_ID = "Id must not be empty or negative.";
    public static final String INCORRECT_PHONE = "Incorrect phone number.";
    public static final String INCORRECT_EMAIL = "Incorrect email.";
    public static final String NULL_OR_EMPTY = "Parameters must not be null or empty.";

    private ServiceVerifier(){}

    public static boolean isCorrectId(Long id) {
        return id != null && id > 0;
    }

    public static boolean isResultCorrect(BigDecimal result) {
        return result.compareTo(BigDecimal.valueOf(100)) > 0 &&
                result.compareTo(BigDecimal.valueOf(200)) <= 0;
    }

    public static boolean isPositive(int i) {
        return i > 0;
    }

    public static boolean isCoefCorrect(BigDecimal coef) {
        return coef.compareTo(BigDecimal.ZERO) > 0 && coef.compareTo(BigDecimal.ONE) < 0;
    }

    public static boolean isPhoneCorrect(String phone) {
        return false;
    }

    public static boolean isEmailCorrect(String email) {
        return email.indexOf('@') != -1;
    }

    public static boolean isPasswordCorrect(String password) {
        return !isNullOrEmpty(password) && password.length() >= 6;
    }

    public static boolean isNullOrEmpty(String input) {
        return input == null || input.isEmpty();
    }

    public static boolean isNullOrEmpty(String... input) {
        for(String s: input) {
            if(s == null || s.isEmpty())
                return true;
        }
        return false;
    }

    public static void verifyId(Long id) throws ServiceException {
        if(!ServiceVerifier.isCorrectId(id)) {
            throw new ServiceVerificationException(INCORRECT_ID);
        }
    }

    public static void verifyString(String input) throws ServiceException {
        if(ServiceVerifier.isNullOrEmpty(input)) {
            throw new ServiceVerificationException(NULL_OR_EMPTY);
        }
    }
}
