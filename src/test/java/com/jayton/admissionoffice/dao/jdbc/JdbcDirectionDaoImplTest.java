package com.jayton.admissionoffice.dao.jdbc;

import com.jayton.admissionoffice.dao.DirectionDao;
import com.jayton.admissionoffice.dao.data.DirectionMatcher;
import com.jayton.admissionoffice.dao.exception.DAOException;
import com.jayton.admissionoffice.model.to.EntriesWithAssociatedPairsDto;
import com.jayton.admissionoffice.model.to.PaginationDTO;
import com.jayton.admissionoffice.model.university.Direction;
import com.jayton.admissionoffice.util.di.BeanContextHolder;
import com.jayton.admissionoffice.util.di.exception.InjectionException;
import org.junit.*;
import util.ContextInitializationHelper;
import util.DbInitializationHelper;
import org.junit.rules.ExpectedException;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static com.jayton.admissionoffice.dao.data.TestData.*;

public class JdbcDirectionDaoImplTest {

    private DirectionDao directionDao = (DirectionDao)
            BeanContextHolder.getInstance().getActualContext().getBean("directionDao");

    private DirectionMatcher matcher = new DirectionMatcher();

    @Rule
    public ExpectedException expected = ExpectedException.none();

    @BeforeClass
    public static void initContext() throws InjectionException {
        ContextInitializationHelper helper = ContextInitializationHelper.getInstance();
        helper.initContext("di/dependencies.xml");
    }

    @Before
    public void setUpDb() throws Exception {
        DbInitializationHelper.getInstance().executeDbPopulate("populateForDaoTest.sql");
    }

    @Test
    public void addTest() throws Exception {
        long id = directionDao.add(NEW_DIRECTION);
        Assert.assertEquals(NEW_ID, id);

        Direction added = new Direction(id, NEW_DIRECTION.getName(), NEW_DIRECTION.getAverageCoefficient(),
                NEW_DIRECTION.getCountOfStudents(), NEW_DIRECTION.getFacultyId(), NEW_DIRECTION.getEntranceSubjects());
        Assert.assertTrue(matcher.compare(added, directionDao.get(id)));
    }

    @Test
    public void addWithIncorrectFacultyTest() throws Exception {
        expected.expect(DAOException.class);
        directionDao.add(DIRECTION_WITH_INCORRECT_OWNER);
    }

    @Test
    public void addWithNullableFieldsTest() throws Exception {
        expected.expect(DAOException.class);
        directionDao.add(DIRECTION_WITH_NULLABLE_FIELDS);
    }

    @Test
    public void getTest() throws Exception {
        Direction retrieved = directionDao.get(DIRECTION1.getId());
        Assert.assertTrue(matcher.compare(retrieved, DIRECTION1));

        Assert.assertEquals(Collections.emptyMap(),
                directionDao.get(DIRECTION4.getId()).getEntranceSubjects());
    }

    @Test
    public void getByIncorrectIdTest() throws Exception {
        Assert.assertNull(directionDao.get(INCORRECT_ID));
    }

    @Test
    public void updateTest() throws Exception {
        Assert.assertTrue(directionDao.update(UPDATED_DIRECTION));

        Assert.assertTrue(matcher.compare(UPDATED_DIRECTION, directionDao.get(DIRECTION2.getId())));
    }

    @Test
    public void updateNewTest() throws Exception {
        Assert.assertFalse(directionDao.update(NEW_DIRECTION));
    }

    @Test
    public void updateWithNullableFieldsTest() throws Exception {
        expected.expect(DAOException.class);
        Assert.assertFalse(directionDao.update(DIRECTION_WITH_NULLABLE_FIELDS));
    }

    @Test
    public void deleteTest() throws Exception {
        directionDao.delete(DIRECTION2.getId());

        List<Direction> all = Arrays.asList(DIRECTION1, DIRECTION3, DIRECTION4, DIRECTION5);
        Assert.assertTrue(matcher.compareListsWithoutSubjects(all, directionDao.getAll().getEntries()));
    }

