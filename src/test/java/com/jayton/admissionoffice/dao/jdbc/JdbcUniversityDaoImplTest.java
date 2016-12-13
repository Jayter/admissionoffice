package com.jayton.admissionoffice.dao.jdbc;

import com.jayton.admissionoffice.dao.FactoryProducer;
import com.jayton.admissionoffice.dao.UniversityDao;
import com.jayton.admissionoffice.dao.exception.DAOException;
import com.jayton.admissionoffice.model.university.University;
import com.jayton.admissionoffice.util.InitHelper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.jayton.admissionoffice.data.TestData.*;

public class JdbcUniversityDaoImplTest {

    private UniversityDao universityDao = FactoryProducer.getInstance().getPostgresDaoFactory().getUniversityDao();

    @Rule
    public ExpectedException expected = ExpectedException.none();

    @Before
    public void setUpDb() throws Exception {
        InitHelper.executeDbPopulate("populateTestDb.sql");
    }

    @Test
    public void getTest() throws Exception {
        Assert.assertEquals(UNIVERSITY1, universityDao.get(UNIVERSITY1.getId()));

        Assert.assertNull(universityDao.get(INCORRECT_ID));
    }

    @Test
    public void getByCityTest() throws Exception {
        List<University> list = universityDao.getByCity(KYIV);

        Assert.assertEquals(list, Arrays.asList(UNIVERSITY1, UNIVERSITY2));

        //if call with name that does not exist, receive an empty list, not null
        Assert.assertEquals(universityDao.getByCity(INCORRECT_STRING), Collections.emptyList());
    }

    @Test
    public void getAllTest() throws Exception {
        List<University> all = universityDao.getAll();

        Assert.assertEquals(Arrays.asList(UNIVERSITY1, UNIVERSITY2, UNIVERSITY3), all);
    }

    @Test
    public void addTest() throws Exception {
        Assert.assertEquals(NEW_ID, universityDao.add(NEW_UNIVERSITY).getId());

        Assert.assertEquals(Arrays.asList(UNIVERSITY1, UNIVERSITY2, UNIVERSITY3, NEW_UNIVERSITY),
                universityDao.getAll());

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

        Assert.assertEquals(Arrays.asList(UNIVERSITY1, UNIVERSITY2), universityDao.getAll());

        expected.expect(DAOException.class);
        universityDao.delete(INCORRECT_ID);
    }
}