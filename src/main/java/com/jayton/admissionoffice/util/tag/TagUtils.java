package com.jayton.admissionoffice.util.tag;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Utility class for dates, used on jsp pages.
 */
public class TagUtils {

    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private TagUtils() {}

    public static String format(BigDecimal coefficient) {
        return coefficient.setScale(2, BigDecimal.ROUND_HALF_UP).toString();
    }

    public static String format(LocalDateTime dateTime) {
        return dateTime.format(DATE_TIME_FORMATTER);
    }

    public static String format(LocalDate date) {
        return date.format(DATE_FORMATTER);
    }

    public static boolean isBetween(LocalDateTime sessionStart, LocalDateTime sessionEnd) {
        LocalDateTime current = LocalDateTime.now();
        return current.isAfter(sessionStart) && current.isBefore(sessionEnd);
    }

    public static boolean isBefore(LocalDateTime sessionStart) {
        return LocalDateTime.now().isBefore(sessionStart);
    }

    public static boolean isAfter(LocalDateTime sessionEnd) {
        return LocalDateTime.now().isAfter(sessionEnd);
    }
}