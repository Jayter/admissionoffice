package com.jayton.admissionoffice.data;

import com.jayton.admissionoffice.model.Subject;

/**
 * Created by Jayton on 26.11.2016.
 */
public class SubjectTestData {
    public static final Long SUBJECT1_ID = 10000L;

    public static final Subject SUBJECT1 = new Subject(SUBJECT1_ID, "Українська мова та література");
    public static final Subject SUBJECT2 = new Subject(SUBJECT1_ID + 1, "Англійська мова");
    public static final Subject SUBJECT3 = new Subject(SUBJECT1_ID + 2, "Математика");
    public static final Subject NEW_SUBJECT = new Subject("Хімія");
    public static final Subject DUPLICATED_SUBJECT = new Subject("Математика");
}
