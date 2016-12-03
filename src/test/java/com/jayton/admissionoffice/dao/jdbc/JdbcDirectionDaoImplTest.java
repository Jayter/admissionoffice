package com.jayton.admissionoffice.dao.jdbc;

import com.jayton.admissionoffice.dao.DirectionDao;
import com.jayton.admissionoffice.dao.FactoryProducer;
import com.jayton.admissionoffice.dao.exception.DAOException;
import com.jayton.admissionoffice.model.university.Direction;
import com.jayton.admissionoffice.util.InitHelper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.jayton.admissionoffice.data.TestData.*;

/**
 * Created by Jayton on 27.11.2016.
 */
public class JdbcDirectionDaoImplTest {

    private DirectionDao directionDao = FactoryProducer.getInstance().getPostgresDaoFactory().getDirectionDao();

    @Rule
    public ExpectedException expected = ExpectedException.none();

    @Before
    public void setUpDb() throws Exception {
        InitHelper.executeDbPopulate("populateTestDb.sql");
    }

    @Test
    public void get() throws Exception {
        Assert.assertEquals(DIRECTION1, directionDao.get(DIRECTION1.getId()));
        Assert.assertEquals(Collections.emptyMap(),
                directionDao.get(DIRECTION4.getId()).getEntranceSubjects());

        Assert.assertNull(directionDao.get(INCORRECT_ID));
    }

    @Test
    public void getByFaculty() throws Exception {
        List<Direction> directions = directionDao.getByFaculty(FACULTY1.getId());

        Assert.assertEquals(Arrays.asList(DIRECTION1, DIRECTION2, DIRECTION3), directions);

        Assert.assertEquals(Collections.emptyList(), directionDao.getByFaculty(INCORRECT_ID));
    }

    @Test
    public void getAll() throws Exception {
        List<Direction> all = directionDao.getAll();

        Assert.assertEquals(all, Arrays.asList(DIRECTION1, DIRECTION2, DIRECTION3, DIRECTION4, DIRECTION5));
    }

    @Test
    public void add() throws Exception {
        Assert.assertEquals(NEW_ID, directionDao.add(NEW_DIRECTION));

        List<Direction> all = Arrays.asList(DIRECTION1, DIRECTION2, DIRECTION3, DIRECTION4, DIRECTION5, NEW_DIRECTION);
        Assert.assertEquals(directionDao.getAll(), all);
        Assert.assertEquals(directionDao.get(NEW_ID).getEntranceSubjects(), NEW_DIRECTION.getEntranceSubjects());

        expected.expect(DAOException.class);
        directionDao.add(DIRECTION_WITH_INCORRECT_OWNER);
    }

    @Test
    public void update() throws Exception {
        directionDao.update(UPDATED_DIRECTION);

        Assert.assertEquals(UPDATED_DIRECTION, directionDao.get(DIRECTION2.getId()));
    }

    @Test
    public void delete() throws Exception {
        Assert.assertTrue(directionDao.delete(DIRECTION2.getId()));

        Assert.assertEquals(Arrays.asList(DIRECTION1, DIRECTION3, DIRECTION4, DIRECTION5),
                directionDao.getAll());

        expected.expect(DAOException.class);
        directionDao.delete(NEW_DIRECTION.getId());
    }

    @Test
    public void addEntranceSubject() throws Exception {
        directionDao.addSubject(DIRECTION1.getId(), SUBJECT4.getId(), new BigDecimal(0.3));

        Assert.assertEquals(directionDao.get(DIRECTION1.getId()).getEntranceSubjects().size(), 4);

        expected.expect(DAOException.class);
        directionDao.addSubject(INCORRECT_ID, SUBJECT1.getId(), new BigDecimal(0.1));
    }

    @Test
    public void deleteEntranceSubject() throws Exception {
        Assert.assertTrue(directionDao.deleteSubject(DIRECTION2.getId(), SUBJECT1.getId()));

        Assert.assertEquals(directionDao.get(DIRECTION2.getId()).getEntranceSubjects().size(), 2);

        Assert.assertFalse(directionDao.deleteSubject(DIRECTION3.getId(), SUBJECT4.getId()));
    }

    @Test
    public void getByDirection() throws Exception {
        Assert.assertEquals(directionDao.getByDirection(DIRECTION1.getId()), DIRECTION1.getEntranceSubjects());

        Assert.assertEquals(directionDao.getByDirection(INCORRECT_ID), Collections.emptyMap());
    }
}