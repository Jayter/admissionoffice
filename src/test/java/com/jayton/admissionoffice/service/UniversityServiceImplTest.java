package com.jayton.admissionoffice.service;

import com.jayton.admissionoffice.model.to.PaginationDTO;
import com.jayton.admissionoffice.model.university.University;
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
import static com.jayton.admissionoffice.data.TestData.UNIVERSITY1;

public class UniversityServiceImplTest {

    private UniversityService universityService = (UniversityService)
            BeanContextHolder.getInstance().getActualContext().getBean("universityService");

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
        long id = universityService.add(NEW_UNIVERSITY);
        Assert.assertEquals(NEW_ID, id);

        University added = new University(id, NEW_UNIVERSITY.getName(), NEW_UNIVERSITY.getCity(), NEW_UNIVERSITY.getAddress());
        Assert.assertEquals(Arrays.asList(UNIVERSITY1, UNIVERSITY2, UNIVERSITY3, added),
                universityService.getWithCount(0, 100).getEntries());
    }

    @Test
    public void addWithNullableFieldsTest() throws Exception {
        expected.expect(ServiceException.class);
        universityService.add(UNIVERSITY_WITH_NULLABLE_FIELDS);
    }

    @Test
    public void addNullableTest() throws Exception {
        expected.expect(ServiceVerificationException.class);
        universityService.add(null);
    }

    @Test
    public void getTest() throws Exception {
        Assert.assertEquals(UNIVERSITY1, universityService.get(UNIVERSITY1.getId()));
    }

    @Test
    public void getByIncorrectIdTest() throws Exception {
        Assert.assertNull(universityService.get(INCORRECT_ID));
    }

    @Test
    public void updateTest() throws Exception {
        Assert.assertTrue(universityService.update(UPDATED_UNIVERSITY));

        Assert.assertEquals(UPDATED_UNIVERSITY, universityService.get(UNIVERSITY3.getId()));
    }

    @Test
    public void updateNewTest() throws Exception {
        Assert.assertFalse(universityService.update(NEW_UNIVERSITY));
    }

    @Test
    public void updateIncorrectTest() throws Exception {
        Assert.assertFalse(universityService.update(new University(INCORRECT_ID, UNIVERSITY1.getName(),
                UNIVERSITY1.getCity(), UNIVERSITY1.getAddress())));
    }

    @Test
    public void updateNullableTest() throws Exception {
        expected.expect(ServiceVerificationException.class);
        universityService.update(null);
    }

    @Test
    public void deleteTest() throws Exception {
        Assert.assertTrue(universityService.delete(UNIVERSITY3.getId()));

        Assert.assertFalse(universityService.getWithCount(0, 100).getEntries().contains(UNIVERSITY3));
    }

    @Test
    public void deleteByIncorrectIdTest() throws Exception {
        Assert.assertFalse(universityService.delete(INCORRECT_ID));
    }

    @Test
    public void getByCityTest() throws Exception {
        List<University> list = universityService.getWithCountByCity(KYIV, 0, 100).getEntries();

        Assert.assertEquals(list, Arrays.asList(UNIVERSITY1, UNIVERSITY2));
    }

    @Test
    public void getByIncorrectCityTest() throws Exception {
        Assert.assertEquals(universityService.getWithCountByCity(INCORRECT_STRING, 0, 100).getEntries(), Collections.emptyList());
    }

    @Test
    public void getByNullableCityTest() throws Exception {
        expected.expect(ServiceVerificationException.class);
        Assert.assertEquals(universityService.getWithCountByCity(null, 0, 100).getEntries(), Collections.emptyList());
    }

    @Test
    public void getWithCountTest() throws Exception {
        PaginationDTO<University> allDto = universityService.getWithCount(0, 100);
        List<University> all = allDto.getEntries();

        Assert.assertEquals(Arrays.asList(UNIVERSITY1, UNIVERSITY2, UNIVERSITY3), all);
        Assert.assertEquals(allDto.getCount(), 3);
    }

    @Test
    public void getSingleWithCountTest() throws Exception {
        PaginationDTO<University> singleDto = universityService.getWithCount(0, 1);
        List<University> single = singleDto.getEntries();

        Assert.assertEquals(Collections.singletonList(UNIVERSITY1), single);
        Assert.assertEquals(singleDto.getCount(), 3);
    }
}