package com.jayton.admissionoffice.model.user;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Enrollee extends User {
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

        return averageMark != null ? averageMark.compareTo(enrollee.getAverageMark()) == 0 : enrollee.averageMark == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (averageMark != null ? averageMark.intValue() : 0);
        return result;
    }
}
