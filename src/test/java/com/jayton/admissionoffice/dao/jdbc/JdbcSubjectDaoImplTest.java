package com.jayton.admissionoffice.dao.jdbc;

import com.jayton.admissionoffice.dao.FactoryProducer;
import com.jayton.admissionoffice.dao.SubjectDao;
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

    private SubjectDao subjectDao = FactoryProducer.getInstance().getPostgresDaoFactory().getSubjectDao();

    @Rule
    public ExpectedException expected = ExpectedException.none();

    @Before
    public void setUpDb() throws Exception {
        InitHelper.executeDbPopulate("populateTestDb.sql");
    }

    @Test
    public void get() throws Exception {
        Assert.assertEquals(SUBJECT1, subjectDao.get(SUBJECT1.getId()));

        Assert.assertNull(subjectDao.get(INCORRECT_ID));
    }

    @Test
    public void getAll() throws Exception {
        Assert.assertEquals(Arrays.asList(SUBJECT1, SUBJECT2, SUBJECT3, SUBJECT4), subjectDao.getAll());
    }

    @Test
    public void getByName() throws Exception {
        Assert.assertEquals(SUBJECT2, subjectDao.getByName(SUBJECT2.getName()));

        Assert.assertNull(subjectDao.getByName(INCORRECT_STRING));
    }

    @Test
    public void add() throws Exception {
        Assert.assertEquals(NEW_ID, subjectDao.add(NEW_SUBJECT));
        Assert.assertEquals(NEW_SUBJECT, subjectDao.getByName(NEW_SUBJECT.getName()));

        //trying to add a subject with name that already exists
        expected.expect(DAOException.class);
        subjectDao.add(DUPLICATED_SUBJECT);
    }

    @Test
    public void update() throws Exception {
        subjectDao.update(UPDATED_SUBJECT);
        Assert.assertEquals(UPDATED_SUBJECT, subjectDao.get(SUBJECT3.getId()));

        //trying to update a subject with name that already exists
        expected.expect(DAOException.class);
        subjectDao.update(DUPLICATED_SUBJECT);
    }

    @Test
    public void delete() throws Exception {
        Assert.assertTrue(subjectDao.delete(SUBJECT3.getId()));
        Assert.assertEquals(Arrays.asList(SUBJECT1, SUBJECT2, SUBJECT4), subjectDao.getAll());

        Assert.assertFalse(subjectDao.delete(INCORRECT_ID));
    }

}