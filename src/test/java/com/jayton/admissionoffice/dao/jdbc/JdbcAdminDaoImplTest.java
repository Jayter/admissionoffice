package com.jayton.admissionoffice.dao.jdbc;

import com.jayton.admissionoffice.dao.exception.DAOException;
import com.jayton.admissionoffice.model.user.Admin;
import com.jayton.admissionoffice.util.InitHelper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;
import java.util.List;

import static com.jayton.admissionoffice.data.AdminTestData.*;
import static com.jayton.admissionoffice.data.CommonTestData.INCORRECT_ID;

/**
 * Created by Jayton on 25.11.2016.
 */
public class JdbcAdminDaoImplTest {

    private JdbcAdminDaoImpl jdbcAdminDao = JdbcAdminDaoImpl.getInstance();

    @Rule
    public ExpectedException expected = ExpectedException.none();

    @Before
    public void setUpDb() throws SQLException {
        InitHelper.executeDbPopulate("populateByAdmins.sql");
    }

    @Test
    public void getTest() throws Exception {
        Admin admin = jdbcAdminDao.get(ADMIN1_ID);

        Assert.assertEquals(admin, ADMIN1);

        Assert.assertNull(jdbcAdminDao.get(INCORRECT_ID));
    }

    @Test
    public void getAllTest() throws Exception {
        List<Admin> actual = jdbcAdminDao.getAll();

        Assert.assertEquals(Arrays.asList(ADMIN1, ADMIN2, ADMIN3), actual);
    }

    @Test
    public void addTest() throws Exception {
        jdbcAdminDao.add(NEW_ADMIN);

        Assert.assertEquals(ADMIN1_ID + 3, NEW_ADMIN.getId());

        Assert.assertEquals(Arrays.asList(ADMIN1, ADMIN2, ADMIN3, NEW_ADMIN), jdbcAdminDao.getAll());

        expected.expect(DAOException.class);
        jdbcAdminDao.add(ADMIN_WITH_NULLABLE_FIELDS);
    }

    @Test
    public void updateTest() throws Exception {
        ADMIN2.setAddress("Дніпропетровськ, Орловська 13");
        ADMIN2.setBirthDate(LocalDate.of(1995, Month.DECEMBER, 12));
        jdbcAdminDao.update(ADMIN2);

        Assert.assertEquals(ADMIN2, jdbcAdminDao.get(ADMIN2.getId()));

        //reset to initial state
        ADMIN2.setAddress("Львів, Некрасова 12");
        ADMIN2.setBirthDate(LocalDate.of(1995, Month.DECEMBER, 11));

        //updating user that was not saved in db
        expected.expect(DAOException.class);
        jdbcAdminDao.update(NEW_ADMIN);
        System.out.println("here too");
    }

    @Test
    public void deleteTest() throws Exception {
        jdbcAdminDao.delete(ADMIN1_ID + 1);

        Assert.assertEquals(Arrays.asList(ADMIN1, ADMIN3), jdbcAdminDao.getAll());

        //deleting user that was not saved in db
        expected.expect(DAOException.class);
        jdbcAdminDao.delete(NEW_ADMIN);
    }

}