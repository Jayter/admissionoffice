package com.jayton.admissionoffice.dao.jdbc;

import com.jayton.admissionoffice.dao.FactoryProducer;
import com.jayton.admissionoffice.dao.UtilDao;
import com.jayton.admissionoffice.dao.exception.DAOException;
import com.jayton.admissionoffice.model.Subject;
import com.jayton.admissionoffice.model.to.SessionTerms;
import util.InitHelper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Arrays;
import java.util.List;

import static com.jayton.admissionoffice.dao.data.TestData.*;

public class JdbcUtilDaoTest {
    private UtilDao utilDao = FactoryProducer.getInstance().getPostgresDaoFactory().getUtilDao();

    @Rule
    public ExpectedException expected = ExpectedException.none();

    @Before
    public void setUpDb() throws Exception {
        InitHelper.executeDbPopulate("populateForDaoTest.sql");
    }

    @Test
    public void getSessionTermsTest() throws Exception {
        SessionTerms terms = utilDao.getSessionTerms((short)2016);

        Assert.assertEquals(terms, SESSION_TERMS);

        expected.expect(DAOException.class);
        utilDao.getSessionTerms((short)2018);
    }

    @Test
    public void getAllSubjectsTest() throws Exception {
        List<Subject> subjects = utilDao.getAllSubjects();

        Assert.assertEquals(subjects, Arrays.asList(SUBJECT1, SUBJECT2, SUBJECT3, SUBJECT4));
    }
}