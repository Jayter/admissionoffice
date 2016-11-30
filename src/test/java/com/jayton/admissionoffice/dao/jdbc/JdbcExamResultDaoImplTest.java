package com.jayton.admissionoffice.dao.jdbc;

import com.jayton.admissionoffice.util.InitHelper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Arrays;
import java.util.Collections;

import static com.jayton.admissionoffice.data.TestData.*;

/**
 * Created by Jayton on 30.11.2016.
 */
public class JdbcExamResultDaoImplTest {

    private JdbcExamResultDaoImpl jdbcExamResultDao = JdbcExamResultDaoImpl.getInstance();

    @Rule
    public ExpectedException expected = ExpectedException.none();

    @Before
    public void setUpDb() throws Exception {
        InitHelper.executeDbPopulate("populateTestDb.sql");
    }

    @Test
    public void add() throws Exception {
        jdbcExamResultDao.add(RESULT5);
        jdbcExamResultDao.add(Arrays.asList(RESULT6, RESULT7, RESULT8));

        Assert.assertEquals(Arrays.asList(RESULT5, RESULT6, RESULT7, RESULT8),
                jdbcExamResultDao.getByUser(USER2.getId()));
    }

    @Test
    public void delete() throws Exception {
        Assert.assertTrue(jdbcExamResultDao.delete(RESULT4));

        Assert.assertEquals(Arrays.asList(RESULT1, RESULT2, RESULT3),
                jdbcExamResultDao.getByUser(USER1.getId()));

        Assert.assertFalse(jdbcExamResultDao.delete(RESULT6));
    }

    @Test
    public void update() throws Exception {
        jdbcExamResultDao.update(UPDATED_RESULT);

        Assert.assertEquals(Arrays.asList(RESULT1, RESULT3, RESULT4, UPDATED_RESULT),
                jdbcExamResultDao.getByUser(USER1.getId()));
    }

    @Test
    public void getByUser() throws Exception {
        Assert.assertEquals(Arrays.asList(RESULT1, RESULT2, RESULT3, RESULT4),
                jdbcExamResultDao.getByUser(USER1.getId()));

        Assert.assertEquals(Collections.emptyList(), jdbcExamResultDao.getByUser(USER3.getId()));
    }

}