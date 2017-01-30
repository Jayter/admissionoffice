package com.jayton.admissionoffice.model.university;

import java.math.BigDecimal;
import java.util.Map;

public class Direction {
    private long id;
    private String name;
    private BigDecimal averageCoefficient;
    private int countOfStudents;
    private long facultyId;
    private Map<Long, BigDecimal> entranceSubjects;

    public Direction(String name, BigDecimal averageCoefficient, int countOfStudents, long facultyId,
                     Map<Long, BigDecimal> entranceSubjects) {
        this.name = name;
        this.averageCoefficient = averageCoefficient;
        this.countOfStudents = countOfStudents;
        this.facultyId = facultyId;
        this.entranceSubjects = entranceSubjects;
    }

    public Direction(long id, String name, BigDecimal averageCoefficient, int countOfStudents, long facultyId) {
        this.id = id;
        this.name = name;
        this.averageCoefficient = averageCoefficient;
        this.countOfStudents = countOfStudents;
        this.facultyId = facultyId;
    }

    public Direction(long id, String name, BigDecimal averageCoefficient, int countOfStudents, long facultyId,
                     Map<Long, BigDecimal> entranceSubjects) {
        this.id = id;
        this.name = name;
        this.averageCoefficient = averageCoefficient;
        this.countOfStudents = countOfStudents;
        this.facultyId = facultyId;
        this.entranceSubjects = entranceSubjects;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getAverageCoefficient() {
        return averageCoefficient;
    }

    public int getCountOfStudents() {
        return countOfStudents;
    }

    public long getFacultyId() {
        return facultyId;
    }

    public Map<Long, BigDecimal> getEntranceSubjects() {
        return entranceSubjects;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Direction direction = (Direction) o;

        if (id != direction.id) return false;
        if (countOfStudents != direction.countOfStudents) return false;
        if (facultyId != direction.facultyId) return false;
        if (name != null ? !name.equals(direction.name) : direction.name != null) return false;
        if (averageCoefficient != null ? !averageCoefficient.equals(direction.averageCoefficient) : direction.averageCoefficient != null)
            return false;
        return entranceSubjects != null ? entranceSubjects.equals(direction.entranceSubjects) : direction.entranceSubjects == null;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (averageCoefficient != null ? averageCoefficient.hashCode() : 0);
        result = 31 * result + countOfStudents;
        result = 31 * result + (int) (facultyId ^ (facultyId >>> 32));
        result = 31 * result + (entranceSubjects != null ? entranceSubjects.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Direction{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", averageCoefficient=" + averageCoefficient +
                ", countOfStudents=" + countOfStudents +
                ", facultyId=" + facultyId +
                ", entranceSubjects=" + entranceSubjects +
                '}';
    }
}