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

import static com.jayton.admissionoffice.data.CommonTestData.INCORRECT_ID;
import static com.jayton.admissionoffice.data.FacultyTestData.*;
import static com.jayton.admissionoffice.data.JdbcUniversityTestData.UNIVERSITY2;

/**
 * Created by Jayton on 27.11.2016.
 */
public class JdbcFacultyDaoImplTest {

    private JdbcFacultyDaoImpl jdbcFacultyDao = JdbcFacultyDaoImpl.getInstance();

    @Rule
    public ExpectedException expected = ExpectedException.none();

    @Before
    public void setUpDb() throws Exception {
        InitHelper.executeDbPopulate("populateByFaculties.sql");
    }
    @Test
    public void get() throws Exception {
        Assert.assertEquals(jdbcFacultyDao.get(FACULTY1_ID), FACULTY1);

        Assert.assertNull(jdbcFacultyDao.get(INCORRECT_ID));
    }

    @Test
    public void getByUniversity() throws Exception {
        List<Faculty> faculties = jdbcFacultyDao.getByUniversity(UNIVERSITY2.getId());

        Assert.assertEquals(faculties, Arrays.asList(FACULTY3, FACULTY4));
        Assert.assertEquals(jdbcFacultyDao.getByUniversity(INCORRECT_ID), Collections.emptyList());
    }

    @Test
    public void getAll() throws Exception {
        List<Faculty> all = jdbcFacultyDao.getAll();

        Assert.assertEquals(all, Arrays.asList(FACULTY1, FACULTY2, FACULTY3, FACULTY4));
    }

    @Test
    public void add() throws Exception {
        jdbcFacultyDao.add(NEW_FACULTY);

        Assert.assertEquals(new Long(FACULTY1_ID + 4), NEW_FACULTY.getId());
        Assert.assertEquals(jdbcFacultyDao.getAll(), Arrays.asList(FACULTY1, FACULTY2, FACULTY3, FACULTY4, NEW_FACULTY));

        //university does not exist
        expected.expect(DAOException.class);
        jdbcFacultyDao.add(FACULTY_WITH_INCORRECT_UNIVERSITY);
    }

    @Test
    public void update() throws Exception {
        FACULTY1.setOfficeAddress("просп. Академіка Глушкова, 4д");
        jdbcFacultyDao.update(FACULTY1);

        Assert.assertEquals(FACULTY1, jdbcFacultyDao.get(FACULTY1.getId()));

        FACULTY1.setOfficeAddress("вул. Ванди Василевської, 24");

        expected.expect(DAOException.class);
        jdbcFacultyDao.update(FACULTY_WITH_INCORRECT_UNIVERSITY);
    }

    @Test
    public void delete() throws Exception {
        jdbcFacultyDao.delete(FACULTY3);

        Assert.assertEquals(jdbcFacultyDao.getAll(), Arrays.asList(FACULTY1, FACULTY2, FACULTY4));
    }

}