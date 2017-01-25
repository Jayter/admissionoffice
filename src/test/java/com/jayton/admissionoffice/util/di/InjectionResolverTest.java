package com.jayton.admissionoffice.util.di;

import com.jayton.admissionoffice.model.university.University;
import com.jayton.admissionoffice.service.UniversityService;
import com.jayton.admissionoffice.util.di.exception.InjectionException;
import org.junit.*;
import org.junit.rules.ExpectedException;
import util.ContextInitializationHelper;
import util.DbInitializationHelper;

import java.util.Arrays;
import java.util.List;

import static com.jayton.admissionoffice.dao.data.TestData.UNIVERSITY1;
import static com.jayton.admissionoffice.dao.data.TestData.UNIVERSITY2;
import static com.jayton.admissionoffice.dao.data.TestData.UNIVERSITY3;

public class InjectionResolverTest {

    UniversityService universityService = (UniversityService)
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
        DbInitializationHelper.getInstance().executeDbPopulate("populateForDaoTest.sql");
    }

    @Test
    public void initTest() throws Exception {
        XmlBeanContext resolver = new XmlBeanContext("di/dependencies.xml");
        resolver.init();

        List<University> all = universityService.getAll(0, 100);
        Assert.assertEquals(Arrays.asList(UNIVERSITY1, UNIVERSITY2, UNIVERSITY3), all);
    }
}