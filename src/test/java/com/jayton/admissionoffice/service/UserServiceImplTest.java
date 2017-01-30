package com.jayton.admissionoffice.service;

import com.jayton.admissionoffice.data.UserMatcher;
import com.jayton.admissionoffice.model.to.AuthorizationResult;
import com.jayton.admissionoffice.model.to.PaginationDTO;
import com.jayton.admissionoffice.model.user.User;
import com.jayton.admissionoffice.service.exception.ServiceException;
import com.jayton.admissionoffice.service.exception.ServiceVerificationException;
import com.jayton.admissionoffice.util.di.BeanContextHolder;
import com.jayton.admissionoffice.util.di.exception.InjectionException;
import org.junit.*;
import org.junit.rules.ExpectedException;
import util.ContextInitializationHelper;
import util.DbInitializationHelper;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;

import static com.jayton.admissionoffice.data.TestData.*;
import static com.jayton.admissionoffice.data.TestData.NEW_USER;

public class UserServiceImplTest {

    private UserService userService = (UserService)
            BeanContextHolder.getInstance().getActualContext().getBean("userService");

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
        DbInitializationHelper.getInstance().executeDbPopulate("populateForServiceTest.sql");
    }

    @Test
    public void addTest() throws Exception {
        long id = userService.add(NEW_USER);
        Assert.assertEquals(NEW_ID, id);

        User added = new User(id, NEW_USER.getName(), NEW_USER.getLastName(), NEW_USER.getAddress(), NEW_USER.getEmail(),
                NEW_USER.getPhoneNumber(), NEW_USER.getBirthDate(), NEW_USER.getAverageMark(), Collections.emptyMap());

        Assert.assertEquals(added, userService.get(id));
    }

    @Test
    public void addWithNullableFieldsTest() throws Exception {
        expected.expect(ServiceException.class);
        userService.add(USER_WITH_NULLABLE_FIELDS);
    }

    @Test
    public void addNullableTest() throws Exception {
        expected.expect(ServiceVerificationException.class);
        userService.add(null);
    }

    @Test
    public void getTest() throws Exception {
        User retrieved = userService.get(USER1.getId());

        Assert.assertTrue(matcher.compare(USER1, retrieved));
        Assert.assertNull(retrieved.getPassword());
    }

    @Test
    public void getUserByIncorrectIdTest() throws Exception {
        Assert.assertNull(userService.get(INCORRECT_ID));
    }

    @Test
    public void getByEmailTest() throws Exception {
        User retrievedByEmail = userService.getByEmail(USER1.getEmail());
        Assert.assertTrue(matcher.compare(USER1, retrievedByEmail));
    }

    @Test
    public void getUserByIncorrectEmailTest() throws Exception {
        Assert.assertNull(userService.getByEmail(INCORRECT_STRING));
    }

    @Test
    public void updateTest() throws Exception {
        Assert.assertTrue(userService.update(UPDATED_USER));
        Assert.assertEquals(UPDATED_USER, userService.get(USER3.getId()));
    }

    @Test
    public void updateNewUserTest() throws Exception {
        Assert.assertFalse(userService.update(NEW_USER));
    }

    @Test
    public void updateUserWithNullableFieldsTest() throws Exception {
        expected.expect(ServiceException.class);
        Assert.assertFalse(userService.update(new User(USER1.getId(), null, USER1.getLastName(), USER1.getAddress(),
                USER1.getEmail(), null, USER1.getBirthDate(), USER1.getAverageMark())));
    }

    @Test
    public void updateNullableTest() throws Exception {
        expected.expect(ServiceVerificationException.class);
        userService.update(null);
    }

    @Test
    public void deleteTest() throws Exception {
        Assert.assertTrue(userService.delete(USER1.getId()));
        Assert.assertNull(userService.get(USER1.getId()));
    }

    @Test
    public void deleteByIncorrectIdTest() throws Exception {
        Assert.assertFalse(userService.delete(INCORRECT_ID));
    }

    @Test
    public void getAllWithCountTest() throws Exception {
        PaginationDTO<User> dto = userService.getAllWithCount(0, 100);

        Assert.assertTrue(matcher.compareListsWithoutResults(dto.getEntries(), Arrays.asList(USER1, USER2, USER3)));
    }

    @Test
    public void addResultTest() throws Exception {
        Assert.assertTrue(userService.addResult(USER2.getId(), SUBJECT4.getId(), new Short("173")));
    }

    @Test
    public void addDuplicatedResultTest() throws Exception {
        expected.expect(ServiceException.class);
        userService.addResult(USER1.getId(), SUBJECT3.getId(), new Short("173"));
    }

    @Test
    public void deleteResultTest() throws Exception {
        Assert.assertTrue(userService.deleteResult(USER1.getId(), SUBJECT4.getId()));
    }

    @Test
    public void deleteNonExistedResultTest() throws Exception {
        Assert.assertFalse(userService.deleteResult(NEW_USER.getId(), SUBJECT1.getId()));
    }

    @Test
    public void authorizeAdminTest() throws Exception {
        Assert.assertEquals(AuthorizationResult.ADMIN, userService.authorize(ADMIN_LOGIN, ADMIN_PASSWORD));
    }

    @Test
    public void authorizeUserTest() throws Exception {
        Assert.assertEquals(AuthorizationResult.USER, userService.authorize(USER_LOGIN, USER_PASSWORD));
    }

    @Test
    public void authorizeByIncorrectCombinationsTest() throws Exception {
        Assert.assertEquals(AuthorizationResult.ABSENT, userService.authorize(ADMIN_LOGIN, USER_PASSWORD));
        Assert.assertEquals(AuthorizationResult.ABSENT, userService.authorize(INCORRECT_STRING, INCORRECT_STRING));
    }
}