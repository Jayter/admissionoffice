package com.jayton.admissionoffice.model.user;

import com.jayton.admissionoffice.model.NamedEntity;

import java.time.LocalDate;
import java.util.Map;

public class User extends NamedEntity {
    private String lastName;
    private String address;
    private String email;
    private String password;
    private String phoneNumber;
    private LocalDate birthDate;
    private Byte averageMark;
    private Map<Long, Short> results;

    public User(String name, String lastName, String address, String email, String password,
                String phoneNumber, LocalDate birthDate, Byte averageMark) {
        super(name);
        this.lastName = lastName;
        this.address = address;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.birthDate = birthDate;
        this.averageMark = averageMark;
    }

    public User(Long id, String name, String lastName, String address, String phoneNumber,
                LocalDate birthDate, Byte averageMark) {
        super(id, name);
        this.lastName = lastName;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.birthDate = birthDate;
        this.averageMark = averageMark;
    }

    public User(Long id, String name, String lastName, String address, String email, String phoneNumber,
                LocalDate birthDate, Byte averageMark, Map<Long, Short> results) {
        super(id, name);
        this.lastName = lastName;
        this.address = address;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.birthDate = birthDate;
        this.averageMark = averageMark;
        this.results = results;
    }

    public Byte getAverageMark() {
        return averageMark;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPassword() {
        return password;
    }

    public String getAddress() {
        return address;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public Map<Long, Short> getResults() {
        return results;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        User user = (User) o;

        if (lastName != null ? !lastName.equals(user.lastName) : user.lastName != null) return false;
        if (address != null ? !address.equals(user.address) : user.address != null) return false;
        if (email != null ? !email.equals(user.email) : user.email != null) return false;
        if (phoneNumber != null ? !phoneNumber.equals(user.phoneNumber) : user.phoneNumber != null) return false;
        if (birthDate != null ? !birthDate.equals(user.birthDate) : user.birthDate != null) return false;
        if (averageMark != null ? !(averageMark.equals(user.averageMark)) : user.averageMark != null) return false;
        return results != null ? results.equals(user.results) : user.results == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (lastName != null ? lastName.hashCode() : 0);
        result = 31 * result + (address != null ? address.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (phoneNumber != null ? phoneNumber.hashCode() : 0);
        result = 31 * result + (birthDate != null ? birthDate.hashCode() : 0);
        result = 31 * result + (averageMark != null ? averageMark : 0);
        result = 31 * result + (results != null ? results.hashCode() : 0);
        return result;
    }
}
