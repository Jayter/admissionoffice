package com.jayton.admissionoffice.data;

import com.jayton.admissionoffice.model.Subject;
import com.jayton.admissionoffice.model.to.EntranceSubject;
import com.jayton.admissionoffice.model.to.ExamResult;
import com.jayton.admissionoffice.model.university.Direction;
import com.jayton.admissionoffice.model.university.Faculty;
import com.jayton.admissionoffice.model.university.University;
import com.jayton.admissionoffice.model.user.User;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;

/**
 * Created by Jayton on 30.11.2016.
 */
public class TestData {
    public static final Long START_SEQ = 10000L;
    public static final Long INCORRECT_ID = 10L;
    public static final Long NEW_ID = 10019L;
    public static final String KYIV = "Київ";
    public static final String INCORRECT_NAME = "Some dummy value";

    public static final Subject SUBJECT1 = new Subject(START_SEQ, "Українська мова та література");
    public static final Subject SUBJECT2 = new Subject(START_SEQ + 1, "Англійська мова");
    public static final Subject SUBJECT3 = new Subject(START_SEQ + 2, "Математика");
    public static final Subject SUBJECT4 = new Subject(START_SEQ + 3, "Хімія");
    public static final Subject NEW_SUBJECT = new Subject("Алгебро-геометрія");
    public static final Subject DUPLICATED_SUBJECT = new Subject("Математика");
    public static final Subject UPDATED_SUBJECT = new Subject(START_SEQ + 2, "Математичний аналіз");

    public static final User USER1 = new User(START_SEQ + 4, "Дмитро", "Васильков", "Ужгород, Загорська 35",
            "vas_dim@gmail.com", "+380671823452", LocalDate.of(2000, Month.MARCH, 22), BigDecimal.valueOf(10.5));
    public static final User USER2 = new User(START_SEQ + 5, "Катерина", "Руденко", "Умань, Європейська 12",
            "kate2000@gmail.com", "+380632212612", LocalDate.of(1999, Month.DECEMBER, 11), BigDecimal.valueOf(9.8));
    public static final User USER3 = new User(START_SEQ + 6, "Максим", "Панченко", "Шостка, Сімейна 1",
            "pan_max@gmail.com", "+380952235616", LocalDate.of(2001, Month.JANUARY, 2), BigDecimal.valueOf(8.7));
    public static final User NEW_USER = new User("Анна", "Мохир", "Боярка, Шевченка 45", "annBo", "anna.moch@yandex.ru",
            "aNNa1q", "+380991329782", LocalDate.of(1990, Month.JULY, 7), BigDecimal.valueOf(12.0));
    public static final User USER_WITH_NULLABLE_FIELDS = new User("name", "", "", null, null, null, null, null, null);
    public static final User UPDATED_USER = new User(START_SEQ + 5, "Катерина", "Руденко", "Дніпропетровськ, Орловська 13",
            "kate2000@gmail.com", "+380632212612", LocalDate.of(1999, Month.DECEMBER, 12), BigDecimal.valueOf(9.8));


    public static final ExamResult RESULT1 = new ExamResult(USER1.getId(), SUBJECT1.getId(), scale(new BigDecimal(192.5)));
    public static final ExamResult RESULT2 = new ExamResult(USER1.getId(), SUBJECT2.getId(), scale(new BigDecimal(187.0)));
    public static final ExamResult RESULT3 = new ExamResult(USER1.getId(), SUBJECT3.getId(), scale(new BigDecimal(183.25)));
    public static final ExamResult RESULT4 = new ExamResult(USER1.getId(), SUBJECT4.getId(), scale(new BigDecimal(181.2)));
    public static final ExamResult RESULT5 = new ExamResult(USER2.getId(), SUBJECT1.getId(), scale(new BigDecimal(190.6)));
    public static final ExamResult RESULT6 = new ExamResult(USER2.getId(), SUBJECT2.getId(), scale(new BigDecimal(184.22)));
    public static final ExamResult RESULT7 = new ExamResult(USER2.getId(), SUBJECT3.getId(), scale(new BigDecimal(181.5)));
    public static final ExamResult RESULT8 = new ExamResult(USER2.getId(), SUBJECT4.getId(), scale(new BigDecimal(173.6)));
    public static final ExamResult UPDATED_RESULT = new ExamResult(USER1.getId(), SUBJECT2.getId(),
            scale(new BigDecimal(194.6)));

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

    public static final Direction DIRECTION1 = new Direction(START_SEQ + 14, "Інформатика",
            scale(new BigDecimal(0.1)), 30, FACULTY1.getId());
    public static final Direction DIRECTION2 = new Direction(START_SEQ + 15, "Комп`ютерні науки",
            scale(new BigDecimal(0.05)), 45, FACULTY1.getId());
    public static final Direction DIRECTION3 = new Direction(START_SEQ + 16, "Безпека інформаційних систем",
            scale(new BigDecimal(0.1)), 20, FACULTY1.getId());
    public static final Direction DIRECTION4 = new Direction(START_SEQ + 17, "Прикладна математика",
            scale(new BigDecimal(0.05)), 40, FACULTY2.getId());
    public static final Direction DIRECTION5 = new Direction(START_SEQ + 18, "Програмне забезпечення автоматизованих систем",
            scale(new BigDecimal(0.05)), 25, FACULTY2.getId());
    public static final Direction NEW_DIRECTION = new Direction("Системний аналіз",
            scale(new BigDecimal(0.15)), 35, FACULTY2.getId());
    public static final Direction DIRECTION_WITH_INCORRECT_OWNER = new Direction("Програмна інженерія",
            scale(new BigDecimal(0.1)), 35, INCORRECT_ID);
    public static final Direction UPDATED_DIRECTION = new Direction(START_SEQ + 16, "Безпека інформаційних систем",
            scale(new BigDecimal(0.1)), 25, FACULTY1.getId());

    public static final EntranceSubject ENTRANCE1 = new EntranceSubject(DIRECTION1.getId(), SUBJECT1.getId(),
            scale(new BigDecimal(0.4)));
    public static final EntranceSubject ENTRANCE2 = new EntranceSubject(DIRECTION1.getId(), SUBJECT2.getId(),
            scale(new BigDecimal(0.3)));
    public static final EntranceSubject ENTRANCE3 = new EntranceSubject(DIRECTION1.getId(), SUBJECT3.getId(),
            scale(new BigDecimal(0.2)));
    public static final EntranceSubject ENTRANCE4 = new EntranceSubject(DIRECTION2.getId(), SUBJECT1.getId(),
            scale(new BigDecimal(0.4)));
    public static final EntranceSubject ENTRANCE5 = new EntranceSubject(DIRECTION2.getId(), SUBJECT2.getId(),
            scale(new BigDecimal(0.3)));
    public static final EntranceSubject ENTRANCE6 = new EntranceSubject(DIRECTION2.getId(), SUBJECT3.getId(),
            scale(new BigDecimal(0.25)));

    private static BigDecimal scale(BigDecimal in) {
        return in.setScale(2, BigDecimal.ROUND_HALF_UP);
    }
}