    @Test
    public void deleteByIncorrectIdTest() throws Exception {
        Assert.assertFalse(directionDao.delete(INCORRECT_ID));
    }

    @Test
    public void getByFacultyTest() throws Exception {
        PaginationDTO<EntriesWithAssociatedPairsDto<Direction, Long, Long, BigDecimal>> dto =
                directionDao.getWithCountByFaculty(FACULTY1.getId(), 0, 100);
        EntriesWithAssociatedPairsDto<Direction, Long, Long, BigDecimal> retrievedEntries = dto.getEntries().get(0);

        Assert.assertTrue(matcher.compareListsWithoutSubjects(retrievedEntries.getEntries(), Arrays.asList(DIRECTION1, DIRECTION2, DIRECTION3)));
        Assert.assertEquals(dto.getCount(), 3);
        Assert.assertEquals(retrievedEntries.getPairs().size(), 7);
    }

    @Test
    public void getByFacultyWithSingleResultTest() throws Exception {
        PaginationDTO<EntriesWithAssociatedPairsDto<Direction, Long, Long, BigDecimal>> singleDto =
                directionDao.getWithCountByFaculty(FACULTY1.getId(), 0, 1);
        EntriesWithAssociatedPairsDto<Direction, Long, Long, BigDecimal> singleEntry = singleDto.getEntries().get(0);

        Assert.assertTrue(matcher.compareListsWithoutSubjects(singleEntry.getEntries(), Collections.singletonList(DIRECTION1)));
        Assert.assertEquals(singleDto.getCount(), 3);
    }

    @Test
    public void getByFacultyByIncorrectIdTest() throws Exception {
        PaginationDTO<EntriesWithAssociatedPairsDto<Direction, Long, Long, BigDecimal>> emptyDto =
                directionDao.getWithCountByFaculty(INCORRECT_ID, 0, 100);
        EntriesWithAssociatedPairsDto<Direction, Long, Long, BigDecimal> emptyEntries = emptyDto.getEntries().get(0);
        Assert.assertEquals(Collections.emptyList(), emptyEntries.getEntries());
    }


    @Test
    public void getAllTest() throws Exception {
        EntriesWithAssociatedPairsDto<Direction, Long, Long, BigDecimal> retrieved = directionDao.getAll();

        Assert.assertTrue(matcher.compareListsWithoutSubjects(retrieved.getEntries(), Arrays.asList(DIRECTION1, DIRECTION2,
                DIRECTION4, DIRECTION5, DIRECTION3)));
    }

    @Test
    public void addSubjectTest() throws Exception {
        Assert.assertTrue(directionDao.addSubject(DIRECTION1.getId(), SUBJECT4.getId(), new BigDecimal(0.3)));

        Assert.assertEquals(directionDao.get(DIRECTION1.getId()).getEntranceSubjects().size(), 4);
    }

    @Test
    public void addSubjectWithIncorrectIdTest() throws Exception {
        expected.expect(DAOException.class);
        directionDao.addSubject(INCORRECT_ID, SUBJECT1.getId(), new BigDecimal(0.1));
    }

    @Test
    public void deleteSubjectTest() throws Exception {
        Assert.assertTrue(directionDao.deleteSubject(DIRECTION2.getId(), SUBJECT1.getId()));

        Assert.assertEquals(directionDao.get(DIRECTION2.getId()).getEntranceSubjects().size(), 2);
    }

    @Test
    public void deleteNonExistedSubjectTest() throws Exception {
        Assert.assertFalse(directionDao.deleteSubject(DIRECTION3.getId(), SUBJECT4.getId()));
    }

    @Test
    public void getSubjectsTest() throws Exception {
        Map<Long, BigDecimal> subjects = directionDao.getSubjects(DIRECTION1.getId());
        Assert.assertTrue(matcher.compareSubjects(subjects, DIRECTION1.getEntranceSubjects()));
    }

    @Test
    public void getSubjectsByIncorrectIdTest() throws Exception {
        Assert.assertEquals(directionDao.getSubjects(INCORRECT_ID), Collections.emptyMap());
    }
}