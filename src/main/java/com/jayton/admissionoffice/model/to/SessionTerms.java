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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SessionTerms terms = (SessionTerms) o;

        if (year != null ? !year.equals(terms.year) : terms.year != null) return false;
        if (sessionStart != null ? !sessionStart.equals(terms.sessionStart) : terms.sessionStart != null) return false;
        return sessionEnd != null ? sessionEnd.equals(terms.sessionEnd) : terms.sessionEnd == null;

    }

    @Override
    public int hashCode() {
        int result = year != null ? year.hashCode() : 0;
        result = 31 * result + (sessionStart != null ? sessionStart.hashCode() : 0);
        result = 31 * result + (sessionEnd != null ? sessionEnd.hashCode() : 0);
        return result;
    }
}
