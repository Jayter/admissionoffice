package com.jayton.admissionoffice.data;

import com.jayton.admissionoffice.model.user.Enrollee;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;

/**
 * Created by Jayton on 27.11.2016.
 */
public class EnrolleeTestData {
    public static final Long ENROLLEE1_ID = 10000L;

    public static final Enrollee ENROLLEE1 = new Enrollee(ENROLLEE1_ID, "Дмитро", "Васильков", "Ужгород, Загорська 35",
            "vas_dim@gmail.com", "dim-dim", "+380671823452", LocalDate.of(2000, Month.MARCH, 22), BigDecimal.valueOf(10.5));
    public static final Enrollee ENROLLEE2 = new Enrollee(ENROLLEE1_ID + 1, "Катерина", "Руденко", "Умань, Європейська 12",
            "kate2000@gmail.com", "kk-kkate", "+380632212612", LocalDate.of(1999, Month.DECEMBER, 11), BigDecimal.valueOf(9.8));
    public static final Enrollee ENROLLEE3 = new Enrollee(ENROLLEE1_ID + 2, "Максим", "Панченко", "Шостка, Сімейна 1",
            "pan_max@gmail.com", "maxim(pan)", "+380952235616", LocalDate.of(2001, Month.JANUARY, 2), BigDecimal.valueOf(8.7));
    public static final Enrollee NEW_ENROLLEE = new Enrollee("Анна", "Мохир", "Боярка, Шевченка 45",
            "anna.mochyr@yandex.ru", "kotik18", "+380991329782", LocalDate.of(1990, Month.JULY, 7), BigDecimal.valueOf(12.0));
    public static final Enrollee ENROLLEE_WITH_NULLABLE_FIELDS = new Enrollee();

    static {
        ENROLLEE_WITH_NULLABLE_FIELDS.setName("Ім`я");
        ENROLLEE_WITH_NULLABLE_FIELDS.setBirthDate(LocalDate.of(1992, Month.FEBRUARY, 19));
    }
}
