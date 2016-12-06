package com.jayton.admissionoffice.data;

import com.jayton.admissionoffice.model.NamedEntity;
import com.jayton.admissionoffice.model.Subject;
import com.jayton.admissionoffice.model.to.Application;
import com.jayton.admissionoffice.model.to.Status;
import com.jayton.admissionoffice.model.university.Direction;
import com.jayton.admissionoffice.model.university.Faculty;
import com.jayton.admissionoffice.model.university.University;
import com.jayton.admissionoffice.model.user.User;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

/**
 * Created by Jayton on 30.11.2016.
 */
public class TestData {
    public static final Long START_SEQ = 10000L;
    public static final Long INCORRECT_ID = 10L;
    public static final Long NEW_ID = 10022L;
    public static final String KYIV = "Київ";
    public static final String INCORRECT_STRING = "Some dummy value";

    public static final LocalDateTime START_DATE = LocalDateTime.of(2016, 7, 1, 12, 0, 0);
    public static final LocalDateTime END_DATE = LocalDateTime.of(2016, 7, 21, 12, 0, 0);

    public static final Subject SUBJECT1 = new Subject(START_SEQ, "Українська мова та література");
    public static final Subject SUBJECT2 = new Subject(START_SEQ + 1, "Англійська мова");
    public static final Subject SUBJECT3 = new Subject(START_SEQ + 2, "Математика");
    public static final Subject SUBJECT4 = new Subject(START_SEQ + 3, "Хімія");
    public static final Subject NEW_SUBJECT = new Subject("Алгебро-геометрія");
    public static final Subject DUPLICATED_SUBJECT = new Subject("Математика");
    public static final Subject UPDATED_SUBJECT = new Subject(START_SEQ + 2, "Математичний аналіз");

    public static final User USER1 = new User(START_SEQ + 4, "Дмитро", "Васильков", "Ужгород, Загорська 35",
            "vas_dim@gmail.com", "+380671823452", LocalDate.of(2000, Month.MARCH, 22), BigDecimal.valueOf(10.5),
            new HashMap(){{put(SUBJECT1.getId(), scale(new BigDecimal(192.5))); put(SUBJECT2.getId(), scale(new BigDecimal(187.0)));
                put(SUBJECT3.getId(), scale(new BigDecimal(183.25))); put(SUBJECT4.getId(), scale(new BigDecimal(181.2)));}});
    public static final User USER2 = new User(START_SEQ + 5, "Катерина", "Руденко", "Умань, Європейська 12",
            "kate2000@gmail.com", "+380632212612", LocalDate.of(1999, Month.DECEMBER, 11), BigDecimal.valueOf(9.8),
            new HashMap(){{put(SUBJECT1.getId(), scale(new BigDecimal(190.6))); put(SUBJECT2.getId(), scale(new BigDecimal(184.22)));
                put(SUBJECT3.getId(), scale(new BigDecimal(181.5)));}});
    public static final User USER3 = new User(START_SEQ + 6, "Максим", "Панченко", "Шостка, Сімейна 1",
            "pan_max@gmail.com", "+380952235616", LocalDate.of(2001, Month.JANUARY, 2), BigDecimal.valueOf(8.7),
            Collections.emptyMap());
    public static final User NEW_USER = new User("Анна", "Мохир", "Боярка, Шевченка 45", "anna.moch@yandex.ru",
            "aNNa1q", "+380991329782", LocalDate.of(1990, Month.JULY, 7), BigDecimal.valueOf(12.0),
            new HashMap(){{put(SUBJECT1.getId(), scale(new BigDecimal(190.6))); put(SUBJECT2.getId(), scale(new BigDecimal(184.22)));
                put(SUBJECT3.getId(), scale(new BigDecimal(181.5))); put(SUBJECT4.getId(), scale(new BigDecimal(173.6)));}});
    public static final User NEW_USER_WITHOUT_CREDENTIALS = new User(NEW_ID, "Анна", "Мохир", "Боярка, Шевченка 45",
            "anna.moch@yandex.ru", "+380991329782", LocalDate.of(1990, Month.JULY, 7), BigDecimal.valueOf(12.0),
            new HashMap(){{put(SUBJECT1.getId(), scale(new BigDecimal(190.6))); put(SUBJECT2.getId(), scale(new BigDecimal(184.22)));
                put(SUBJECT3.getId(), scale(new BigDecimal(181.5))); put(SUBJECT4.getId(), scale(new BigDecimal(173.6)));}});
    public static final User USER_WITH_NULLABLE_FIELDS = new User("name", "", "", null, null, null, null, null,
            Collections.emptyMap());
    public static final User UPDATED_USER = new User(START_SEQ + 5, "Катерина", "Руденко", "Дніпропетровськ, Орловська 13",
            "kate2000@gmail.com", "+380632212612", LocalDate.of(1999, Month.DECEMBER, 12), BigDecimal.valueOf(9.8),
            new HashMap(){{put(SUBJECT1.getId(), scale(new BigDecimal(190.6))); put(SUBJECT2.getId(), scale(new BigDecimal(184.22)));
                put(SUBJECT3.getId(), scale(new BigDecimal(182.5)));}});

