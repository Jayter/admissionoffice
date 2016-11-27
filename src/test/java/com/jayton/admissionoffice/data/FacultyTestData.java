package com.jayton.admissionoffice.data;

import com.jayton.admissionoffice.model.university.Faculty;

import static com.jayton.admissionoffice.data.CommonTestData.INCORRECT_ID;
import static com.jayton.admissionoffice.data.JdbcUniversityTestData.UNIVERSITY1_ID;

/**
 * Created by Jayton on 27.11.2016.
 */
public class FacultyTestData {
    public static final Long FACULTY1_ID = 10003L;
    public static final Faculty FACULTY1 = new Faculty(FACULTY1_ID, "Факультет інформаційних технологій",
            "сайт не працює", "fit_knu@ukr.net", "вул. Ванди Василевської, 24", UNIVERSITY1_ID);
    public static final Faculty FACULTY2 = new Faculty(FACULTY1_ID + 1, "Факультет кібернетики",
            "+38044-521-3554", "ava@unicyb.kiev.ua", "просп. Академіка Глушкова, 4д", UNIVERSITY1_ID);
    public static final Faculty FACULTY3 = new Faculty(FACULTY1_ID + 2, "Факультет гуманітарних наук",
            "+38044-425-5188", "vakuliuk@ukma.edu.ua", "вулиця Григорія Сковороди, 2", UNIVERSITY1_ID + 1);
    public static final Faculty FACULTY4 = new Faculty(FACULTY1_ID + 3, "Факультет інформатики",
            "+38044-463-6985", "gor@ukma.kiev.ua", "вулиця Григорія Сковороди, 2", UNIVERSITY1_ID + 1);

    public static final Faculty NEW_FACULTY = new Faculty("Факультет Географії та Психології",
            "+38044-521-3270", "psy-univ@ukr.net", "проспект Академіка Глушкова, 2", UNIVERSITY1_ID);
    public static final Faculty FACULTY_WITH_INCORRECT_UNIVERSITY = new Faculty("Інститут архітектури",
            "+38032-258-22-39", "kornel@polynet.lviv.ua", "вул. С. Бандери 12, Головний корпус", INCORRECT_ID);

}
