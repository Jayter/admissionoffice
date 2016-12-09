package com.jayton.admissionoffice.model.to;

import java.time.LocalDateTime;

public class SessionTerms {
    private Short year;
    private LocalDateTime sessionStart;
    private LocalDateTime sessionEnd;

    public SessionTerms(Short year, LocalDateTime sessionStart, LocalDateTime sessionEnd) {
        this.year = year;
        this.sessionStart = sessionStart;
        this.sessionEnd = sessionEnd;
    }

    public Short getYear() {
        return year;
    }

    public LocalDateTime getSessionStart() {
        return sessionStart;
    }

    public LocalDateTime getSessionEnd() {
        return sessionEnd;
    }
}