    public static final University UNIVERSITY1 = new University(START_SEQ + 7,
            "Київський національний університет ім. Тараса Шевченка", "Київ", "вул. Володимирська, 60");
    public static final University UNIVERSITY2 = new University(START_SEQ + 8,
            "Національний університет \"Києво-Могилянська Академія\"", "Київ", "вул. Григорія Сковороди, 2");
    public static final University UNIVERSITY3 = new University(START_SEQ + 9,
            "Національний університет \"Львівська політехніка\"", "Львів", "вул. Степана Бандери, 12");
    public static final University NEW_UNIVERSITY = new University("Харківський національний університет внутрішніх справ",
            "Харків", "майдан Свободи, 4");
    public static final University UNIVERSITY_WITH_NULLABLE_FIELDS = new University(
            "Ще один відомо-невідомий університет", null, null);
    public static final University UPDATED_UNIVERSITY = new University(START_SEQ + 9,
            "Національний перейменований університет \"Львівська політехніка\"", "Львів", "вул. Степана Бандери, 13");

    public static final Faculty FACULTY1 = new Faculty(START_SEQ + 10, "Факультет інформаційних технологій",
            "сайт не працює", "fit_knu@ukr.net", "вул. Ванди Василевської, 24", UNIVERSITY1.getId());
    public static final Faculty FACULTY2 = new Faculty(START_SEQ + 11, "Факультет кібернетики",
            "+38044-521-3554", "ava@unicyb.kiev.ua", "просп. Академіка Глушкова, 4д", UNIVERSITY1.getId());
    public static final Faculty FACULTY3 = new Faculty(START_SEQ + 12, "Факультет гуманітарних наук",
            "+38044-425-5188", "vakuliuk@ukma.edu.ua", "вулиця Григорія Сковороди, 2", UNIVERSITY2.getId());
    public static final Faculty FACULTY4 = new Faculty(START_SEQ + 13, "Факультет інформатики",
            "+38044-463-6985", "gor@ukma.kiev.ua", "вулиця Григорія Сковороди, 2", UNIVERSITY2.getId());
    public static final Faculty NEW_FACULTY = new Faculty("Факультет Географії та Психології",
            "+38044-521-3270", "psy-univ@ukr.net", "проспект Академіка Глушкова, 2", UNIVERSITY1.getId());
    public static final Faculty FACULTY_WITH_INCORRECT_UNIVERSITY = new Faculty("Інститут архітектури",
            "+38032-258-22-39", "kornel@polynet.lviv.ua", "вул. С. Бандери 12, Головний корпус", INCORRECT_ID);
    public static final Faculty UPDATED_FACULTY = new Faculty(START_SEQ + 10, "Факультет інформаційних технологій",
            "сайт не працює", "fit_knu@ukr.net", "вул. Ванди Василевської, 24", UNIVERSITY1.getId());

