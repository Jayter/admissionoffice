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

import static com.jayton.admissionoffice.data.TestData.*;

/**
 * Created by Jayton on 26.11.2016.
 */
public class JdbcUniversityDaoImplTest {
    private JdbcUniversityDaoImpl jdbcUniversityDao = JdbcUniversityDaoImpl.getInstance();

    @Rule
    public ExpectedException expected = ExpectedException.none();

    @Before
    public void setUpDb() throws Exception {
        InitHelper.executeDbPopulate("populateTestDb.sql");
    }

    @Test
    public void get() throws Exception {
        Assert.assertEquals(UNIVERSITY1, jdbcUniversityDao.get(UNIVERSITY1.getId()));

        Assert.assertNull(jdbcUniversityDao.get(INCORRECT_ID));
    }

    @Test
    public void getByCity() throws Exception {
        List<University> list = jdbcUniversityDao.getByCity(KYIV);

        Assert.assertEquals(list, Arrays.asList(UNIVERSITY1, UNIVERSITY2));

        //if call with name that does not exist, receive an empty list, not null
        Assert.assertEquals(jdbcUniversityDao.getByCity(INCORRECT_STRING), Collections.emptyList());
    }

    @Test
    public void getAll() throws Exception {
        List<University> all = jdbcUniversityDao.getAll();

        Assert.assertEquals(Arrays.asList(UNIVERSITY1, UNIVERSITY2, UNIVERSITY3), all);
    }

    @Test
    public void add() throws Exception {
        Assert.assertEquals(NEW_ID, jdbcUniversityDao.add(NEW_UNIVERSITY));

        Assert.assertEquals(Arrays.asList(UNIVERSITY1, UNIVERSITY2, UNIVERSITY3, NEW_UNIVERSITY),
                jdbcUniversityDao.getAll());

        expected.expect(DAOException.class);
        jdbcUniversityDao.add(UNIVERSITY_WITH_NULLABLE_FIELDS);
    }

    @Test
    public void update() throws Exception {
        jdbcUniversityDao.update(UPDATED_UNIVERSITY);

        Assert.assertEquals(UPDATED_UNIVERSITY, jdbcUniversityDao.get(UNIVERSITY3.getId()));

        //incorrect or nullable id
        expected.expect(DAOException.class);
        jdbcUniversityDao.update(NEW_UNIVERSITY);
    }

    @Test
    public void delete() throws Exception {
        Assert.assertTrue(jdbcUniversityDao.delete(UNIVERSITY3.getId()));

        Assert.assertEquals(Arrays.asList(UNIVERSITY1, UNIVERSITY2), jdbcUniversityDao.getAll());

        Assert.assertFalse(jdbcUniversityDao.delete(INCORRECT_ID));
    }

}