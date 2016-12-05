package com.jayton.admissionoffice.dao.jdbc;

import com.jayton.admissionoffice.dao.FactoryProducer;
import com.jayton.admissionoffice.dao.UtilDao;
import com.jayton.admissionoffice.dao.exception.DAOException;
import com.jayton.admissionoffice.util.InitHelper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static com.jayton.admissionoffice.data.TestData.*;

/**
 * Created by Jayton on 06.12.2016.
 */
public class JdbcUtilDaoTest {
    private UtilDao utilDao = FactoryProducer.getInstance().getPostgresDaoFactory().getUtilDao();

    @Rule
    public ExpectedException expected = ExpectedException.none();

    @Before
    public void setUpDb() throws Exception {
        InitHelper.executeDbPopulate("populateTestDb.sql");
    }

    @Test
    public void getSessionDate() throws Exception {
        List<LocalDateTime> dates = utilDao.getSessionDate(2016);
        Assert.assertEquals(dates, Arrays.asList(START_DATE, END_DATE));

        expected.expect(DAOException.class);
        utilDao.getSessionDate(2018);
    }
}
