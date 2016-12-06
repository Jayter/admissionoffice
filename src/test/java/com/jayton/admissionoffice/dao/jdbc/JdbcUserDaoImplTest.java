package com.jayton.admissionoffice.dao.jdbc;

import com.jayton.admissionoffice.dao.FactoryProducer;
import com.jayton.admissionoffice.dao.UserDao;
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

    private UserDao userDao = FactoryProducer.getInstance().getPostgresDaoFactory().getUserDao();

    @Rule
    public ExpectedException expected = ExpectedException.none();

    @Before
    public void setUpDb() throws SQLException {
        InitHelper.executeDbPopulate("populateTestDb.sql");
    }

    @Test
    public void get() throws Exception {
        User retrieved = userDao.get(USER1.getId());

        Assert.assertEquals(USER1, retrieved);
        Assert.assertNull(retrieved.getPassword());

        User retrievedByEmail = userDao.getByEmail(USER1.getEmail());
        Assert.assertEquals(USER1, retrievedByEmail);

        Assert.assertNull(userDao.get(INCORRECT_ID));
    }

    @Test
    public void getAll() throws Exception {
        List<User> actual = userDao.getAll();

        Assert.assertEquals(Arrays.asList(USER1, USER2, USER3), actual);
    }

    @Test
    public void add() throws Exception {
        Assert.assertEquals(NEW_ID, userDao.add(NEW_USER));
        Assert.assertEquals(Arrays.asList(USER1, USER2, USER3, NEW_USER_WITHOUT_CREDENTIALS),
                userDao.getAll().stream().sorted(COMPARATOR).collect(Collectors.toList()));

        expected.expect(DAOException.class);
        userDao.add(USER_WITH_NULLABLE_FIELDS);
    }

    @Test
    public void update() throws Exception {
        userDao.update(UPDATED_USER);
        Assert.assertEquals(UPDATED_USER, userDao.get(USER2.getId()));

        //updating user that was not saved in db
        expected.expect(DAOException.class);
        userDao.update(NEW_USER);
    }

    @Test
    public void delete() throws Exception {
        Assert.assertTrue(userDao.delete(USER1.getId()));
        Assert.assertEquals(Arrays.asList(USER2, USER3), userDao.getAll());

        Assert.assertFalse(userDao.delete(INCORRECT_ID));

        expected.expect(DAOException.class);
        Assert.assertFalse(userDao.delete(NEW_USER.getId()));
    }

    @Test
    public void deleteResult() throws DAOException {
        Assert.assertTrue(userDao.deleteResult(USER1.getId(), SUBJECT4.getId()));
        Assert.assertEquals(userDao.getByUser(USER1.getId()).size(), 3);

        //has already been removed
        Assert.assertFalse(userDao.deleteResult(USER1.getId(), SUBJECT4.getId()));

        expected.expect(DAOException.class);
        userDao.deleteResult(NEW_USER.getId(), SUBJECT1.getId());
    }

    @Test
    public void addResult() throws DAOException {
        userDao.addResult(USER2.getId(), SUBJECT4.getId(), new BigDecimal(173.6));
        Assert.assertEquals(userDao.getByUser(USER1.getId()).size(), 4);

        //duplicated
        expected.expect(DAOException.class);
        userDao.addResult(USER2.getId(), SUBJECT4.getId(), new BigDecimal(173.6));
    }

    @Test
    public void getByUser() throws DAOException {
        Assert.assertEquals(userDao.getByUser(USER2.getId()), USER2.getResults());

        Assert.assertEquals(userDao.getByUser(INCORRECT_ID), Collections.emptyMap());
    }

    @Test
    public void authorizeTest() throws Exception {
        Assert.assertEquals(AuthorizationResult.ADMIN, userDao.authorize(ADMIN_LOGIN, ADMIN_PASSWORD));

        Assert.assertEquals(AuthorizationResult.USER, userDao.authorize(USER_LOGIN, USER_PASSWORD));

        //incorrect combinations
        Assert.assertEquals(AuthorizationResult.NULL, userDao.authorize(ADMIN_LOGIN, USER_PASSWORD));
        Assert.assertEquals(AuthorizationResult.NULL, userDao.authorize(INCORRECT_STRING, INCORRECT_STRING));
    }

    @Test
    public void checkEmailTest() throws Exception {
        Assert.assertEquals(1, userDao.checkEmail(USER1.getEmail()));

        Assert.assertEquals(0, userDao.checkEmail(NEW_USER.getEmail()));
    }
}