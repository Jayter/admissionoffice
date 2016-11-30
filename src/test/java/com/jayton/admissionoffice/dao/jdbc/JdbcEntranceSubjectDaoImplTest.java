package com.jayton.admissionoffice.dao.jdbc;

import com.jayton.admissionoffice.model.to.EntranceSubject;
import com.jayton.admissionoffice.util.InitHelper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.jayton.admissionoffice.data.TestData.*;

/**
 * Created by Jayton on 30.11.2016.
 */
public class JdbcEntranceSubjectDaoImplTest {

    private JdbcEntranceSubjectDaoImpl jdbcEntranceSubjectDao = JdbcEntranceSubjectDaoImpl.getInstance();

    @Rule
    public ExpectedException expected = ExpectedException.none();

    @Before
    public void setUpDb() throws Exception {
        InitHelper.executeDbPopulate("populateTestDb.sql");
    }

    @Test
    public void addSubjects() throws Exception {
        jdbcEntranceSubjectDao.addSubject(ENTRANCE4);
        jdbcEntranceSubjectDao.addSubjects(Arrays.asList(ENTRANCE5, ENTRANCE6));

        Assert.assertEquals(Arrays.asList(ENTRANCE4, ENTRANCE5, ENTRANCE6),
                jdbcEntranceSubjectDao.getSubjects(DIRECTION2.getId()));
    }

    @Test
    public void deleteSubject() throws Exception {
        Assert.assertTrue(jdbcEntranceSubjectDao.deleteSubject(ENTRANCE3));

        Assert.assertEquals(Arrays.asList(ENTRANCE1, ENTRANCE2),
                jdbcEntranceSubjectDao.getSubjects(DIRECTION1.getId()));

        Assert.assertFalse(jdbcEntranceSubjectDao.deleteSubject(ENTRANCE4));
    }

    @Test
    public void getSubjects() throws Exception {
        Assert.assertEquals(Arrays.asList(ENTRANCE1, ENTRANCE2, ENTRANCE3),
                jdbcEntranceSubjectDao.getSubjects(DIRECTION1.getId()));

        Assert.assertEquals(Collections.emptyList(),
                jdbcEntranceSubjectDao.getSubjects(DIRECTION4.getId()));
    }

}