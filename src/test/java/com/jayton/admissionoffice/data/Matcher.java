package com.jayton.admissionoffice.data;

import java.math.BigDecimal;
import java.util.List;

public abstract class Matcher<T> {
    public abstract boolean equals(T first, T second);
    public abstract boolean equals(List<T> first, List<T> second);
    protected BigDecimal scale(BigDecimal in) {
        return in.setScale(2, BigDecimal.ROUND_HALF_UP);
    }
}