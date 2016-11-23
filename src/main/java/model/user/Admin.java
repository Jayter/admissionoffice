package model.user;

import java.time.LocalDate;

public class Admin extends User {
    public Admin(String name, String secondName, String address, String email,
                 String phoneNumber, LocalDate birthDate) {
        super(name, secondName, address, email, phoneNumber, birthDate);
    }

    public Admin(int id, String name, String secondName, String address, String email,
                 String phoneNumber, LocalDate birthDate) {
        super(id, name, secondName, address, email, phoneNumber, birthDate);
    }
}
