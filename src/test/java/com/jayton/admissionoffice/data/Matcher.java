package com.jayton.admissionoffice.data;

import java.math.BigDecimal;

public abstract class Matcher<T> {
    public abstract boolean compare(T first, T second);
    protected static BigDecimal scale(BigDecimal in) {
        return in.setScale(2, BigDecimal.ROUND_HALF_UP);
    }
}