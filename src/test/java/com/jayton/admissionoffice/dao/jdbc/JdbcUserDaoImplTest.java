package com.jayton.admissionoffice.dao.jdbc;

import com.jayton.admissionoffice.dao.exception.DAOException;
import com.jayton.admissionoffice.model.to.AuthorizationResult;
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

    private JdbcUserDaoImpl jdbcUserDao = JdbcUserDaoImpl.getInstance();

    @Rule
    public ExpectedException expected = ExpectedException.none();

    @Before
    public void setUpDb() throws SQLException {
        InitHelper.executeDbPopulate("populateTestDb.sql");
    }

    @Test
    public void get() throws Exception {
        User retrieved = jdbcUserDao.get(USER1.getId());

        Assert.assertEquals(USER1, retrieved);
        Assert.assertNull(retrieved.getPassword());

        Assert.assertNull(jdbcUserDao.get(INCORRECT_ID));
    }

    @Test
    public void getAll() throws Exception {
        List<User> actual = jdbcUserDao.getAll();

        Assert.assertEquals(Arrays.asList(USER1, USER2, USER3), actual);
    }

    @Test
    public void add() throws Exception {
        Assert.assertEquals(NEW_ID, jdbcUserDao.add(NEW_USER));
        Assert.assertEquals(Arrays.asList(USER1, USER2, USER3, NEW_USER_WITHOUT_CREDENTIALS),
                jdbcUserDao.getAll().stream().sorted(COMPARATOR).collect(Collectors.toList()));

        expected.expect(DAOException.class);
        jdbcUserDao.add(USER_WITH_NULLABLE_FIELDS);
    }

    @Test
    public void update() throws Exception {
        jdbcUserDao.update(UPDATED_USER);
        Assert.assertEquals(UPDATED_USER, jdbcUserDao.get(USER2.getId()));

        //updating user that was not saved in db
        expected.expect(DAOException.class);
        jdbcUserDao.update(NEW_USER);
    }

    @Test
    public void delete() throws Exception {
        Assert.assertTrue(jdbcUserDao.delete(USER1.getId()));
        Assert.assertEquals(Arrays.asList(USER2, USER3), jdbcUserDao.getAll());

        Assert.assertFalse(jdbcUserDao.delete(INCORRECT_ID));

        expected.expect(DAOException.class);
        Assert.assertFalse(jdbcUserDao.delete(NEW_USER.getId()));
    }

    @Test
    public void deleteResult() throws DAOException {
        Assert.assertTrue(jdbcUserDao.deleteResult(USER1.getId(), SUBJECT4.getId()));
        Assert.assertEquals(jdbcUserDao.getByUser(USER1.getId()).size(), 3);

        //has already been removed
        Assert.assertFalse(jdbcUserDao.deleteResult(USER1.getId(), SUBJECT4.getId()));

        expected.expect(DAOException.class);
        jdbcUserDao.deleteResult(NEW_USER.getId(), SUBJECT1.getId());
    }

    @Test
    public void addResult() throws DAOException {
        jdbcUserDao.addResult(USER2.getId(), SUBJECT4.getId(), new BigDecimal(173.6));
        Assert.assertEquals(jdbcUserDao.getByUser(USER1.getId()).size(), 4);

        //duplicated
        expected.expect(DAOException.class);
        jdbcUserDao.addResult(USER2.getId(), SUBJECT4.getId(), new BigDecimal(173.6));
    }

    @Test
    public void getByUser() throws DAOException {
        Assert.assertEquals(jdbcUserDao.getByUser(USER2.getId()), USER2.getResults());

        Assert.assertEquals(jdbcUserDao.getByUser(INCORRECT_ID), Collections.emptyMap());
    }

    @Test
    public void authorizeTest() throws Exception {
        Assert.assertEquals(AuthorizationResult.ADMIN, jdbcUserDao.authorize(ADMIN_LOGIN, ADMIN_PASSWORD));

        Assert.assertEquals(AuthorizationResult.USER, jdbcUserDao.authorize(USER_LOGIN, USER_PASSWORD));

        //incorrect combinations
        Assert.assertEquals(AuthorizationResult.NULL, jdbcUserDao.authorize(ADMIN_LOGIN, USER_PASSWORD));
        Assert.assertEquals(AuthorizationResult.NULL, jdbcUserDao.authorize(INCORRECT_STRING, INCORRECT_STRING));
    }

    @Test
    public void checkEmailTest() throws Exception {
        Assert.assertEquals(1, jdbcUserDao.checkEmail(USER1.getEmail()));

        Assert.assertEquals(0, jdbcUserDao.checkEmail(NEW_USER.getEmail()));
    }
}