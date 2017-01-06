package com.jayton.admissionoffice.util.di;

import com.jayton.admissionoffice.model.university.University;
import com.jayton.admissionoffice.service.ServiceFactory;
import com.jayton.admissionoffice.service.UniversityService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import util.InitHelper;

import java.util.Arrays;
import java.util.List;

import static com.jayton.admissionoffice.dao.data.TestData.UNIVERSITY1;
import static com.jayton.admissionoffice.dao.data.TestData.UNIVERSITY2;
import static com.jayton.admissionoffice.dao.data.TestData.UNIVERSITY3;

public class InjectionResolverTest {

    @Rule
    public ExpectedException expected = ExpectedException.none();

    @Before
    public void setUpDb() throws Exception {
        InitHelper.executeDbPopulate("populateForDaoTest.sql");
    }

    @Test
    public void initTest() throws Exception {
        InjectionResolver resolver = new InjectionResolver();
        resolver.init();
        resolver.parse();

        UniversityService service = ServiceFactory.getInstance().getUniversityService();
        List<University> all = service.getAll(0, 100);
        Assert.assertEquals(Arrays.asList(UNIVERSITY1, UNIVERSITY2, UNIVERSITY3), all);
    }
}