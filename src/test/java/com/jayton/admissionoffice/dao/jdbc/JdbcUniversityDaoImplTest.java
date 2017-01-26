package com.jayton.admissionoffice.dao.jdbc;

import com.jayton.admissionoffice.dao.UniversityDao;
import com.jayton.admissionoffice.dao.exception.DAOException;
import com.jayton.admissionoffice.model.university.University;
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

public class JdbcUniversityDaoImplTest {

    private UniversityDao universityDao = (UniversityDao)
            BeanContextHolder.getInstance().getActualContext().getBean("universityDao");

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
    public void getTest() throws Exception {
        Assert.assertEquals(UNIVERSITY1, universityDao.get(UNIVERSITY1.getId()));

        Assert.assertNull(universityDao.get(INCORRECT_ID));
    }

    @Test
    public void getByCityTest() throws Exception {
        List<University> list = universityDao.getByCity(KYIV, 0, 100);

        Assert.assertEquals(list, Arrays.asList(UNIVERSITY1, UNIVERSITY2));

        //if call with name that does not exist, receive an empty list, not null
        Assert.assertEquals(universityDao.getByCity(INCORRECT_STRING, 0, 100), Collections.emptyList());
    }

    @Test
    public void getAllTest() throws Exception {
        List<University> all = universityDao.getWithCount(0, 100).getEntries();

        Assert.assertEquals(Arrays.asList(UNIVERSITY1, UNIVERSITY2, UNIVERSITY3), all);
    }

    @Test
    public void addTest() throws Exception {
        Assert.assertEquals(NEW_ID, universityDao.add(NEW_UNIVERSITY).getId());

        Assert.assertEquals(Arrays.asList(UNIVERSITY1, UNIVERSITY2, UNIVERSITY3, NEW_UNIVERSITY),
                universityDao.getWithCount(0, 100).getEntries());

        expected.expect(DAOException.class);
        universityDao.add(UNIVERSITY_WITH_NULLABLE_FIELDS);
    }

    @Test
    public void updateTest() throws Exception {
        universityDao.update(UPDATED_UNIVERSITY);

        Assert.assertEquals(UPDATED_UNIVERSITY, universityDao.get(UNIVERSITY3.getId()));

        //incorrect or nullable id
        expected.expect(DAOException.class);
        universityDao.update(NEW_UNIVERSITY);
    }

    @Test
    public void deleteTest() throws Exception {
        universityDao.delete(UNIVERSITY3.getId());

        Assert.assertEquals(Arrays.asList(UNIVERSITY1, UNIVERSITY2), universityDao.getWithCount(0, 100).getEntries());

        expected.expect(DAOException.class);
        universityDao.delete(INCORRECT_ID);
    }
}