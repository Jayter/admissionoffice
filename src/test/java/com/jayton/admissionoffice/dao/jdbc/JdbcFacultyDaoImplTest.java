package com.jayton.admissionoffice.dao.jdbc;

import com.jayton.admissionoffice.dao.FactoryProducer;
import com.jayton.admissionoffice.dao.FacultyDao;
import com.jayton.admissionoffice.dao.exception.DAOException;
import com.jayton.admissionoffice.model.university.Faculty;
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

public class JdbcFacultyDaoImplTest {

    private FacultyDao facultyDao = FactoryProducer.getInstance().getPostgresDaoFactory().getFacultyDao();

    @Rule
    public ExpectedException expected = ExpectedException.none();

    @Before
    public void setUpDb() throws Exception {
        InitHelper.executeDbPopulate("populateForDaoTest.sql");
    }

    @Test
    public void getTest() throws Exception {
        Assert.assertEquals(FACULTY1, facultyDao.get(FACULTY1.getId()));

        Assert.assertNull(facultyDao.get(INCORRECT_ID));
    }

    @Test
    public void getByUniversityTest() throws Exception {
        List<Faculty> faculties = facultyDao.getByUniversity(UNIVERSITY2.getId(), 0, 100);

        Assert.assertEquals(Arrays.asList(FACULTY3, FACULTY4), faculties);

        Assert.assertEquals(facultyDao.getByUniversity(INCORRECT_ID, 0, 100), Collections.emptyList());
    }

    @Test
    public void getAllTest() throws Exception {
        List<Faculty> all = facultyDao.getAll();

        Assert.assertEquals(Arrays.asList(FACULTY1, FACULTY2, FACULTY3, FACULTY4), all);
    }

    @Test
    public void addTest() throws Exception {
        Assert.assertEquals(NEW_ID, facultyDao.add(NEW_FACULTY).getId());
        Assert.assertEquals(Arrays.asList(FACULTY1, FACULTY2, FACULTY3, FACULTY4, NEW_FACULTY),
                facultyDao.getAll());

        //university does not exist
        expected.expect(DAOException.class);
        facultyDao.add(FACULTY_WITH_INCORRECT_UNIVERSITY);
    }

    @Test
    public void updateTest() throws Exception {
        facultyDao.update(UPDATED_FACULTY);

        Assert.assertEquals(UPDATED_FACULTY, facultyDao.get(FACULTY1.getId()));

        expected.expect(DAOException.class);
        facultyDao.update(FACULTY_WITH_INCORRECT_UNIVERSITY);
    }

    @Test
    public void deleteTest() throws Exception {
        facultyDao.delete(FACULTY3.getId());

        Assert.assertEquals(facultyDao.getAll(), Arrays.asList(FACULTY1, FACULTY2, FACULTY4));

        expected.expect(DAOException.class);
        facultyDao.delete(INCORRECT_ID);
    }
}