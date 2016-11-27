package com.jayton.admissionoffice.data;

import com.jayton.admissionoffice.model.university.University;

/**
 * Created by Jayton on 26.11.2016.
 */
public class JdbcUniversityTestData {
    public static final Long UNIVERSITY1_ID = 10000L;
    public static final String CITY = "Київ";

    public static final University UNIVERSITY1 = new University(UNIVERSITY1_ID,
            "Київський національний університет ім. Тараса Шевченка", "Київ", "вул. Володимирська, 60");
    public static final University UNIVERSITY2 = new University(UNIVERSITY1_ID + 1,
            "Національний університет \"Києво-Могилянська Академія\"", "Київ", "вул. Григорія Сковороди, 2");
    public static final University UNIVERSITY3 = new University(UNIVERSITY1_ID + 2,
            "Національний університет \"Львівська політехніка\"", "Львів", "вул. Степана Бандери, 12");
    public static final University NEW_UNIVERSITY = new University("Харківський національний університет внутрішніх справ",
            "Харків", "майдан Свободи, 4");
    public static final University UNIVERSITY_WITH_NULLABLE_FIELDS = new University(
            "Ще один відомо-невідомий університет", null, null);
}
