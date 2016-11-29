package com.jayton.admissionoffice.data;

import com.jayton.admissionoffice.model.university.University;

import static com.jayton.admissionoffice.data.CommonTestData.START_SEQUENCE;

/**
 * Created by Jayton on 26.11.2016.
 */
public class JdbcUniversityTestData {
    public static final String CITY = "Київ";

    public static final University UNIVERSITY1 = new University(START_SEQUENCE,
            "Київський національний університет ім. Тараса Шевченка", "Київ", "вул. Володимирська, 60");
    public static final University UNIVERSITY2 = new University(START_SEQUENCE + 1,
            "Національний університет \"Києво-Могилянська Академія\"", "Київ", "вул. Григорія Сковороди, 2");
    public static final University UNIVERSITY3 = new University(START_SEQUENCE + 2,
            "Національний університет \"Львівська політехніка\"", "Львів", "вул. Степана Бандери, 12");
    public static final University NEW_UNIVERSITY = new University("Харківський національний університет внутрішніх справ",
            "Харків", "майдан Свободи, 4");
    public static final University UNIVERSITY_WITH_NULLABLE_FIELDS = new University(
            "Ще один відомо-невідомий університет", null, null);
}
