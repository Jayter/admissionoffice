package com.jayton.admissionoffice.dao.jdbc;

import com.jayton.admissionoffice.dao.ApplicationDao;
import com.jayton.admissionoffice.dao.FactoryProducer;
import com.jayton.admissionoffice.dao.exception.DAOException;
import com.jayton.admissionoffice.model.to.Status;
import com.jayton.admissionoffice.util.InitHelper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Arrays;
import java.util.Collections;

import static com.jayton.admissionoffice.data.TestData.*;

/**
 * Created by Jayton on 30.11.2016.
 */
public class JdbcApplicationDaoImplTest {

    private ApplicationDao applicationDao = FactoryProducer.getInstance().getPostgresDaoFactory().getApplicationDao();

    @Rule
    public ExpectedException expected = ExpectedException.none();

    @Before
    public void setUpDb() throws Exception {
        InitHelper.executeDbPopulate("populateTestDb.sql");
    }

    @Test
    public void add() throws Exception {
        Assert.assertEquals(NEW_ID, applicationDao.add(NEW_APPLICATION));

        expected.expect(DAOException.class);
        applicationDao.add(DUPLICATED_APPLICATION);
    }

    @Test
    public void update() throws Exception {
        applicationDao.update(APPLICATION2.getId(), Status.APPROVED);

        Assert.assertEquals(UPDATED_APPLICATION, applicationDao.get(APPLICATION2.getId()));
    }

    @Test
    public void delete() throws Exception {
        Assert.assertTrue(applicationDao.delete(APPLICATION1.getId()));

        Assert.assertEquals(Arrays.asList(APPLICATION3, APPLICATION2),
                applicationDao.getByUser(USER1.getId()));

        Assert.assertFalse(applicationDao.delete(INCORRECT_ID));
    }

    @Test
    public void get() throws Exception {
        Assert.assertEquals(APPLICATION3, applicationDao.get(APPLICATION3.getId()));

        Assert.assertNull(applicationDao.get(INCORRECT_ID));

        Assert.assertEquals(Arrays.asList(APPLICATION1, APPLICATION3, APPLICATION2),
                applicationDao.getByUser(USER1.getId()));

        Assert.assertEquals(Collections.singletonList(APPLICATION2), applicationDao.getByDirection(DIRECTION1.getId()));

        Assert.assertEquals(Collections.emptyList(), applicationDao.getByUser(INCORRECT_ID));
    }

}