package com.jayton.admissionoffice.data;

import com.jayton.admissionoffice.model.to.EntranceSubject;
import com.jayton.admissionoffice.model.university.Direction;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.jayton.admissionoffice.data.CommonTestData.INCORRECT_ID;
import static com.jayton.admissionoffice.data.FacultyTestData.FACULTY1_ID;

/**
 * Created by Jayton on 27.11.2016.
 */
public class DirectionTestData {
    public static final Long DIRECTION1_ID = 10007L;
    public static final Direction DIRECTION1 = new Direction(DIRECTION1_ID, "Інформатика",
            new BigDecimal(0.1).setScale(2, BigDecimal.ROUND_HALF_UP), 30, FACULTY1_ID);
    public static final Direction DIRECTION2 = new Direction(DIRECTION1_ID + 1, "Комп`ютерні науки",
            new BigDecimal(0.1).setScale(2, BigDecimal.ROUND_HALF_UP), 45, FACULTY1_ID);
    public static final Direction DIRECTION3 = new Direction(DIRECTION1_ID + 2, "Безпека інформаційних систем",
            new BigDecimal(0.1).setScale(2, BigDecimal.ROUND_HALF_UP), 20, FACULTY1_ID);
    public static final Direction DIRECTION4 = new Direction(DIRECTION1_ID + 3, "Прикладна математика",
            new BigDecimal(0.05).setScale(2, BigDecimal.ROUND_HALF_UP), 40, FACULTY1_ID + 3);
    public static final Direction DIRECTION5 = new Direction(DIRECTION1_ID + 4, "Програмне забезпечення автоматизованих систем",
            new BigDecimal(0.05).setScale(2, BigDecimal.ROUND_HALF_UP), 25, FACULTY1_ID + 3);
    public static final Direction NEW_DIRECTION = new Direction("Системний аналіз",
            new BigDecimal(0.15).setScale(2, BigDecimal.ROUND_HALF_UP), 35, FACULTY1_ID + 1);
    public static final Direction DIRECTION_WITH_INCORRECT_OWNER = new Direction("Програмна інженерія",
            new BigDecimal(0.1).setScale(2, BigDecimal.ROUND_HALF_UP), 35, INCORRECT_ID);

    public static final EntranceSubject ENTRANCE_SUBJECT1 = new EntranceSubject(DIRECTION1_ID, DIRECTION1_ID + 5,
            new BigDecimal(0.4).setScale(2, BigDecimal.ROUND_HALF_UP));
    public static final EntranceSubject ENTRANCE_SUBJECT2 = new EntranceSubject(DIRECTION1_ID, DIRECTION1_ID + 6,
            new BigDecimal(0.3).setScale(2, BigDecimal.ROUND_HALF_UP));
    public static final EntranceSubject ENTRANCE_SUBJECT3 = new EntranceSubject(DIRECTION1_ID, DIRECTION1_ID + 7,
            new BigDecimal(0.2).setScale(2, BigDecimal.ROUND_HALF_UP));

    public static final EntranceSubject ENTRANCE_SUBJECT4 = new EntranceSubject(DIRECTION1_ID + 1, DIRECTION1_ID + 5,
            new BigDecimal(0.4).setScale(2, BigDecimal.ROUND_HALF_UP));
    public static final EntranceSubject ENTRANCE_SUBJECT5 = new EntranceSubject(DIRECTION1_ID + 1, DIRECTION1_ID + 6,
            new BigDecimal(0.25).setScale(2, BigDecimal.ROUND_HALF_UP));
    public static final EntranceSubject ENTRANCE_SUBJECT6 = new EntranceSubject(DIRECTION1_ID + 1, DIRECTION1_ID + 7,
            new BigDecimal(0.5).setScale(2, BigDecimal.ROUND_HALF_UP));

    public static final List<EntranceSubject> ALL_SUBJECTS = Arrays.asList(ENTRANCE_SUBJECT1, ENTRANCE_SUBJECT2,
            ENTRANCE_SUBJECT3);
    public static final List<EntranceSubject> ALL_NEW_SUBJECTS = Arrays.asList(ENTRANCE_SUBJECT4, ENTRANCE_SUBJECT5,
            ENTRANCE_SUBJECT6);
}
