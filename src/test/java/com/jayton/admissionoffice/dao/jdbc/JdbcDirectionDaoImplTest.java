package com.jayton.admissionoffice.dao.jdbc;

import com.jayton.admissionoffice.dao.exception.DAOException;
import com.jayton.admissionoffice.model.university.Direction;
import com.jayton.admissionoffice.util.InitHelper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.jayton.admissionoffice.data.TestData.*;

/**
 * Created by Jayton on 27.11.2016.
 */
public class JdbcDirectionDaoImplTest {

    private JdbcDirectionDaoImpl jdbcDirectionDao = JdbcDirectionDaoImpl.getInstance();

    @Rule
    public ExpectedException expected = ExpectedException.none();

    @Before
    public void setUpDb() throws Exception {
        InitHelper.executeDbPopulate("populateTestDb.sql");
    }

    @Test
    public void get() throws Exception {
        Assert.assertEquals(DIRECTION1, jdbcDirectionDao.get(DIRECTION1.getId()));

        Assert.assertNull(jdbcDirectionDao.get(INCORRECT_ID));
    }

    @Test
    public void getByFaculty() throws Exception {
        List<Direction> directions = jdbcDirectionDao.getByFaculty(FACULTY1.getId());

        Assert.assertEquals(Arrays.asList(DIRECTION1, DIRECTION2, DIRECTION3), directions);

        Assert.assertEquals(Collections.emptyList(), jdbcDirectionDao.getByFaculty(INCORRECT_ID));
    }

    @Test
    public void getAll() throws Exception {
        List<Direction> all = jdbcDirectionDao.getAll();

        Assert.assertEquals(all, Arrays.asList(DIRECTION1, DIRECTION2, DIRECTION3, DIRECTION4, DIRECTION5));
    }

    @Test
    public void add() throws Exception {
        Assert.assertEquals(NEW_ID, jdbcDirectionDao.add(NEW_DIRECTION));

        List<Direction> all = Arrays.asList(DIRECTION1, DIRECTION2, DIRECTION3, DIRECTION4, DIRECTION5, NEW_DIRECTION);
        Assert.assertEquals(jdbcDirectionDao.getAll(), all);

        expected.expect(DAOException.class);
        jdbcDirectionDao.add(DIRECTION_WITH_INCORRECT_OWNER);
    }

    @Test
    public void update() throws Exception {
        jdbcDirectionDao.update(UPDATED_DIRECTION);

        Assert.assertEquals(UPDATED_DIRECTION, jdbcDirectionDao.get(DIRECTION3.getId()));
    }

    @Test
    public void delete() throws Exception {
        Assert.assertTrue(jdbcDirectionDao.delete(DIRECTION3.getId()));

        Assert.assertEquals(Arrays.asList(DIRECTION1, DIRECTION2, DIRECTION4, DIRECTION5),
                jdbcDirectionDao.getAll());

        expected.expect(DAOException.class);
        jdbcDirectionDao.delete(NEW_DIRECTION.getId());
    }
}