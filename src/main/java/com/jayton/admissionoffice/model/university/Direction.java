package com.jayton.admissionoffice.model.university;

import com.jayton.admissionoffice.model.BaseEntity;
import com.jayton.admissionoffice.model.Subject;

import java.util.Map;

public class Direction extends BaseEntity {
    private double averageCoefficient;
    private int countOfStudents;
    private Faculty owner;
    private Map<Subject, Integer> entranceSubjects;

    public Direction(String name, double averageCoefficient, int countOfStudents) {
        super(name);
        this.averageCoefficient = averageCoefficient;
        this.countOfStudents = countOfStudents;
    }

    public Direction(Long id, String name, double averageCoefficient, int countOfStudents) {
        super(id, name);
        this.averageCoefficient = averageCoefficient;
        this.countOfStudents = countOfStudents;
    }

    public Direction(String name, double averageCoefficient, int countOfStudents,
                     Faculty owner, Map<Subject, Integer> entranceSubjects) {
        super(name);
        this.averageCoefficient = averageCoefficient;
        this.countOfStudents = countOfStudents;
        this.owner = owner;
        this.entranceSubjects = entranceSubjects;
    }

    public Direction(Long id, String name, double averageCoefficient, int countOfStudents,
                     Faculty owner, Map<Subject, Integer> entranceSubjects) {
        super(id, name);
        this.averageCoefficient = averageCoefficient;
        this.countOfStudents = countOfStudents;
        this.owner = owner;
        this.entranceSubjects = entranceSubjects;
    }

    public double getAverageCoefficient() {
        return averageCoefficient;
    }

    public void setAverageCoefficient(double averageCoefficient) {
        this.averageCoefficient = averageCoefficient;
    }

    public int getCountOfStudents() {
        return countOfStudents;
    }

    public void setCountOfStudents(int countOfStudents) {
        this.countOfStudents = countOfStudents;
    }

    public Map<Subject, Integer> getEntranceSubjects() {
        return entranceSubjects;
    }

    public void setEntranceSubjects(Map<Subject, Integer> entranceSubjects) {
        this.entranceSubjects = entranceSubjects;
    }

    public Faculty getOwner() {
        return owner;
    }

    public void setOwner(Faculty owner) {
        this.owner = owner;
    }
}
