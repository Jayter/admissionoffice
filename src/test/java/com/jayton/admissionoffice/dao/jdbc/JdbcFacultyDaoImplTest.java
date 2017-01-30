package com.jayton.admissionoffice.dao.jdbc;

import com.jayton.admissionoffice.dao.FacultyDao;
import com.jayton.admissionoffice.dao.exception.DAOException;
import com.jayton.admissionoffice.model.to.PaginationDto;
import com.jayton.admissionoffice.model.university.Faculty;
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

public class JdbcFacultyDaoImplTest {

    private FacultyDao facultyDao = (FacultyDao)
            BeanContextHolder.getInstance().getActualContext().getBean("facultyDao");

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
        long id = facultyDao.add(NEW_FACULTY);

        Assert.assertEquals(NEW_ID, id);

        Faculty added = new Faculty(id, NEW_FACULTY.getName(), NEW_FACULTY.getOfficePhone(), NEW_FACULTY.getOfficeEmail(),
                NEW_FACULTY.getOfficeAddress(), NEW_FACULTY.getUniversityId());
        Assert.assertTrue(facultyDao.getAll().contains(added));
    }

    @Test
    public void addWithIncorrectUniversityTest() throws Exception {
        expected.expect(DAOException.class);
        facultyDao.add(FACULTY_WITH_INCORRECT_UNIVERSITY);
    }

    @Test
    public void addWithNullableFieldsTest() throws Exception {
        expected.expect(DAOException.class);
        facultyDao.add(FACULTY_WITH_NULLABLE_FIELDS);
    }


    @Test
    public void getTest() throws Exception {
        Assert.assertEquals(FACULTY1, facultyDao.get(FACULTY1.getId()));
    }

    @Test
    public void getByIncorrectIdTest() throws Exception {
        Assert.assertNull(facultyDao.get(INCORRECT_ID));
    }

    @Test
    public void updateTest() throws Exception {
        Assert.assertTrue(facultyDao.update(UPDATED_FACULTY));

        Assert.assertEquals(UPDATED_FACULTY, facultyDao.get(FACULTY1.getId()));
    }

    @Test
    public void updateWithIncorrectUniversityTest() throws Exception {
        Assert.assertFalse(facultyDao.update(FACULTY_WITH_INCORRECT_UNIVERSITY));
    }

    @Test
    public void updateWithNullableFieldsTest() throws Exception {
        expected.expect(DAOException.class);
        Assert.assertFalse(facultyDao.update(FACULTY_WITH_NULLABLE_FIELDS));
    }

    @Test
    public void deleteTest() throws Exception {
        Assert.assertTrue(facultyDao.delete(FACULTY3.getId()));

        Assert.assertFalse(facultyDao.getAll().contains(FACULTY3));
    }

    @Test
    public void deleteByIncorrectIdTest() throws Exception {
        Assert.assertFalse(facultyDao.delete(INCORRECT_ID));
    }

    @Test
    public void getWithCountByUniversityTest() throws Exception {
        PaginationDto<Faculty> allDto = facultyDao.getWithCountByUniversity(UNIVERSITY2.getId(), 0, 100);
        List<Faculty> faculties = allDto.getEntries();

        Assert.assertEquals(faculties, Arrays.asList(FACULTY3, FACULTY4));
        Assert.assertEquals(allDto.getCount(), 2);
    }

    @Test
    public void getWithCountByIncorrectIdTest() throws Exception {
        PaginationDto<Faculty> incorrectDto = facultyDao.getWithCountByUniversity(INCORRECT_ID, 0, 100);

        Assert.assertEquals(incorrectDto.getEntries(), Collections.emptyList());
        Assert.assertEquals(incorrectDto.getCount(), 0);
    }

    @Test
    public void getAllTest() throws Exception {
        List<Faculty> all = facultyDao.getAll();

        Assert.assertEquals(Arrays.asList(FACULTY1, FACULTY2, FACULTY3, FACULTY4), all);
    }
}