package com.jayton.admissionoffice.dao.jdbc;

import com.jayton.admissionoffice.dao.exception.DAOException;
import com.jayton.admissionoffice.model.user.User;
import com.jayton.admissionoffice.util.InitHelper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

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
        Assert.assertEquals(Arrays.asList(USER1, USER2, USER3, NEW_USER), jdbcEnrolleeDao.getAll());

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

        expected.expect(DAOException.class);
        Assert.assertFalse(jdbcEnrolleeDao.delete(NEW_USER.getId()));
    }
}