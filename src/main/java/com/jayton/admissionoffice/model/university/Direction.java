package com.jayton.admissionoffice.model.university;

import com.jayton.admissionoffice.model.BaseEntity;
import com.jayton.admissionoffice.model.Subject;

import java.math.BigDecimal;
import java.util.Map;

public class Direction extends BaseEntity {
    private BigDecimal averageCoefficient;
    private int countOfStudents;
    private Long ownerId;

    public Direction() {
    }

    public Direction(String name, BigDecimal averageCoefficient, int countOfStudents, Long ownerId) {
        super(name);
        this.averageCoefficient = averageCoefficient;
        this.countOfStudents = countOfStudents;
        this.ownerId = ownerId;
    }

    public Direction(Long id, String name, BigDecimal averageCoefficient, int countOfStudents, Long ownerId) {
        super(id, name);
        this.averageCoefficient = averageCoefficient;
        this.countOfStudents = countOfStudents;
        this.ownerId = ownerId;
    }

    public BigDecimal getAverageCoefficient() {
        return averageCoefficient;
    }

    public void setAverageCoefficient(BigDecimal averageCoefficient) {
        this.averageCoefficient = averageCoefficient;
    }

    public int getCountOfStudents() {
        return countOfStudents;
    }

    public void setCountOfStudents(int countOfStudents) {
        this.countOfStudents = countOfStudents;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Direction direction = (Direction) o;

        if (countOfStudents != direction.countOfStudents) return false;
        if (averageCoefficient != null ? !averageCoefficient.equals(direction.averageCoefficient) : direction.averageCoefficient != null)
            return false;
        return ownerId != null ? ownerId.equals(direction.ownerId) : direction.ownerId == null;

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (averageCoefficient != null ? averageCoefficient.hashCode() : 0);
        result = 31 * result + countOfStudents;
        result = 31 * result + (ownerId != null ? ownerId.hashCode() : 0);
        return result;
    }
}
