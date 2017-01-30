package com.jayton.admissionoffice.service;

import com.jayton.admissionoffice.model.to.PaginationDTO;
import com.jayton.admissionoffice.model.university.Faculty;
import com.jayton.admissionoffice.service.exception.ServiceException;
import com.jayton.admissionoffice.service.exception.ServiceVerificationException;
import com.jayton.admissionoffice.util.di.BeanContextHolder;
import com.jayton.admissionoffice.util.di.exception.InjectionException;
import org.junit.*;
import org.junit.rules.ExpectedException;
import util.ContextInitializationHelper;
import util.DbInitializationHelper;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.jayton.admissionoffice.data.TestData.*;
import static com.jayton.admissionoffice.data.TestData.FACULTY3;
import static com.jayton.admissionoffice.data.TestData.FACULTY4;

public class FacultyServiceImplTest {

    private FacultyService facultyService = (FacultyService)
            BeanContextHolder.getInstance().getActualContext().getBean("facultyService");

    @Rule
    public ExpectedException expected = ExpectedException.none();

    @BeforeClass
    public static void initContext() throws InjectionException {
        ContextInitializationHelper helper = ContextInitializationHelper.getInstance();
        helper.initContext("di/dependencies.xml");
    }

    @Before
    public void setUpDb() throws Exception {
        DbInitializationHelper.getInstance().executeDbPopulate("populateForServiceTest.sql");
    }

    @Test
    public void addTest() throws Exception {
        long id = facultyService.add(NEW_FACULTY);

        Assert.assertEquals(NEW_ID, id);
    }

    @Test
    public void addWithIncorrectUniversityTest() throws Exception {
        expected.expect(ServiceException.class);
        facultyService.add(FACULTY_WITH_INCORRECT_UNIVERSITY);
    }

    @Test
    public void addWithNullableFieldsTest() throws Exception {
        expected.expect(ServiceException.class);
        facultyService.add(FACULTY_WITH_NULLABLE_FIELDS);
    }

    @Test
    public void addNullableTest() throws Exception {
        expected.expect(ServiceVerificationException.class);
        facultyService.add(null);
    }


    @Test
    public void getTest() throws Exception {
        Assert.assertEquals(FACULTY1, facultyService.get(FACULTY1.getId()));
    }

    @Test
    public void getByIncorrectIdTest() throws Exception {
        Assert.assertNull(facultyService.get(INCORRECT_ID));
    }

    @Test
    public void updateTest() throws Exception {
        Assert.assertTrue(facultyService.update(UPDATED_FACULTY));

        Assert.assertEquals(UPDATED_FACULTY, facultyService.get(FACULTY1.getId()));
    }

    @Test
    public void updateWithIncorrectUniversityTest() throws Exception {
        Assert.assertFalse(facultyService.update(FACULTY_WITH_INCORRECT_UNIVERSITY));
    }

    @Test
    public void updateWithNullableFieldsTest() throws Exception {
        expected.expect(ServiceException.class);
        Assert.assertFalse(facultyService.update(FACULTY_WITH_NULLABLE_FIELDS));
    }

    @Test
    public void updateNullableTest() throws Exception {
        expected.expect(ServiceVerificationException.class);
        facultyService.update(null);
    }

    @Test
    public void deleteTest() throws Exception {
        Assert.assertTrue(facultyService.delete(FACULTY3.getId()));
    }

    @Test
    public void deleteByIncorrectIdTest() throws Exception {
        Assert.assertFalse(facultyService.delete(INCORRECT_ID));
    }

    @Test
    public void getWithCountByUniversityTest() throws Exception {
        PaginationDTO<Faculty> allDto = facultyService.getWithCountByUniversity(UNIVERSITY2.getId(), 0, 100);
        List<Faculty> faculties = allDto.getEntries();

        Assert.assertEquals(faculties, Arrays.asList(FACULTY3, FACULTY4));
        Assert.assertEquals(allDto.getCount(), 2);
    }

    @Test
    public void getWithCountByIncorrectIdTest() throws Exception {
        PaginationDTO<Faculty> incorrectDto = facultyService.getWithCountByUniversity(INCORRECT_ID, 0, 100);

        Assert.assertEquals(incorrectDto.getEntries(), Collections.emptyList());
        Assert.assertEquals(incorrectDto.getCount(), 0);
    }
}