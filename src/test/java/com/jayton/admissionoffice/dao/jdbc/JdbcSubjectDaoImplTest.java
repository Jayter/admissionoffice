package com.jayton.admissionoffice.dao.jdbc;

import com.jayton.admissionoffice.dao.exception.DAOException;
import com.jayton.admissionoffice.util.InitHelper;
import com.sun.org.apache.bcel.internal.generic.NEW;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Arrays;
import java.util.Collections;

import static com.jayton.admissionoffice.data.CommonTestData.*;
import static com.jayton.admissionoffice.data.SubjectTestData.*;

/**
 * Created by Jayton on 26.11.2016.
 */
public class JdbcSubjectDaoImplTest {

    private JdbcSubjectDaoImpl jdbcSubjectDao = JdbcSubjectDaoImpl.getInstance();

    @Rule
    public ExpectedException expected = ExpectedException.none();

    @Before
    public void setUpDb() throws Exception {
        InitHelper.executeDbPopulate("populateBySubjects.sql");
    }

    @Test
    public void get() throws Exception {
        Assert.assertEquals(SUBJECT1, jdbcSubjectDao.get(SUBJECT1_ID));

        Assert.assertNull(jdbcSubjectDao.get(INCORRECT_ID));
    }

    @Test
    public void getAll() throws Exception {
        Assert.assertEquals(Arrays.asList(SUBJECT1, SUBJECT2, SUBJECT3), jdbcSubjectDao.getAll());
    }

    @Test
    public void getByName() throws Exception {
        Assert.assertEquals(SUBJECT2, jdbcSubjectDao.getByName(SUBJECT2.getName()));

        Assert.assertNull(jdbcSubjectDao.getByName(INCORRECT_NAME));
    }

    @Test
    public void add() throws Exception {
        jdbcSubjectDao.add(NEW_SUBJECT);

        Assert.assertEquals(SUBJECT1_ID + 3, NEW_SUBJECT.getId());
        Assert.assertEquals(NEW_SUBJECT, jdbcSubjectDao.getByName(NEW_SUBJECT.getName()));

        //trying to add a subject with name that already exists
        expected.expect(DAOException.class);
        jdbcSubjectDao.add(DUPLICATED_SUBJECT);
    }

    @Test
    public void update() throws Exception {
        SUBJECT3.setName("Алгебра та Геометрія");

        jdbcSubjectDao.update(SUBJECT3);
        Assert.assertEquals(SUBJECT3, jdbcSubjectDao.get(SUBJECT3.getId()));

        //trying to update a subject with name that already exists
        SUBJECT3.setName("Українська мова та література");
        expected.expect(DAOException.class);
        jdbcSubjectDao.update(SUBJECT3);

        //restore initial state
        SUBJECT3.setName("Українська мова та література");
    }

    @Test
    public void delete() throws Exception {
        jdbcSubjectDao.delete(SUBJECT3.getId());
        Assert.assertEquals(Arrays.asList(SUBJECT1, SUBJECT2), jdbcSubjectDao.getAll());

        jdbcSubjectDao.delete(SUBJECT2);
        Assert.assertEquals(Collections.singletonList(SUBJECT1), jdbcSubjectDao.getAll());

        expected.expect(DAOException.class);
        jdbcSubjectDao.delete(INCORRECT_ID);
    }

}