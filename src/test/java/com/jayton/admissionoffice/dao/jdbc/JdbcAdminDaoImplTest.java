package com.jayton.admissionoffice.dao.jdbc;

import com.jayton.admissionoffice.dao.exception.DAOException;
import com.jayton.admissionoffice.model.user.Admin;
import com.jayton.admissionoffice.util.InitHelper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;

import static com.jayton.admissionoffice.data.AdminTestData.*;

/**
 * Created by Jayton on 25.11.2016.
 */
public class JdbcAdminDaoImplTest {

    private JdbcAdminDaoImpl jdbcAdminDao = JdbcAdminDaoImpl.getInstance();

    @Rule
    public ExpectedException expected = ExpectedException.none();

    @Before
    public void setUpDb() throws Exception {
        InitHelper.populateDb();
    }

    @Test
    public void getTest() throws Exception {
        Admin admin = jdbcAdminDao.get(ADMIN1_ID);

        Assert.assertEquals(admin, ADMIN1);
        Assert.assertNull(jdbcAdminDao.get(INCORRECT_ID));
    }

    @Test
    public void addTest() throws Exception {
        jdbcAdminDao.add(ADMIN4);

        Assert.assertEquals(ADMIN1_ID + 3, ADMIN4.getId());
        System.out.println(jdbcAdminDao.getAll());
        Assert.assertEquals(Arrays.asList(ADMIN1, ADMIN2, ADMIN3, ADMIN4), jdbcAdminDao.getAll());
    }

    @Test
    public void updateTest() throws Exception {
        ADMIN2.setAddress("Дніпропетровськ, Орловська 13");
        ADMIN2.setBirthDate(LocalDate.of(1995, Month.DECEMBER, 12));
        jdbcAdminDao.update(ADMIN2);

        Assert.assertEquals(ADMIN2, jdbcAdminDao.get(ADMIN2.getId()));

        //updating user that was not saved in db
        expected.expect(DAOException.class);
        jdbcAdminDao.update(ADMIN4);
    }

    @Test
    public void deleteTest() throws Exception {
        jdbcAdminDao.delete(ADMIN1_ID + 1);

        Assert.assertEquals(Arrays.asList(ADMIN1, ADMIN3), jdbcAdminDao.getAll());

        //deleting user that was not saved in db
        expected.expect(DAOException.class);
        jdbcAdminDao.delete(ADMIN4);
    }

}