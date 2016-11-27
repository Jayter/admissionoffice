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

import static com.jayton.admissionoffice.data.CommonTestData.INCORRECT_ID;
import static com.jayton.admissionoffice.data.DirectionTestData.*;
import static com.jayton.admissionoffice.data.FacultyTestData.*;

/**
 * Created by Jayton on 27.11.2016.
 */
public class JdbcDirectionDaoImplTest {

    private JdbcDirectionDaoImpl jdbcDirectionDao = JdbcDirectionDaoImpl.getInstance();

    @Rule
    public ExpectedException expected = ExpectedException.none();

    @Before
    public void setUpDb() throws Exception {
        InitHelper.executeDbPopulate("populateByDirections.sql");
    }

    @Test
    public void get() throws Exception {
        Assert.assertEquals(jdbcDirectionDao.get(DIRECTION1_ID), DIRECTION1);

        Assert.assertNull(jdbcDirectionDao.get(INCORRECT_ID));
    }

    @Test
    public void getByFaculty() throws Exception {
        List<Direction> directions = jdbcDirectionDao.getByFaculty(FACULTY1_ID);

        Assert.assertEquals(directions, Arrays.asList(DIRECTION1, DIRECTION2, DIRECTION3));

        Assert.assertEquals(jdbcDirectionDao.getByFaculty(INCORRECT_ID), Collections.emptyList());
    }

    @Test
    public void getAll() throws Exception {
        List<Direction> all = jdbcDirectionDao.getAll();

        Assert.assertEquals(all, Arrays.asList(DIRECTION1, DIRECTION2, DIRECTION3, DIRECTION4, DIRECTION5));
    }

    @Test
    public void add() throws Exception {
        jdbcDirectionDao.add(NEW_DIRECTION);

        Assert.assertEquals(NEW_DIRECTION.getId(), new Long(DIRECTION1_ID + 5));

        List<Direction> all = Arrays.asList(DIRECTION1, DIRECTION2, DIRECTION3, DIRECTION4, DIRECTION5, NEW_DIRECTION);
        Assert.assertEquals(jdbcDirectionDao.getAll(), all);

        expected.expect(DAOException.class);
        jdbcDirectionDao.add(DIRECTION_WITH_INCORRECT_OWNER);
    }

    @Test
    public void update() throws Exception {
        DIRECTION3.setCountOfStudents(25);
        jdbcDirectionDao.update(DIRECTION3);

        Assert.assertEquals(jdbcDirectionDao.get(DIRECTION3.getId()), DIRECTION3);

        DIRECTION3.setCountOfStudents(20);
    }

    @Test
    public void delete() throws Exception {
        jdbcDirectionDao.delete(DIRECTION5);
        jdbcDirectionDao.delete(DIRECTION3.getId());

        Assert.assertEquals(jdbcDirectionDao.getAll(), Arrays.asList(DIRECTION1, DIRECTION2, DIRECTION4));

        expected.expect(DAOException.class);
        jdbcDirectionDao.delete(NEW_DIRECTION);
    }

}