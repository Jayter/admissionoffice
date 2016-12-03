package com.jayton.admissionoffice.dao.jdbc;

import com.jayton.admissionoffice.dao.FactoryProducer;
import com.jayton.admissionoffice.dao.FacultyDao;
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

    private FacultyDao facultyDao = FactoryProducer.getInstance().getPostgresDaoFactory().getFacultyDao();

    @Rule
    public ExpectedException expected = ExpectedException.none();

    @Before
    public void setUpDb() throws Exception {
        InitHelper.executeDbPopulate("populateTestDb.sql");
    }

    @Test
    public void get() throws Exception {
        Assert.assertEquals(FACULTY1, facultyDao.get(FACULTY1.getId()));

        Assert.assertNull(facultyDao.get(INCORRECT_ID));
    }

    @Test
    public void getByUniversity() throws Exception {
        List<Faculty> faculties = facultyDao.getByUniversity(UNIVERSITY2.getId());

        Assert.assertEquals(Arrays.asList(FACULTY3, FACULTY4), faculties);

        Assert.assertEquals(facultyDao.getByUniversity(INCORRECT_ID), Collections.emptyList());
    }

    @Test
    public void getAll() throws Exception {
        List<Faculty> all = facultyDao.getAll();

        Assert.assertEquals(Arrays.asList(FACULTY1, FACULTY2, FACULTY3, FACULTY4), all);
    }

    @Test
    public void add() throws Exception {
        Assert.assertEquals(NEW_ID, facultyDao.add(NEW_FACULTY));
        Assert.assertEquals(Arrays.asList(FACULTY1, FACULTY2, FACULTY3, FACULTY4, NEW_FACULTY),
                facultyDao.getAll());

        //university does not exist
        expected.expect(DAOException.class);
        facultyDao.add(FACULTY_WITH_INCORRECT_UNIVERSITY);
    }

    @Test
    public void update() throws Exception {
        facultyDao.update(UPDATED_FACULTY);

        Assert.assertEquals(UPDATED_FACULTY, facultyDao.get(FACULTY1.getId()));

        expected.expect(DAOException.class);
        facultyDao.update(FACULTY_WITH_INCORRECT_UNIVERSITY);
    }

    @Test
    public void delete() throws Exception {
        Assert.assertTrue(facultyDao.delete(FACULTY3.getId()));

        Assert.assertEquals(facultyDao.getAll(), Arrays.asList(FACULTY1, FACULTY2, FACULTY4));

        Assert.assertFalse(facultyDao.delete(INCORRECT_ID));
    }

}