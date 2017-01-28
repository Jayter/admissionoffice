package com.jayton.admissionoffice.dao.jdbc;

import com.jayton.admissionoffice.dao.UtilDao;
import com.jayton.admissionoffice.dao.exception.DAOException;
import com.jayton.admissionoffice.model.Subject;
import com.jayton.admissionoffice.model.to.SessionTerms;
import com.jayton.admissionoffice.util.di.BeanContextHolder;
import com.jayton.admissionoffice.util.di.exception.InjectionException;
import org.junit.*;
import util.ContextInitializationHelper;
import util.DbInitializationHelper;
import org.junit.rules.ExpectedException;

import java.util.Arrays;
import java.util.List;

import static com.jayton.admissionoffice.dao.data.TestData.*;

public class JdbcUtilDaoTest {

    private UtilDao utilDao = (UtilDao)
            BeanContextHolder.getInstance().getActualContext().getBean("utilDao");

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
    public void getSessionTermsTest() throws Exception {
        SessionTerms terms = utilDao.getSessionTerms((short)2016);

        Assert.assertEquals(terms, SESSION_TERMS);

        Assert.assertNull(utilDao.getSessionTerms((short)2018));
    }

    @Test
    public void getAllSubjectsTest() throws Exception {
        List<Subject> subjects = utilDao.getAllSubjects();

        Assert.assertEquals(subjects, Arrays.asList(SUBJECT1, SUBJECT2, SUBJECT3, SUBJECT4));
    }
}