    public static final Direction DIRECTION1 = new Direction(START_SEQ + 14, "Інформатика", scale(new BigDecimal(0.1)),
            30, FACULTY1.getId(), new HashMap(){{put(SUBJECT1.getId(), scale(new BigDecimal(0.4)));
        put(SUBJECT2.getId(), scale(new BigDecimal(0.3))); put(SUBJECT3.getId(), scale(new BigDecimal(0.2)));}});
    public static final Direction DIRECTION2 = new Direction(START_SEQ + 15, "Комп`ютерні науки", scale(new BigDecimal(0.05)),
            45, FACULTY1.getId(), new HashMap(){{put(SUBJECT1.getId(), scale(new BigDecimal(0.4))); put(SUBJECT2.getId(),
            scale(new BigDecimal(0.3))); put(SUBJECT3.getId(), scale(new BigDecimal(0.25)));}});
    public static final Direction DIRECTION3 = new Direction(START_SEQ + 16, "Безпека інформаційних систем",
            scale(new BigDecimal(0.1)), 20, FACULTY1.getId(), Collections.emptyMap());
    public static final Direction DIRECTION4 = new Direction(START_SEQ + 17, "Прикладна математика",
            scale(new BigDecimal(0.05)), 40, FACULTY2.getId(), Collections.emptyMap());
    public static final Direction DIRECTION5 = new Direction(START_SEQ + 18, "Програмне забезпечення автоматизованих систем",
            scale(new BigDecimal(0.05)), 25, FACULTY2.getId(), Collections.emptyMap());
    public static final Direction NEW_DIRECTION = new Direction("Системний аналіз", scale(new BigDecimal(0.15)), 35,
            FACULTY2.getId(), new HashMap(){{put(SUBJECT1.getId(), scale(new BigDecimal(0.4))); put(SUBJECT2.getId(),
            scale(new BigDecimal(0.2))); put(SUBJECT3.getId(), scale(new BigDecimal(0.25)));}});
    public static final Direction DIRECTION_WITH_INCORRECT_OWNER = new Direction("Програмна інженерія",
            scale(new BigDecimal(0.1)), 35, INCORRECT_ID, Collections.emptyMap());
    public static final Direction UPDATED_DIRECTION = new Direction(START_SEQ + 15, "Комп`ютерні науки",
            scale(new BigDecimal(0.1)), 35, FACULTY1.getId(), new HashMap(){{put(SUBJECT1.getId(), scale(new BigDecimal(0.4)));
            put(SUBJECT2.getId(), scale(new BigDecimal(0.3))); put(SUBJECT3.getId(), scale(new BigDecimal(0.25)));}});

    public static final Application APPLICATION1 = new Application(START_SEQ + 19, USER1.getId(), DIRECTION2.getId(),
            LocalDateTime.of(2016, 11, 29, 12, 15, 55), Status.APPROVED, scale(new BigDecimal(192.3)));
    public static final Application APPLICATION2 = new Application(START_SEQ + 20, USER1.getId(), DIRECTION1.getId(),
            LocalDateTime.of(2016, 11, 30, 20, 49, 30), Status.CREATED, scale(new BigDecimal(180.5)));
    public static final Application APPLICATION3 = new Application(START_SEQ + 21, USER1.getId(), DIRECTION3.getId(),
            LocalDateTime.of(2016, 11, 30, 18, 1, 45), Status.REJECTED, scale(new BigDecimal(178.2)));
    public static final Application NEW_APPLICATION = new Application(USER1.getId(), DIRECTION4.getId(),
            LocalDateTime.now(), scale(new BigDecimal(123.4)));
    public static final Application DUPLICATED_APPLICATION = new Application(USER1.getId(), DIRECTION2.getId(),
            LocalDateTime.now(), scale(new BigDecimal(192.3)));
    public static final Application UPDATED_APPLICATION = new Application(START_SEQ + 20, USER1.getId(), DIRECTION1.getId(),
            LocalDateTime.of(2016, 11, 30, 20, 49, 30), Status.APPROVED, scale(new BigDecimal(180.5)));

    public static final String ADMIN_LOGIN = "admin";
    public static final String ADMIN_PASSWORD = "a1D2m3I4n4I2s2Ch1E";
    public static final String USER_LOGIN = "dimasik";
    public static final String USER_PASSWORD = "dim_dim";

    public static final Comparator<NamedEntity> COMPARATOR = new Comparator<NamedEntity>() {
        @Override
        public int compare(NamedEntity o1, NamedEntity o2) {
            return o1.getId().compareTo(o2.getId());
        }
    };

    private static BigDecimal scale(BigDecimal in) {
        return in.setScale(2, BigDecimal.ROUND_HALF_UP);
    }
}
