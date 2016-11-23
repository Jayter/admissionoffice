package model.user;

import model.BaseEntity;

import java.time.LocalDate;

public class User extends BaseEntity {
    protected String secondName;
    protected String address;
    protected String email;
    protected String phoneNumber;
    protected LocalDate birthDate;

    public User(String name, String secondName, String address, String email,
                String phoneNumber, LocalDate birthDate) {
        super(name);
        this.secondName = secondName;
        this.address = address;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.birthDate = birthDate;
    }

    public User(int id, String name, String secondName, String address, String email,
                String phoneNumber, LocalDate birthDate) {
        super(id, name);
        this.secondName = secondName;
        this.address = address;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.birthDate = birthDate;
    }
}
