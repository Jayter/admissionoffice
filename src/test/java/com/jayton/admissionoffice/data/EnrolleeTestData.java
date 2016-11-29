package com.jayton.admissionoffice.data;

import com.jayton.admissionoffice.model.to.ExamResult;
import com.jayton.admissionoffice.model.user.Enrollee;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;
import java.util.List;

import static com.jayton.admissionoffice.data.CommonTestData.START_SEQUENCE;

/**
 * Created by Jayton on 27.11.2016.
 */
public class EnrolleeTestData {

    public static final Enrollee ENROLLEE1 = new Enrollee(START_SEQUENCE, "Дмитро", "Васильков", "Ужгород, Загорська 35",
            "vas_dim@gmail.com", "dim-dim", "+380671823452", LocalDate.of(2000, Month.MARCH, 22), BigDecimal.valueOf(10.5));
    public static final Enrollee ENROLLEE2 = new Enrollee(START_SEQUENCE + 1, "Катерина", "Руденко", "Умань, Європейська 12",
            "kate2000@gmail.com", "kk-kkate", "+380632212612", LocalDate.of(1999, Month.DECEMBER, 11), BigDecimal.valueOf(9.8));
    public static final Enrollee ENROLLEE3 = new Enrollee(START_SEQUENCE + 2, "Максим", "Панченко", "Шостка, Сімейна 1",
            "pan_max@gmail.com", "maxim(pan)", "+380952235616", LocalDate.of(2001, Month.JANUARY, 2), BigDecimal.valueOf(8.7));
    public static final Enrollee NEW_ENROLLEE = new Enrollee("Анна", "Мохир", "Боярка, Шевченка 45",
            "anna.mochyr@yandex.ru", "kotik18", "+380991329782", LocalDate.of(1990, Month.JULY, 7), BigDecimal.valueOf(12.0));
    public static final Enrollee ENROLLEE_WITH_NULLABLE_FIELDS = new Enrollee();

    public static final ExamResult RESULT1 = new ExamResult(START_SEQUENCE, START_SEQUENCE + 3, BigDecimal.valueOf(192.5));
    public static final ExamResult RESULT2 = new ExamResult(START_SEQUENCE, START_SEQUENCE + 4, BigDecimal.valueOf(187.0));
    public static final ExamResult RESULT3 = new ExamResult(START_SEQUENCE, START_SEQUENCE + 5, BigDecimal.valueOf(183.25));
    public static final ExamResult RESULT4 = new ExamResult(START_SEQUENCE, START_SEQUENCE + 6, BigDecimal.valueOf(181.2));

    public static final ExamResult RESULT5 = new ExamResult(START_SEQUENCE + 1, START_SEQUENCE + 3, BigDecimal.valueOf(175.1));
    public static final ExamResult RESULT6 = new ExamResult(START_SEQUENCE + 1, START_SEQUENCE + 5, BigDecimal.valueOf(180.2));
    public static final ExamResult RESULT7 = new ExamResult(START_SEQUENCE + 1, START_SEQUENCE + 4, BigDecimal.valueOf(194.55));

    public static final List<ExamResult> NEW_RESULTS = Arrays.asList(RESULT5, RESULT6, RESULT7);
    public static final List<ExamResult> ALL_RESULTS = Arrays.asList(RESULT1, RESULT2, RESULT3, RESULT4);

    static {
        ENROLLEE_WITH_NULLABLE_FIELDS.setName("Ім`я");
        ENROLLEE_WITH_NULLABLE_FIELDS.setBirthDate(LocalDate.of(1992, Month.FEBRUARY, 19));
    }
}
