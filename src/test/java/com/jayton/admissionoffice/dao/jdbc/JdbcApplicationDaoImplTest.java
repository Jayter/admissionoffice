package com.jayton.admissionoffice.dao.jdbc;

import com.jayton.admissionoffice.dao.ApplicationDao;
import com.jayton.admissionoffice.dao.exception.DAOException;
import com.jayton.admissionoffice.dao.data.ApplicationMatcher;
import com.jayton.admissionoffice.model.to.Application;
import com.jayton.admissionoffice.model.to.Status;
import com.jayton.admissionoffice.util.di.BeanContextHolder;
import com.jayton.admissionoffice.util.di.exception.InjectionException;
import org.junit.*;
import util.ContextInitializationHelper;
import util.DbInitializationHelper;
import org.junit.rules.ExpectedException;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.jayton.admissionoffice.dao.data.TestData.*;

public class JdbcApplicationDaoImplTest {

    private ApplicationDao applicationDao = (ApplicationDao)
            BeanContextHolder.getInstance().getActualContext().getBean("applicationDao");

    private ApplicationMatcher matcher = new ApplicationMatcher();

    @Rule
    public ExpectedException expected = ExpectedException.none();

    @BeforeClass
    public static void initContext() throws InjectionException {
        ContextInitializationHelper helper = ContextInitializationHelper.getInstance();
        helper.initContext("di/dependencies.xml");
    }

    @Before
    public void setUpDb() throws Exception {
        DbInitializationHelper.getInstance().executeDbPopulate("populateForDaoTest.sql");
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
                applicationDao.getByDirection(DIRECTION1.getId(), 0, 5).getApplications()));

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