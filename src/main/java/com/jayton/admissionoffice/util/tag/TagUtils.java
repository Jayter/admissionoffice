package com.jayton.admissionoffice.util.tag;

import com.jayton.admissionoffice.model.to.SessionTerms;
import com.jayton.admissionoffice.model.university.Direction;
import com.jayton.admissionoffice.model.user.User;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Utility class for functions, used on jsps.
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

    public static boolean isBeforeSessionStart(SessionTerms terms) {
        LocalDateTime current = LocalDateTime.now();
        if(terms != null && current.isAfter(terms.getSessionStart())) {
            return false;
        }
        return true;
    }

    public static boolean isAfterSessionEnd(SessionTerms terms) {
        LocalDateTime current = LocalDateTime.now();
        if(terms == null || current.isBefore(terms.getSessionEnd())) {
            return false;
        }
        return true;
    }

    public static boolean isWithinSessionStart(SessionTerms terms) {
        LocalDateTime current = LocalDateTime.now();
        if(terms == null || current.isAfter(terms.getSessionEnd()) || current.isBefore(terms.getSessionStart())) {
            return false;
        }
        return true;
    }

    public static boolean isBeyondSessionStart(SessionTerms terms) {
        LocalDateTime current = LocalDateTime.now();
        if(terms != null && current.isAfter(terms.getSessionStart()) && current.isBefore(terms.getSessionEnd())) {
            return false;
        }
        return true;
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

    public static boolean containsAllSubjects(Direction direction, User user) {
        return user.getResults().keySet().containsAll(direction.getEntranceSubjects().keySet());
    }
}