package model.university;

import model.BaseEntity;
import model.Subject;

import java.util.Map;

public class Direction extends BaseEntity {
    private double averageCoefficient;
    private int countOfStudents;
    private Map<Subject, Integer> entranceSubjects;
    private Faculty owner;

    public Direction(String name, double averageCoefficient, int countOfStudents,
                     Map<Subject, Integer> entranceSubjects, Faculty owner) {
        super(name);
        this.averageCoefficient = averageCoefficient;
        this.countOfStudents = countOfStudents;
        this.entranceSubjects = entranceSubjects;
        this.owner = owner;
    }

    public Direction(int id, String name, double averageCoefficient, int countOfStudents,
                     Map<Subject, Integer> entranceSubjects, Faculty owner) {
        super(id, name);
        this.averageCoefficient = averageCoefficient;
        this.countOfStudents = countOfStudents;
        this.entranceSubjects = entranceSubjects;
        this.owner = owner;
    }

    public double getAverageCoefficient() {
        return averageCoefficient;
    }

    public int getCountOfStudents() {
        return countOfStudents;
    }

    public Map<Subject, Integer> getEntranceSubjects() {
        return entranceSubjects;
    }

    public Faculty getOwner() {
        return owner;
    }
}
