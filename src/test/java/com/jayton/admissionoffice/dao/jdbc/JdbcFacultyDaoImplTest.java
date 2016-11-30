package com.jayton.admissionoffice.dao.jdbc;

import com.jayton.admissionoffice.dao.exception.DAOException;
import com.jayton.admissionoffice.model.university.Faculty;
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
 * Created by Jayton on 27.11.2016.
 */
public class JdbcFacultyDaoImplTest {

    private JdbcFacultyDaoImpl jdbcFacultyDao = JdbcFacultyDaoImpl.getInstance();

    @Rule
    public ExpectedException expected = ExpectedException.none();

    @Before
    public void setUpDb() throws Exception {
        InitHelper.executeDbPopulate("populateTestDb.sql");
    }

    @Test
    public void get() throws Exception {
        Assert.assertEquals(FACULTY1, jdbcFacultyDao.get(FACULTY1.getId()));

        Assert.assertNull(jdbcFacultyDao.get(INCORRECT_ID));
    }

    @Test
    public void getByUniversity() throws Exception {
        List<Faculty> faculties = jdbcFacultyDao.getByUniversity(UNIVERSITY2.getId());

        Assert.assertEquals(Arrays.asList(FACULTY3, FACULTY4), faculties);

        Assert.assertEquals(jdbcFacultyDao.getByUniversity(INCORRECT_ID), Collections.emptyList());
    }

    @Test
    public void getAll() throws Exception {
        List<Faculty> all = jdbcFacultyDao.getAll();

        Assert.assertEquals(Arrays.asList(FACULTY1, FACULTY2, FACULTY3, FACULTY4), all);
    }

    @Test
    public void add() throws Exception {
        Assert.assertEquals(NEW_ID, jdbcFacultyDao.add(NEW_FACULTY));
        Assert.assertEquals(Arrays.asList(FACULTY1, FACULTY2, FACULTY3, FACULTY4, NEW_FACULTY),
                jdbcFacultyDao.getAll());

        //university does not exist
        expected.expect(DAOException.class);
        jdbcFacultyDao.add(FACULTY_WITH_INCORRECT_UNIVERSITY);
    }

    @Test
    public void update() throws Exception {
        jdbcFacultyDao.update(UPDATED_FACULTY);

        Assert.assertEquals(UPDATED_FACULTY, jdbcFacultyDao.get(FACULTY1.getId()));

        expected.expect(DAOException.class);
        jdbcFacultyDao.update(FACULTY_WITH_INCORRECT_UNIVERSITY);
    }

    @Test
    public void delete() throws Exception {
        Assert.assertTrue(jdbcFacultyDao.delete(FACULTY3.getId()));

        Assert.assertEquals(jdbcFacultyDao.getAll(), Arrays.asList(FACULTY1, FACULTY2, FACULTY4));

        Assert.assertFalse(jdbcFacultyDao.delete(INCORRECT_ID));
    }

}