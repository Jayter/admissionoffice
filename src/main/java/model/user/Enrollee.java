package model.user;

import model.Subject;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Map;

public class Enrollee extends User {
    private Map<Subject, Integer> examResults;
    private double averageMark;

    public Enrollee(String name, String secondName, String address, String email,
                    String phoneNumber, LocalDate birthDate, double averageMark) {
        super(name, secondName, address, email, phoneNumber, birthDate);
        this.averageMark = averageMark;
    }

    public Enrollee(Long id, String name, String secondName, String address, String email,
                    String phoneNumber, LocalDate birthDate, double averageMark) {
        super(id, name, secondName, address, email, phoneNumber, birthDate);
        this.averageMark = averageMark;
    }

    public Enrollee(String name, String secondName, String address, String email, String phoneNumber,
                    LocalDate birthDate, Map<Subject, Integer> examResults, double averageMark) {
        super(name, secondName, address, email, phoneNumber, birthDate);
        this.examResults = examResults;
        this.averageMark = averageMark;
    }

    public Enrollee(Long id, String name, String secondName, String address, String email, String phoneNumber,
                    LocalDate birthDate, Map<Subject, Integer> examResults, double averageMark) {
        super(id, name, secondName, address, email, phoneNumber, birthDate);
        this.examResults = examResults;
        this.averageMark = averageMark;
    }

    public Map<Subject, Integer> getExamResults() {
        return examResults;
    }

    public void setExamResults(Map<Subject, Integer> examResults) {
        this.examResults = examResults;
    }

    public double getAverageMark() {
        return averageMark;
    }

    public void setAverageMark(double averageMark) {
        this.averageMark = averageMark;
    }
}
