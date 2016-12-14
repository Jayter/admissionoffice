package com.jayton.admissionoffice.dao.jdbc;

import com.jayton.admissionoffice.dao.DirectionDao;
import com.jayton.admissionoffice.dao.FactoryProducer;
import com.jayton.admissionoffice.dao.exception.DAOException;
import com.jayton.admissionoffice.dao.data.DirectionMatcher;
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
import java.util.Map;

import static com.jayton.admissionoffice.dao.data.TestData.*;

public class JdbcDirectionDaoImplTest {

    private DirectionDao directionDao = FactoryProducer.getInstance().getPostgresDaoFactory().getDirectionDao();

    private DirectionMatcher matcher = new DirectionMatcher();

    @Rule
    public ExpectedException expected = ExpectedException.none();

    @Before
    public void setUpDb() throws Exception {
        InitHelper.executeDbPopulate("populateTestDb.sql");
    }

    @Test
    public void getTest() throws Exception {
        Direction retrieved = directionDao.get(DIRECTION1.getId());
        Assert.assertTrue(matcher.equals(retrieved, DIRECTION1));

        Assert.assertEquals(Collections.emptyMap(),
                directionDao.get(DIRECTION4.getId()).getEntranceSubjects());

        Assert.assertNull(directionDao.get(INCORRECT_ID));
    }

    @Test
    public void getByFacultyTest() throws Exception {
        List<Direction> retrieved = directionDao.getByFaculty(FACULTY1.getId());

        Assert.assertTrue(matcher.equals(retrieved, Arrays.asList(DIRECTION1, DIRECTION2, DIRECTION3)));

        Assert.assertEquals(Collections.emptyList(), directionDao.getByFaculty(INCORRECT_ID));
    }

    @Test
    public void getAllTest() throws Exception {
        List<Direction> retrieved = directionDao.getAll();

        Assert.assertTrue(matcher.equals(retrieved, Arrays.asList(DIRECTION1, DIRECTION2,
                DIRECTION4, DIRECTION5, DIRECTION3)));
    }

    @Test
    public void addTest() throws Exception {
        Assert.assertEquals(NEW_ID, directionDao.add(NEW_DIRECTION).getId());

        List<Direction> all = Arrays.asList(DIRECTION1, DIRECTION2, DIRECTION3, DIRECTION4, DIRECTION5, NEW_DIRECTION);
        Assert.assertTrue(matcher.equals(directionDao.getAll(), all));

        expected.expect(DAOException.class);
        directionDao.add(DIRECTION_WITH_INCORRECT_OWNER);
    }

    @Test
    public void updateTest() throws Exception {
        directionDao.update(UPDATED_DIRECTION);

        Assert.assertTrue(matcher.equals(UPDATED_DIRECTION, directionDao.get(DIRECTION2.getId())));
    }

    @Test
    public void deleteTest() throws Exception {
        directionDao.delete(DIRECTION2.getId());

        List<Direction> all = Arrays.asList(DIRECTION1, DIRECTION3, DIRECTION4, DIRECTION5);
        Assert.assertTrue(matcher.equals(all, directionDao.getAll()));

        expected.expect(DAOException.class);
        directionDao.delete(NEW_DIRECTION.getId());
    }

    @Test
    public void addEntranceSubjectTest() throws Exception {
        directionDao.addSubject(DIRECTION1.getId(), SUBJECT4.getId(), new BigDecimal(0.3));

        Assert.assertEquals(directionDao.get(DIRECTION1.getId()).getEntranceSubjects().size(), 4);

        expected.expect(DAOException.class);
        directionDao.addSubject(INCORRECT_ID, SUBJECT1.getId(), new BigDecimal(0.1));
    }

    @Test
    public void deleteEntranceSubjectTest() throws Exception {
        directionDao.deleteSubject(DIRECTION2.getId(), SUBJECT1.getId());

        Assert.assertEquals(directionDao.get(DIRECTION2.getId()).getEntranceSubjects().size(), 2);

        expected.expect(DAOException.class);
        directionDao.deleteSubject(DIRECTION3.getId(), SUBJECT4.getId());
    }

    @Test
    public void getByDirectionTest() throws Exception {
        Map<Long, BigDecimal> subjects = directionDao.getEntranceSubjects(DIRECTION1.getId());
        Assert.assertTrue(matcher.compareSubjects(subjects, DIRECTION1.getEntranceSubjects()));

        Assert.assertEquals(directionDao.getEntranceSubjects(INCORRECT_ID), Collections.emptyMap());
    }
}