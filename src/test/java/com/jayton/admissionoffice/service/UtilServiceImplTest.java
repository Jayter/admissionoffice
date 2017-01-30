package com.jayton.admissionoffice.service;

import com.jayton.admissionoffice.model.Subject;
import com.jayton.admissionoffice.model.to.SessionTerms;
import com.jayton.admissionoffice.service.exception.ServiceException;
import com.jayton.admissionoffice.util.di.BeanContextHolder;
import com.jayton.admissionoffice.util.di.exception.InjectionException;
import org.junit.*;
import org.junit.rules.ExpectedException;
import util.ContextInitializationHelper;
import util.DbInitializationHelper;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static com.jayton.admissionoffice.data.TestData.*;
import static com.jayton.admissionoffice.data.TestData.SUBJECT4;

public class UtilServiceImplTest {
    private UtilService utilService = (UtilService)
            BeanContextHolder.getInstance().getActualContext().getBean("utilService");

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
    public void getSessionTermsTest() throws Exception {
        SessionTerms terms = utilService.getSessionTerms((short)2016);

        Assert.assertEquals(terms, SESSION_TERMS);
    }

    @Test
    public void getNonExistedSessionTermsTest() throws Exception {
        Assert.assertNull(utilService.getSessionTerms((short)2018));
    }

    @Test
    public void getAllSubjectsTest() throws Exception {
        List<Subject> subjects = utilService.getAllSubjects();

        Assert.assertEquals(subjects, Arrays.asList(SUBJECT1, SUBJECT2, SUBJECT3, SUBJECT4));
    }

    @Test
    public void createSessionTermsTest() throws Exception {
        Assert.assertTrue(utilService.createSessionTerms(SESSION_TERMS_2018));
    }

    @Test
    public void createExistedTermsTest() throws Exception {
        expected.expect(ServiceException.class);
        utilService.createSessionTerms(SESSION_TERMS);
    }

    @Test
    public void updateSessionTermsTest() throws Exception {
        Assert.assertTrue(utilService.updateSessionTerms(new SessionTerms(SESSION_TERMS.getYear(),
                SESSION_TERMS.getSessionStart(), LocalDateTime.of(2016, 10, 9, 8, 7))));
    }

    @Test
    public void updateNonExistedSessionTermsTest() throws Exception {
        Assert.assertFalse(utilService.updateSessionTerms(SESSION_TERMS_2018));
    }
}