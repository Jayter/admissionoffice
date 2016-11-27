package com.jayton.admissionoffice.model.user;

import com.jayton.admissionoffice.model.Subject;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.Map;

public class Enrollee extends User {
    private Map<Subject, BigDecimal> examResults;
    private BigDecimal averageMark;

    public Enrollee() {
    }

    public Enrollee(String name, String secondName, String address, String email, String password,
                    String phoneNumber, LocalDate birthDate, BigDecimal averageMark) {
        super(name, secondName, address, email, password, phoneNumber, birthDate);
        this.averageMark = averageMark;
    }

    public Enrollee(Long id, String name, String secondName, String address, String email, String password,
                    String phoneNumber, LocalDate birthDate, BigDecimal averageMark) {
        super(id, name, secondName, address, email, password, phoneNumber, birthDate);
        this.averageMark = averageMark;
    }

    public Enrollee(String name, String secondName, String address, String email, String password, String phoneNumber,
                    LocalDate birthDate, Map<Subject, BigDecimal> examResults, BigDecimal averageMark) {
        super(name, secondName, address, email, password, phoneNumber, birthDate);
        this.examResults = examResults;
        this.averageMark = averageMark;
    }

    public Enrollee(Long id, String name, String secondName, String address, String email, String password,
                    String phoneNumber, LocalDate birthDate, Map<Subject, BigDecimal> examResults, BigDecimal averageMark) {
        super(id, name, secondName, address, email, password, phoneNumber, birthDate);
        this.examResults = examResults;
        this.averageMark = averageMark;
    }

    public Map<Subject, BigDecimal> getExamResults() {
        return examResults;
    }

    public void setExamResults(Map<Subject, BigDecimal> examResults) {
        this.examResults = examResults;
    }

    public BigDecimal getAverageMark() {
        return averageMark;
    }

    public void setAverageMark(BigDecimal averageMark) {
        this.averageMark = averageMark;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Enrollee enrollee = (Enrollee) o;

        if (examResults != null ? !examResults.equals(enrollee.examResults) : enrollee.examResults != null)
            return false;
        return averageMark != null ? averageMark.compareTo(enrollee.getAverageMark()) == 0 : enrollee.averageMark == null;

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (examResults != null ? examResults.hashCode() : 0);
        result = 31 * result + (averageMark != null ? averageMark.hashCode() : 0);
        return result;
    }
}
