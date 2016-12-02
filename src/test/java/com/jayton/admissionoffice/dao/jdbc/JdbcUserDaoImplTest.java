package com.jayton.admissionoffice.dao.jdbc;

import com.jayton.admissionoffice.dao.exception.DAOException;
import com.jayton.admissionoffice.model.user.User;
import com.jayton.admissionoffice.util.InitHelper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.jayton.admissionoffice.data.TestData.*;

/**
 * Created by Jayton on 27.11.2016.
 */
public class JdbcUserDaoImplTest {

    private JdbcUserDaoImpl jdbcEnrolleeDao = JdbcUserDaoImpl.getInstance();

    @Rule
    public ExpectedException expected = ExpectedException.none();

    @Before
    public void setUpDb() throws SQLException {
        InitHelper.executeDbPopulate("populateTestDb.sql");
    }

    @Test
    public void get() throws Exception {
        User retrieved = jdbcEnrolleeDao.get(USER1.getId());

        Assert.assertEquals(USER1, retrieved);
        Assert.assertNull(retrieved.getPassword());

        Assert.assertNull(jdbcEnrolleeDao.get(INCORRECT_ID));
    }

    @Test
    public void getAll() throws Exception {
        List<User> actual = jdbcEnrolleeDao.getAll();

        Assert.assertEquals(Arrays.asList(USER1, USER2, USER3), actual);
    }

    @Test
    public void add() throws Exception {
        Assert.assertEquals(NEW_ID, jdbcEnrolleeDao.add(NEW_USER));
        Assert.assertEquals(Arrays.asList(USER1, USER2, USER3, NEW_USER_WITHOUT_CREDENTIALS),
                jdbcEnrolleeDao.getAll().stream().sorted(COMPARATOR).collect(Collectors.toList()));

        expected.expect(DAOException.class);
        jdbcEnrolleeDao.add(USER_WITH_NULLABLE_FIELDS);
    }

    @Test
    public void update() throws Exception {
        jdbcEnrolleeDao.update(UPDATED_USER);
        Assert.assertEquals(UPDATED_USER, jdbcEnrolleeDao.get(USER2.getId()));

        //updating user that was not saved in db
        expected.expect(DAOException.class);
        jdbcEnrolleeDao.update(NEW_USER);
    }

    @Test
    public void delete() throws Exception {
        Assert.assertTrue(jdbcEnrolleeDao.delete(USER1.getId()));
        Assert.assertEquals(Arrays.asList(USER2, USER3), jdbcEnrolleeDao.getAll());

        Assert.assertFalse(jdbcEnrolleeDao.delete(INCORRECT_ID));

        expected.expect(DAOException.class);
        Assert.assertFalse(jdbcEnrolleeDao.delete(NEW_USER.getId()));
    }

    @Test
    public void deleteResult() throws DAOException {
        Assert.assertTrue(jdbcEnrolleeDao.deleteResult(USER1.getId(), SUBJECT4.getId()));
        Assert.assertEquals(jdbcEnrolleeDao.getByUser(USER1.getId()).size(), 3);

        //has already been removed
        Assert.assertFalse(jdbcEnrolleeDao.deleteResult(USER1.getId(), SUBJECT4.getId()));

        expected.expect(DAOException.class);
        jdbcEnrolleeDao.deleteResult(NEW_USER.getId(), SUBJECT1.getId());
    }

    @Test
    public void addResult() throws DAOException {
        jdbcEnrolleeDao.addResult(USER2.getId(), SUBJECT4.getId(), new BigDecimal(173.6));
        Assert.assertEquals(jdbcEnrolleeDao.getByUser(USER1.getId()).size(), 4);

        //duplicated
        expected.expect(DAOException.class);
        jdbcEnrolleeDao.addResult(USER2.getId(), SUBJECT4.getId(), new BigDecimal(173.6));
    }

    @Test
    public void getByUser() throws DAOException {
        Assert.assertEquals(jdbcEnrolleeDao.getByUser(USER2.getId()), USER2.getResults());

        Assert.assertEquals(jdbcEnrolleeDao.getByUser(INCORRECT_ID), Collections.emptyMap());
    }
}