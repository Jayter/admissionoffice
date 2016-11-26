package com.jayton.admissionoffice.data;

import com.jayton.admissionoffice.model.user.Admin;

import java.time.LocalDate;
import java.time.Month;

/**
 * Created by Jayton on 25.11.2016.
 */
public class AdminTestData {
    public static final Long ADMIN1_ID = 10010L;
    public static final Long INCORRECT_ID = 10L;
    public static final Admin ADMIN1 = new Admin(ADMIN1_ID, "Андрій", "Пилипенко", "Київ, Васильківська 25",
            "a_pylip@gmail.com", "pylip19", "+380674562212", LocalDate.of(1984, Month.MARCH, 22));
    public static final Admin ADMIN2 = new Admin(ADMIN1_ID + 1, "Василь", "Крилов", "Львів, Некрасова 12",
            "krylov@gmail.com", "1kr45kr", "+380632245612", LocalDate.of(1995, Month.DECEMBER, 11));
    public static final Admin ADMIN3 = new Admin(ADMIN1_ID + 2, "Ігор", "Дудік", "Житомир, Польова 1",
            "duddik@gmail.com", "12dud12", "+380672235616", LocalDate.of(1989, Month.JUNE, 2));
    public static final Admin ADMIN4 = new Admin(ADMIN1_ID + 3, "Анна", "Мохир", "Боярка, Шевченка 45",
            "anna.mochyr@yandex.ru", "kotik18", "+380991329782", LocalDate.of(1990, Month.JULY, 7));

}
