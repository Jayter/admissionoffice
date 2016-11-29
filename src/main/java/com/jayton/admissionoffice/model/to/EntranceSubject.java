package com.jayton.admissionoffice.model.to;

import java.math.BigDecimal;

/**
 * Created by Jayton on 29.11.2016.
 */
public class EntranceSubject {
    private Long directionId;
    private Long subjectId;
    private BigDecimal coef;

    public EntranceSubject() {
    }

    public EntranceSubject(Long directionId, Long subjectId, BigDecimal coef) {
        this.directionId = directionId;
        this.subjectId = subjectId;
        this.coef = coef;
    }

    public Long getDirectionId() {
        return directionId;
    }

    public void setDirectionId(Long directionId) {
        this.directionId = directionId;
    }

    public Long getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(Long subjectId) {
        this.subjectId = subjectId;
    }

    public BigDecimal getCoef() {
        return coef;
    }

    public void setCoef(BigDecimal coef) {
        this.coef = coef;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EntranceSubject that = (EntranceSubject) o;

        if (directionId != null ? !directionId.equals(that.directionId) : that.directionId != null) return false;
        if (subjectId != null ? !subjectId.equals(that.subjectId) : that.subjectId != null) return false;
        return coef != null ? coef.compareTo(that.coef) == 0 : that.coef == null;
    }

    @Override
    public int hashCode() {
        int result = directionId != null ? directionId.hashCode() : 0;
        result = 31 * result + (subjectId != null ? subjectId.hashCode() : 0);
        result = 31 * result + (coef != null ? coef.intValue() : 0);
        return result;
    }
}
