package com.jayton.admissionoffice.model.university;

import com.jayton.admissionoffice.model.NamedEntity;

import java.math.BigDecimal;
import java.util.Map;

public class Direction extends NamedEntity {
    private BigDecimal averageCoefficient;
    private Integer countOfStudents;
    private Long facultyId;
    private Map<Long, BigDecimal> entranceSubjects;

    public Direction(String name, BigDecimal averageCoefficient, Integer countOfStudents, Long facultyId,
                     Map<Long, BigDecimal> entranceSubjects) {
        super(name);
        this.averageCoefficient = averageCoefficient;
        this.countOfStudents = countOfStudents;
        this.facultyId = facultyId;
        this.entranceSubjects = entranceSubjects;
    }

    public Direction(Long id, String name, BigDecimal averageCoefficient, Integer countOfStudents, Long facultyId) {
        super(id, name);
        this.averageCoefficient = averageCoefficient;
        this.countOfStudents = countOfStudents;
        this.facultyId = facultyId;
    }

    public Direction(Long id, String name, BigDecimal averageCoefficient, Integer countOfStudents, Long facultyId,
                     Map<Long, BigDecimal> entranceSubjects) {
        super(id, name);
        this.averageCoefficient = averageCoefficient;
        this.countOfStudents = countOfStudents;
        this.facultyId = facultyId;
        this.entranceSubjects = entranceSubjects;
    }

    public BigDecimal getAverageCoefficient() {
        return averageCoefficient;
    }

    public Integer getCountOfStudents() {
        return countOfStudents;
    }

    public Long getFacultyId() {
        return facultyId;
    }

    public Map<Long, BigDecimal> getEntranceSubjects() {
        return entranceSubjects;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Direction direction = (Direction) o;

        if (averageCoefficient != null ? !averageCoefficient.equals(direction.averageCoefficient) : direction.averageCoefficient != null)
            return false;
        if (countOfStudents != null ? !countOfStudents.equals(direction.countOfStudents) : direction.countOfStudents != null)
            return false;
        if (facultyId != null ? !facultyId.equals(direction.facultyId) : direction.facultyId != null) return false;
        return entranceSubjects != null ? entranceSubjects.equals(direction.entranceSubjects) : direction.entranceSubjects == null;

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (averageCoefficient != null ? averageCoefficient.hashCode() : 0);
        result = 31 * result + (countOfStudents != null ? countOfStudents.hashCode() : 0);
        result = 31 * result + (facultyId != null ? facultyId.hashCode() : 0);
        result = 31 * result + (entranceSubjects != null ? entranceSubjects.hashCode() : 0);
        return result;
    }
}
