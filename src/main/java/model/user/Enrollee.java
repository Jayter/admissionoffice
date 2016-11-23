package model.user;

import model.Subject;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Map;

public class Enrollee extends User {
    private Map<Subject, Integer> examResults;
    private double averageMark;

    public Enrollee(String name, String secondName, String address, String email, String phoneNumber,
                    LocalDate birthDate, Map<Subject, Integer> examResults, double averageMark) {
        super(name, secondName, address, email, phoneNumber, birthDate);
        this.examResults = examResults;
        this.averageMark = averageMark;
    }

    public Enrollee(int id, String name, String secondName, String address, String email, String phoneNumber,
                    LocalDate birthDate, Map<Subject, Integer> examResults, double averageMark) {
        super(id, name, secondName, address, email, phoneNumber, birthDate);
        this.examResults = examResults;
        this.averageMark = averageMark;
    }

    public Map<Subject, Integer> getExamResults() {
        return Collections.unmodifiableMap(examResults);
    }

    public double getAverageMark() {
        return averageMark;
    }
}
