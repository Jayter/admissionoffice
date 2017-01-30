package com.jayton.admissionoffice.dao.jdbc;

import com.jayton.admissionoffice.dao.UniversityDao;
import com.jayton.admissionoffice.dao.exception.DAOException;
import com.jayton.admissionoffice.model.to.PaginationDto;
import com.jayton.admissionoffice.model.university.University;
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

public class JdbcUniversityDaoImplTest {

    private UniversityDao universityDao = (UniversityDao)
            BeanContextHolder.getInstance().getActualContext().getBean("universityDao");

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
        long id = universityDao.add(NEW_UNIVERSITY);
        Assert.assertEquals(NEW_ID, id);

        University added = new University(id, NEW_UNIVERSITY.getName(), NEW_UNIVERSITY.getCity(), NEW_UNIVERSITY.getAddress());
        Assert.assertEquals(Arrays.asList(UNIVERSITY1, UNIVERSITY2, UNIVERSITY3, added),
                universityDao.getWithCount(0, 100).getEntries());
    }

    @Test
    public void addWithNullableFieldsTest() throws Exception {
        expected.expect(DAOException.class);
        universityDao.add(UNIVERSITY_WITH_NULLABLE_FIELDS);
    }

    @Test
    public void getTest() throws Exception {
        Assert.assertEquals(UNIVERSITY1, universityDao.get(UNIVERSITY1.getId()));
    }

    @Test
    public void getByIncorrectIdTest() throws Exception {
        Assert.assertNull(universityDao.get(INCORRECT_ID));
    }

    @Test
    public void updateTest() throws Exception {
        Assert.assertTrue(universityDao.update(UPDATED_UNIVERSITY));

        Assert.assertEquals(UPDATED_UNIVERSITY, universityDao.get(UNIVERSITY3.getId()));
    }

    @Test
    public void updateNewTest() throws Exception {
        Assert.assertFalse(universityDao.update(NEW_UNIVERSITY));
    }

    @Test
    public void updateIncorrectTest() throws Exception {
        Assert.assertFalse(universityDao.update(new University(INCORRECT_ID, UNIVERSITY1.getName(),
                UNIVERSITY1.getCity(), UNIVERSITY1.getAddress())));
    }

    @Test
    public void deleteTest() throws Exception {
        Assert.assertTrue(universityDao.delete(UNIVERSITY3.getId()));

        Assert.assertFalse(universityDao.getWithCount(0, 100).getEntries().contains(UNIVERSITY3));
    }

    @Test
    public void deleteByIncorrectIdTest() throws Exception {
        Assert.assertFalse(universityDao.delete(INCORRECT_ID));
    }

    @Test
    public void getByCityTest() throws Exception {
        List<University> list = universityDao.getWithCountByCity(KYIV, 0, 100).getEntries();

        Assert.assertEquals(list, Arrays.asList(UNIVERSITY1, UNIVERSITY2));
    }

    @Test
    public void getByIncorrectCityTest() throws Exception {
        Assert.assertEquals(universityDao.getWithCountByCity(INCORRECT_STRING, 0, 100).getEntries(), Collections.emptyList());
    }

    @Test
    public void getWithCountTest() throws Exception {
        PaginationDto<University> allDto = universityDao.getWithCount(0, 100);
        List<University> all = allDto.getEntries();

        Assert.assertEquals(Arrays.asList(UNIVERSITY1, UNIVERSITY2, UNIVERSITY3), all);
        Assert.assertEquals(allDto.getCount(), 3);
    }

    @Test
    public void getSingleWithCountTest() throws Exception {
        PaginationDto<University> singleDto = universityDao.getWithCount(0, 1);
        List<University> single = singleDto.getEntries();

        Assert.assertEquals(Collections.singletonList(UNIVERSITY1), single);
        Assert.assertEquals(singleDto.getCount(), 3);
    }
}