package com.jayton.admissionoffice.dao.jdbc;

import com.jayton.admissionoffice.dao.ApplicationDao;
import com.jayton.admissionoffice.dao.exception.DAOException;
import com.jayton.admissionoffice.data.ApplicationMatcher;
import com.jayton.admissionoffice.model.to.Application;
import com.jayton.admissionoffice.model.to.ApplicationDto;
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

import static com.jayton.admissionoffice.data.TestData.*;

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
        long id = applicationDao.add(NEW_APPLICATION);
        Assert.assertEquals(id, NEW_ID);

        Application added = new Application(id, NEW_APPLICATION.getUserId(), NEW_APPLICATION.getDirectionId(),
                NEW_APPLICATION.getCreationTime(), NEW_APPLICATION.getStatus(), NEW_APPLICATION.getMark());
        Assert.assertTrue(matcher.compare(added, applicationDao.get(id)));
    }

    @Test
    public void addDuplicatedTest() throws Exception {
        expected.expect(DAOException.class);
        applicationDao.add(DUPLICATED_APPLICATION);
    }

    @Test
    public void addWithNullableFieldsTest() throws Exception {
        expected.expect(DAOException.class);
        applicationDao.add(APPLICATION_WITH_NULLABLE_FIELDS);
    }

    @Test
    public void getTest() throws Exception {
        Application retrieved = applicationDao.get(APPLICATION3.getId());

        Assert.assertTrue(matcher.compare(retrieved, APPLICATION3));
    }

    @Test
    public void getByIncorrectIdTest() throws Exception {
        Assert.assertNull(applicationDao.get(INCORRECT_ID));
    }

    @Test
    public void updateTest() throws Exception {
        Assert.assertTrue(applicationDao.update(APPLICATION2.getId(), Status.APPROVED));

        Assert.assertTrue(matcher.compare(UPDATED_APPLICATION, applicationDao.get(APPLICATION2.getId())));
    }

    @Test
    public void updateByIncorrectIdTest() throws Exception {
        Assert.assertFalse(applicationDao.update(INCORRECT_ID, Status.APPROVED));
    }

    @Test
    public void deleteTest() throws Exception {
        Assert.assertTrue(applicationDao.delete(APPLICATION1.getId()));

        Assert.assertFalse(applicationDao.getByUser(USER1.getId()).contains(APPLICATION1));
    }

    @Test
    public void deleteByIncorrectIdTest() throws Exception {
        Assert.assertFalse(applicationDao.delete(INCORRECT_ID));
    }

    @Test
    public void getByUserTest() throws Exception {
        List<Application> list = Arrays.asList(APPLICATION1, APPLICATION3, APPLICATION2);
        Assert.assertTrue(matcher.compareLists(list, applicationDao.getByUser(USER1.getId())));
    }

    @Test
    public void getByUserByIncorrectId() throws Exception {
        Assert.assertEquals(Collections.emptyList(), applicationDao.getByUser(INCORRECT_ID));
    }

    @Test
    public void getByDirectionTest() throws Exception {
        ApplicationDto dto = applicationDao.getByDirection(DIRECTION1.getId(), 0, 5);

        Assert.assertTrue(matcher.compareLists(Collections.singletonList(APPLICATION2), dto.getApplications()));
        Assert.assertEquals(dto.getCount(), 1);
    }

    @Test
    public void getByDirectionByIncorrectIdTest() throws Exception {
        Assert.assertEquals(Collections.emptyList(), applicationDao.getByDirection(INCORRECT_ID, 0, 5).getApplications());
    }


    @Test
    public void getAllTest() throws Exception {
        List<Application> list = Arrays.asList(APPLICATION1, APPLICATION3, APPLICATION2);

        Assert.assertTrue(matcher.compareLists(list, applicationDao.getAll()));
    }

    @Test
    public void updateAllTest() throws Exception {
        List<Application> list = Arrays.asList(APPLICATION1, APPLICATION3, APPLICATION2);
        Assert.assertTrue(applicationDao.updateAll(list, Status.APPROVED));

        for(Application application: applicationDao.getAll()) {
            Assert.assertEquals(application.getStatus(), Status.APPROVED);
        }
    }

    @Test
    public void updateAllWithIncorrectTest() throws Exception {
        List<Application> list = Collections.singletonList(NEW_APPLICATION);
        Assert.assertFalse(applicationDao.updateAll(list, Status.APPROVED));
    }
}