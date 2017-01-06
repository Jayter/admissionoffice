package com.jayton.admissionoffice.dao.jdbc;

import com.jayton.admissionoffice.dao.ApplicationDao;
import com.jayton.admissionoffice.dao.FactoryProducer;
import com.jayton.admissionoffice.dao.exception.DAOException;
import com.jayton.admissionoffice.dao.data.ApplicationMatcher;
import com.jayton.admissionoffice.model.to.Application;
import com.jayton.admissionoffice.model.to.Status;
import util.InitHelper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.jayton.admissionoffice.dao.data.TestData.*;

public class JdbcApplicationDaoImplTest {

    private ApplicationDao applicationDao = FactoryProducer.getInstance().getPostgresDaoFactory().getApplicationDao();

    private ApplicationMatcher matcher = new ApplicationMatcher();

    @Rule
    public ExpectedException expected = ExpectedException.none();

    @Before
    public void setUpDb() throws Exception {
        InitHelper.executeDbPopulate("populateForDaoTest.sql");
    }

    @Test
    public void addTest() throws Exception {
        Application created = applicationDao.add(NEW_APPLICATION);
        Assert.assertEquals(created, NEW_APPLICATION);

        expected.expect(DAOException.class);
        applicationDao.add(DUPLICATED_APPLICATION);
    }

    @Test
    public void updateTest() throws Exception {
        applicationDao.update(APPLICATION2.getId(), Status.APPROVED);

        Assert.assertEquals(UPDATED_APPLICATION, applicationDao.get(APPLICATION2.getId()));
    }

    @Test
    public void deleteTest() throws Exception {
        applicationDao.delete(APPLICATION1.getId());

        Assert.assertEquals(2, applicationDao.getByUser(USER1.getId()).size());

        expected.expect(DAOException.class);
        applicationDao.delete(INCORRECT_ID);
    }

    @Test
    public void getTest() throws Exception {
        Application retrieved = applicationDao.get(APPLICATION3.getId());
        Assert.assertTrue(matcher.equals(retrieved, APPLICATION3));

        Assert.assertNull(applicationDao.get(INCORRECT_ID));
    }

    @Test
    public void getByUserTest() throws Exception {
        List<Application> list = Arrays.asList(APPLICATION1, APPLICATION3, APPLICATION2);
        Assert.assertTrue(matcher.equals(list, applicationDao.getByUser(USER1.getId())));

        Assert.assertEquals(Collections.emptyList(), applicationDao.getByUser(INCORRECT_ID));
    }

    @Test
    public void getByDirectionTest() throws Exception {
        Assert.assertTrue(matcher.equals(Collections.singletonList(APPLICATION2),
                applicationDao.getByDirection(DIRECTION1.getId(), 0, 5)));

        Assert.assertEquals(Collections.emptyList(), applicationDao.getByUser(INCORRECT_ID));
    }

    @Test
    public void getAllTest() throws Exception {
        List<Application> list = Arrays.asList(APPLICATION1, APPLICATION3, APPLICATION2);
        Assert.assertTrue(matcher.equals(list, applicationDao.getAll()));
    }

    @Test
    public void updateAllTest() throws Exception {
        List<Application> list = Arrays.asList(APPLICATION1, APPLICATION3, APPLICATION2);
        applicationDao.updateAll(list, Status.APPROVED);

        for(Application application: applicationDao.getAll()) {
            Assert.assertEquals(application.getStatus(), Status.APPROVED);
        }
    }
}