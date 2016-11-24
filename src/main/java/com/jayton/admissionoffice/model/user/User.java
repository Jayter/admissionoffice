package com.jayton.admissionoffice.model.user;

import com.jayton.admissionoffice.model.BaseEntity;

import java.time.LocalDate;

public class User extends BaseEntity {
    protected String secondName;
    protected String address;
    protected String email;
    protected String password;
    protected String phoneNumber;
    protected LocalDate birthDate;

    public User() {
    }

    public User(String name, String secondName, String address, String email,
                String password, String phoneNumber, LocalDate birthDate) {
        super(name);
        this.secondName = secondName;
        this.address = address;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.birthDate = birthDate;
    }

    public User(Long id, String name, String secondName, String address, String email,
                String password, String phoneNumber, LocalDate birthDate) {
        super(id, name);
        this.secondName = secondName;
        this.address = address;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.birthDate = birthDate;
    }

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
