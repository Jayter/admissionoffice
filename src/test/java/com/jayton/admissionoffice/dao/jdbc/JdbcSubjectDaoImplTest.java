package com.jayton.admissionoffice.dao.jdbc;

import com.jayton.admissionoffice.dao.exception.DAOException;
import com.jayton.admissionoffice.util.InitHelper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Arrays;

import static com.jayton.admissionoffice.data.TestData.*;

/**
 * Created by Jayton on 26.11.2016.
 */
public class JdbcSubjectDaoImplTest {

    private JdbcSubjectDaoImpl jdbcSubjectDao = JdbcSubjectDaoImpl.getInstance();

    @Rule
    public ExpectedException expected = ExpectedException.none();

    @Before
    public void setUpDb() throws Exception {
        InitHelper.executeDbPopulate("populateTestDb.sql");
    }

    @Test
    public void get() throws Exception {
        Assert.assertEquals(SUBJECT1, jdbcSubjectDao.get(SUBJECT1.getId()));

        Assert.assertNull(jdbcSubjectDao.get(INCORRECT_ID));
    }

    @Test
    public void getAll() throws Exception {
        Assert.assertEquals(Arrays.asList(SUBJECT1, SUBJECT2, SUBJECT3, SUBJECT4), jdbcSubjectDao.getAll());
    }

    @Test
    public void getByName() throws Exception {
        Assert.assertEquals(SUBJECT2, jdbcSubjectDao.getByName(SUBJECT2.getName()));

        Assert.assertNull(jdbcSubjectDao.getByName(INCORRECT_STRING));
    }

    @Test
    public void add() throws Exception {
        Assert.assertEquals(NEW_ID, jdbcSubjectDao.add(NEW_SUBJECT));
        Assert.assertEquals(NEW_SUBJECT, jdbcSubjectDao.getByName(NEW_SUBJECT.getName()));

        //trying to add a subject with name that already exists
        expected.expect(DAOException.class);
        jdbcSubjectDao.add(DUPLICATED_SUBJECT);
    }

    @Test
    public void update() throws Exception {
        jdbcSubjectDao.update(UPDATED_SUBJECT);
        Assert.assertEquals(UPDATED_SUBJECT, jdbcSubjectDao.get(SUBJECT3.getId()));

        //trying to update a subject with name that already exists
        expected.expect(DAOException.class);
        jdbcSubjectDao.update(DUPLICATED_SUBJECT);
    }

    @Test
    public void delete() throws Exception {
        Assert.assertTrue(jdbcSubjectDao.delete(SUBJECT3.getId()));
        Assert.assertEquals(Arrays.asList(SUBJECT1, SUBJECT2, SUBJECT4), jdbcSubjectDao.getAll());

        Assert.assertFalse(jdbcSubjectDao.delete(INCORRECT_ID));
    }

}