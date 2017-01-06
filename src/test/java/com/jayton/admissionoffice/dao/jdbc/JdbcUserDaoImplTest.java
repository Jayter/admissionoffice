package com.jayton.admissionoffice.dao.jdbc;

import com.jayton.admissionoffice.dao.FactoryProducer;
import com.jayton.admissionoffice.dao.UserDao;
import com.jayton.admissionoffice.dao.exception.DAOException;
import com.jayton.admissionoffice.dao.data.UserMatcher;
import com.jayton.admissionoffice.model.to.AuthorizationResult;
import com.jayton.admissionoffice.model.user.User;
import util.InitHelper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.jayton.admissionoffice.dao.data.TestData.*;

public class JdbcUserDaoImplTest {

    private UserDao userDao = FactoryProducer.getInstance().getPostgresDaoFactory().getUserDao();

    private UserMatcher matcher = new UserMatcher();

    @Rule
    public ExpectedException expected = ExpectedException.none();

    @Before
    public void setUpDb() throws SQLException {
        InitHelper.executeDbPopulate("populateForDaoTest.sql");
    }

    @Test
    public void getTest() throws Exception {
        User retrieved = userDao.get(USER1.getId());

        Assert.assertTrue(matcher.equals(USER1, retrieved));
        Assert.assertNull(retrieved.getPassword());

        User retrievedByEmail = userDao.getByEmail(USER1.getEmail());
        Assert.assertTrue(matcher.equals(USER1, retrievedByEmail));

        Assert.assertNull(userDao.get(INCORRECT_ID));
    }

    @Test
    public void getAllTest() throws Exception {
        List<User> actual = userDao.getAll(0, 100);

        Assert.assertTrue(matcher.equals(actual, Arrays.asList(USER1, USER2, USER3)));
    }

    @Test
    public void addTest() throws Exception {
        Assert.assertEquals(NEW_ID, userDao.add(NEW_USER).getId());
        Assert.assertTrue(matcher.equals(userDao.getAll(0, 100),
                Arrays.asList(USER1, USER2, USER3, NEW_USER_WITHOUT_CREDENTIALS) ));

        expected.expect(DAOException.class);
        userDao.add(USER_WITH_NULLABLE_FIELDS);
    }

    @Test
    public void updateTest() throws Exception {
        userDao.update(UPDATED_USER);
        Assert.assertTrue(matcher.equals(UPDATED_USER, userDao.get(USER3.getId())));

        //updating user that was not saved in db
        expected.expect(DAOException.class);
        userDao.update(NEW_USER);
    }

    @Test
    public void deleteTest() throws Exception {
        userDao.delete(USER1.getId());
        Assert.assertTrue(matcher.equals(Arrays.asList(USER2, USER3), userDao.getAll(0, 100)));

        expected.expect(DAOException.class);
        userDao.delete(INCORRECT_ID);
    }

    @Test
    public void deleteResultTest() throws DAOException {
        userDao.deleteResult(USER1.getId(), SUBJECT4.getId());
        Assert.assertEquals(userDao.getResultsOfUser(USER1.getId()).size(), 3);

        expected.expect(DAOException.class);
        userDao.deleteResult(NEW_USER.getId(), SUBJECT1.getId());
    }

    @Test
    public void addResultTest() throws DAOException {
        userDao.addResult(USER2.getId(), SUBJECT4.getId(), new Short("173"));
        Assert.assertEquals(userDao.getResultsOfUser(USER1.getId()).size(), 4);

        //duplicated
        expected.expect(DAOException.class);
        userDao.addResult(USER2.getId(), SUBJECT4.getId(), new Short("173"));
    }

    @Test
    public void getByUserTest() throws DAOException {
        Assert.assertTrue(matcher.compareResults(userDao.getResultsOfUser(USER2.getId()), USER2.getResults()));

        Assert.assertEquals(userDao.getResultsOfUser(INCORRECT_ID), Collections.emptyMap());
    }

    @Test
    public void authorizeTest() throws Exception {
        Assert.assertEquals(AuthorizationResult.ADMIN, userDao.authorize(ADMIN_LOGIN, ADMIN_PASSWORD));

        Assert.assertEquals(AuthorizationResult.USER, userDao.authorize(USER_LOGIN, USER_PASSWORD));

        //incorrect combinations
        Assert.assertEquals(AuthorizationResult.ABSENT, userDao.authorize(ADMIN_LOGIN, USER_PASSWORD));
        Assert.assertEquals(AuthorizationResult.ABSENT, userDao.authorize(INCORRECT_STRING, INCORRECT_STRING));
    }

    @Test
    public void checkEmailTest() throws Exception {
        Assert.assertEquals(1, userDao.checkEmail(USER1.getEmail()));

        Assert.assertEquals(0, userDao.checkEmail(NEW_USER.getEmail()));
    }
}