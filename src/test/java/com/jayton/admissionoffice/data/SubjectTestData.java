package com.jayton.admissionoffice.data;

import com.jayton.admissionoffice.model.Subject;

import static com.jayton.admissionoffice.data.CommonTestData.START_SEQUENCE;

/**
 * Created by Jayton on 26.11.2016.
 */
public class SubjectTestData {
    public static final Subject SUBJECT1 = new Subject(START_SEQUENCE, "Українська мова та література");
    public static final Subject SUBJECT2 = new Subject(START_SEQUENCE + 1, "Англійська мова");
    public static final Subject SUBJECT3 = new Subject(START_SEQUENCE + 2, "Математика");
    public static final Subject NEW_SUBJECT = new Subject("Хімія");
    public static final Subject DUPLICATED_SUBJECT = new Subject("Математика");
}
