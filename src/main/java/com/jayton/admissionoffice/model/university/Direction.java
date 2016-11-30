package com.jayton.admissionoffice.model.university;

import com.jayton.admissionoffice.model.NamedEntity;

import java.math.BigDecimal;

public class Direction extends NamedEntity {
    private BigDecimal averageCoefficient;
    private int countOfStudents;
    private Long facultyId;

    public Direction(String name, BigDecimal averageCoefficient, int countOfStudents, Long facultyId) {
        super(name);
        this.averageCoefficient = averageCoefficient;
        this.countOfStudents = countOfStudents;
        this.facultyId = facultyId;
    }

    public Direction(Long id, String name, BigDecimal averageCoefficient, int countOfStudents, Long facultyId) {
        super(id, name);
        this.averageCoefficient = averageCoefficient;
        this.countOfStudents = countOfStudents;
        this.facultyId = facultyId;
    }

    public BigDecimal getAverageCoefficient() {
        return averageCoefficient;
    }

    public int getCountOfStudents() {
        return countOfStudents;
    }

    public Long getFacultyId() {
        return facultyId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Direction direction = (Direction) o;

        if (countOfStudents != direction.countOfStudents) return false;
        if (averageCoefficient != null ? !(averageCoefficient.compareTo(direction.averageCoefficient) == 0) : direction.averageCoefficient != null)
            return false;
        return facultyId != null ? facultyId.equals(direction.facultyId) : direction.facultyId == null;

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (averageCoefficient != null ? averageCoefficient.hashCode() : 0);
        result = 31 * result + countOfStudents;
        result = 31 * result + (facultyId != null ? facultyId.intValue() : 0);
        return result;
    }
}
