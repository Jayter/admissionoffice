package com.jayton.admissionoffice.dao.jdbc.util;

import java.math.BigDecimal;

/**
 * Created by Jayton on 29.11.2016.
 */
public class NumericHelper {

    private NumericHelper() {

    }

    public static BigDecimal scale(BigDecimal bigDecimal, int scale) {
        return bigDecimal.setScale(scale, BigDecimal.ROUND_HALF_UP);
    }
}
