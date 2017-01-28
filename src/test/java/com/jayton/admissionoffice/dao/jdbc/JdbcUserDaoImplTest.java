package com.jayton.admissionoffice.dao.jdbc;

import com.jayton.admissionoffice.dao.UserDao;
import com.jayton.admissionoffice.dao.exception.DAOException;
import com.jayton.admissionoffice.dao.data.UserMatcher;
import com.jayton.admissionoffice.model.to.AuthorizationResult;
import com.jayton.admissionoffice.model.to.EntriesWithAssociatedPairsDto;
import com.jayton.admissionoffice.model.to.PaginationDTO;
import com.jayton.admissionoffice.model.user.User;
import com.jayton.admissionoffice.util.di.BeanContextHolder;
import com.jayton.admissionoffice.util.di.exception.InjectionException;
import org.junit.*;
import util.ContextInitializationHelper;
import util.DbInitializationHelper;
import org.junit.rules.ExpectedException;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.jayton.admissionoffice.dao.data.TestData.*;

public class JdbcUserDaoImplTest {

    private UserDao userDao = (UserDao)
            BeanContextHolder.getInstance().getActualContext().getBean("userDao");

    private UserMatcher matcher = new UserMatcher();

    @Rule
    public ExpectedException expected = ExpectedException.none();

    @BeforeClass
    public static void initContext() throws InjectionException {
        ContextInitializationHelper helper = ContextInitializationHelper.getInstance();
        helper.initContext("di/dependencies.xml");
    }

    @Before
    public void setUpDb() throws SQLException {
        DbInitializationHelper.getInstance().executeDbPopulate("populateForDaoTest.sql");
    }

    @Test
    public void addTest() throws Exception {
        long id = userDao.add(NEW_USER);
        Assert.assertEquals(NEW_ID, id);
        User added = new User(id, NEW_USER.getName(), NEW_USER.getLastName(), NEW_USER.getAddress(), NEW_USER.getEmail(),
                NEW_USER.getPhoneNumber(), NEW_USER.getBirthDate(), NEW_USER.getAverageMark(), Collections.emptyMap());

        Assert.assertEquals(added, userDao.get(id));

        expected.expect(DAOException.class);
        userDao.add(USER_WITH_NULLABLE_FIELDS);
    }

    @Test
    public void getTest() throws Exception {
        User retrieved = userDao.get(USER1.getId());

        Assert.assertTrue(matcher.equals(USER1, retrieved));
        Assert.assertNull(retrieved.getPassword());

        Assert.assertNull(userDao.get(INCORRECT_ID));
    }

    @Test
    public void getByEmailTest() throws Exception {
        User retrievedByEmail = userDao.getByEmail(USER1.getEmail());
        Assert.assertTrue(matcher.equals(USER1, retrievedByEmail));

        Assert.assertNull(userDao.getByEmail(INCORRECT_STRING));
    }

    @Test
    public void updateTest() throws Exception {
        Assert.assertTrue(userDao.update(UPDATED_USER));
        Assert.assertEquals(UPDATED_USER, userDao.get(USER3.getId()));

        //updating user that was not saved in db
        Assert.assertFalse(userDao.update(NEW_USER));
    }

    @Test
    public void deleteTest() throws Exception {
        Assert.assertTrue(userDao.delete(USER1.getId()));
        Assert.assertNull(userDao.get(USER1.getId()));

        Assert.assertFalse(userDao.delete(INCORRECT_ID));
    }

    @Test
    public void getWithCountTest() throws Exception {
        PaginationDTO<EntriesWithAssociatedPairsDto<User, Long, Long, Short>> dto = userDao.getAll(0, 100);
        EntriesWithAssociatedPairsDto<User, Long, Long, Short> entries = dto.getEntries().get(0);

        List<User> users = entries.getEntries();
        Assert.assertTrue(matcher.compareListsWithoutResults(users, Arrays.asList(USER1, USER2, USER3)));
        Assert.assertEquals(entries.getPairs().size(), 7);
    }

    @Test
    public void addResultTest() throws DAOException {
        Assert.assertTrue(userDao.addResult(USER2.getId(), SUBJECT4.getId(), new Short("173")));
        Assert.assertTrue(userDao.getResultsOfUser(USER1.getId()).containsKey(SUBJECT4.getId()));

        //duplicated
        expected.expect(DAOException.class);
        userDao.addResult(USER2.getId(), SUBJECT4.getId(), new Short("173"));
    }

    @Test
    public void getResultsOfUser() throws DAOException {
        Assert.assertEquals(userDao.getResultsOfUser(USER2.getId()), USER2.getResults());

        Assert.assertEquals(userDao.getResultsOfUser(INCORRECT_ID), Collections.emptyMap());
    }

    @Test
    public void deleteResultTest() throws DAOException {
        Assert.assertTrue(userDao.deleteResult(USER1.getId(), SUBJECT4.getId()));
        Assert.assertFalse(userDao.getResultsOfUser(USER1.getId()).containsKey(SUBJECT4.getId()));

        Assert.assertFalse(userDao.deleteResult(NEW_USER.getId(), SUBJECT1.getId()));
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