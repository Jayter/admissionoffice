package com.jayton.admissionoffice.dao.jdbc;

import com.jayton.admissionoffice.dao.exception.DAOException;
import com.jayton.admissionoffice.model.to.ExamResult;
import com.jayton.admissionoffice.model.user.Enrollee;
import com.jayton.admissionoffice.util.InitHelper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.jayton.admissionoffice.data.CommonTestData.*;
import static com.jayton.admissionoffice.data.EnrolleeTestData.*;

/**
 * Created by Jayton on 27.11.2016.
 */
public class JdbcEnrolleeDaoImplTest {

    private JdbcEnrolleeDaoImpl jdbcEnrolleeDao = JdbcEnrolleeDaoImpl.getInstance();

    @Rule
    public ExpectedException expected = ExpectedException.none();

    @Before
    public void setUpDb() throws SQLException {
        InitHelper.executeDbPopulate("populateByEnrollees.sql");
    }


    @Test
    public void get() throws Exception {
        Enrollee enrollee = jdbcEnrolleeDao.get(START_SEQUENCE);

        Assert.assertEquals(enrollee, ENROLLEE1);

        Assert.assertNull(jdbcEnrolleeDao.get(INCORRECT_ID));
    }

    @Test
    public void getAll() throws Exception {
        List<Enrollee> actual = jdbcEnrolleeDao.getAll();

        Assert.assertEquals(Arrays.asList(ENROLLEE1, ENROLLEE2, ENROLLEE3), actual);
    }

    @Test
    public void add() throws Exception {
        jdbcEnrolleeDao.add(NEW_ENROLLEE);

        Assert.assertEquals(new Long(START_SEQUENCE + 7), NEW_ENROLLEE.getId());
        Assert.assertEquals(Arrays.asList(ENROLLEE1, ENROLLEE2, ENROLLEE3, NEW_ENROLLEE), jdbcEnrolleeDao.getAll());

        expected.expect(DAOException.class);
        jdbcEnrolleeDao.add(ENROLLEE_WITH_NULLABLE_FIELDS);
    }

    @Test
    public void update() throws Exception {
        ENROLLEE2.setAddress("Дніпропетровськ, Орловська 13");
        ENROLLEE2.setBirthDate(LocalDate.of(1995, Month.DECEMBER, 12));
        jdbcEnrolleeDao.update(ENROLLEE2);

        Assert.assertEquals(ENROLLEE2, jdbcEnrolleeDao.get(ENROLLEE2.getId()));

        //reset to initial state
        ENROLLEE2.setAddress("Умань, Європейська 12");
        ENROLLEE2.setBirthDate(LocalDate.of(1999, Month.DECEMBER, 11));

        //updating user that was not saved in db
        expected.expect(DAOException.class);
        jdbcEnrolleeDao.update(NEW_ENROLLEE);
    }

    @Test
    public void delete() throws Exception {
        jdbcEnrolleeDao.delete(START_SEQUENCE);

        Assert.assertEquals(Arrays.asList(ENROLLEE2, ENROLLEE3), jdbcEnrolleeDao.getAll());

        //deleting user that was not saved in db
        expected.expect(DAOException.class);
        jdbcEnrolleeDao.delete(NEW_ENROLLEE);
    }

    @Test
    public void addResults() throws Exception {
        jdbcEnrolleeDao.addResults(NEW_RESULTS);

        Assert.assertEquals(jdbcEnrolleeDao.getResults(START_SEQUENCE + 1), NEW_RESULTS);
    }

    @Test
    public void deleteResults() throws Exception {
        jdbcEnrolleeDao.deleteResults(RESULT2);

        Assert.assertEquals(jdbcEnrolleeDao.getResults(START_SEQUENCE), Arrays.asList(RESULT1, RESULT3, RESULT4));
    }

    @Test
    public void updateResults() throws Exception {
        RESULT1.setMark(BigDecimal.TEN);

        jdbcEnrolleeDao.updateResults(RESULT1);

        List<ExamResult> all = jdbcEnrolleeDao.getResults(START_SEQUENCE);
        Assert.assertEquals(all.get(3), RESULT1);

        RESULT1.setMark(BigDecimal.valueOf(192.5));
    }

    @Test
    public void getResults() throws Exception {
        Assert.assertEquals(jdbcEnrolleeDao.getResults(START_SEQUENCE), ALL_RESULTS);

        Assert.assertEquals(jdbcEnrolleeDao.getResults(INCORRECT_ID), Collections.emptyList());
    }

}