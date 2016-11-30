package com.jayton.admissionoffice.model.to;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by Jayton on 28.11.2016.
 */
public class ExamResult implements Serializable {
    private Long userId;
    private Long subjectId;
    private BigDecimal mark;

    public ExamResult(Long userId, Long subjectId, BigDecimal mark) {
        this.userId = userId;
        this.subjectId = subjectId;
        this.mark = mark;
    }

    public Long getUserId() {
        return userId;
    }

    public Long getSubjectId() {
        return subjectId;
    }

    public BigDecimal getMark() {
        return mark;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ExamResult that = (ExamResult) o;

        if (userId != null ? !userId.equals(that.userId) : that.userId != null) return false;
        if (subjectId != null ? !subjectId.equals(that.subjectId) : that.subjectId != null) return false;
        return mark != null ? mark.compareTo(that.mark) == 0 : that.mark == null;

    }

    @Override
    public int hashCode() {
        int result = userId != null ? userId.hashCode() : 0;
        result = 31 * result + (subjectId != null ? subjectId.hashCode() : 0);
        result = 31 * result + (mark != null ? mark.intValue() : 0);
        return result;
    }
}
