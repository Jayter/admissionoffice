package com.jayton.admissionoffice.dao.jdbc;

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

import static com.jayton.admissionoffice.data.CommonTestData.INCORRECT_ID;
import static com.jayton.admissionoffice.data.CommonTestData.INCORRECT_NAME;
import static com.jayton.admissionoffice.data.JdbcUniversityTestData.*;

/**
 * Created by Jayton on 26.11.2016.
 */
public class JdbcUniversityDaoImplTest {
    private JdbcUniversityDaoImpl jdbcUniversityDao = JdbcUniversityDaoImpl.getInstance();

    @Rule
    public ExpectedException expected = ExpectedException.none();

    @Before
    public void setUpDb() throws Exception {
        InitHelper.executeDbPopulate("populateByUniversities.sql");
    }

    @Test
    public void get() throws Exception {
        Assert.assertEquals(UNIVERSITY1, jdbcUniversityDao.get(UNIVERSITY1_ID));

        Assert.assertNull(jdbcUniversityDao.get(INCORRECT_ID));
    }

    @Test
    public void getByCity() throws Exception {
        List<University> list = jdbcUniversityDao.getByCity(CITY);

        Assert.assertEquals(list, Arrays.asList(UNIVERSITY1, UNIVERSITY2));

        //if call with name that does not exist, receive an empty list, not null
        Assert.assertEquals(jdbcUniversityDao.getByCity(INCORRECT_NAME), Collections.emptyList());
    }

    @Test
    public void getAll() throws Exception {
        List<University> all = jdbcUniversityDao.getAll();

        Assert.assertEquals(all, Arrays.asList(UNIVERSITY1, UNIVERSITY2, UNIVERSITY3));
    }

    @Test
    public void add() throws Exception {
        jdbcUniversityDao.add(NEW_UNIVERSITY);

        Assert.assertEquals(new Long(UNIVERSITY1_ID + 3), NEW_UNIVERSITY.getId());

        Assert.assertEquals(jdbcUniversityDao.getAll(), Arrays.asList(UNIVERSITY1, UNIVERSITY2, UNIVERSITY3, NEW_UNIVERSITY));

        expected.expect(DAOException.class);
        jdbcUniversityDao.add(UNIVERSITY_WITH_NULLABLE_FIELDS);
    }

    @Test
    public void update() throws Exception {
        UNIVERSITY2.setAddress("вул. Григорія Сковороди, 10");

        jdbcUniversityDao.update(UNIVERSITY2);

        Assert.assertEquals(jdbcUniversityDao.get(UNIVERSITY2.getId()), UNIVERSITY2);

        //restore initial state
        UNIVERSITY2.setAddress("вул. Григорія Сковороди, 2");

        expected.expect(DAOException.class);
        jdbcUniversityDao.update(NEW_UNIVERSITY);
    }

    @Test
    public void delete() throws Exception {
        jdbcUniversityDao.delete(UNIVERSITY3.getId());

        Assert.assertEquals(jdbcUniversityDao.getAll(), Arrays.asList(UNIVERSITY1, UNIVERSITY2));

        expected.expect(DAOException.class);
        jdbcUniversityDao.delete(INCORRECT_ID);
    }

}