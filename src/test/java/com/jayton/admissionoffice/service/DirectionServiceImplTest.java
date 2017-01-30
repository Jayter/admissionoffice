package com.jayton.admissionoffice.service;

import com.jayton.admissionoffice.data.DirectionMatcher;
import com.jayton.admissionoffice.model.to.PaginationDTO;
import com.jayton.admissionoffice.model.university.Direction;
import com.jayton.admissionoffice.service.exception.ServiceException;
import com.jayton.admissionoffice.service.exception.ServiceVerificationException;
import com.jayton.admissionoffice.util.di.BeanContextHolder;
import com.jayton.admissionoffice.util.di.exception.InjectionException;
import org.junit.*;
import org.junit.rules.ExpectedException;
import util.ContextInitializationHelper;
import util.DbInitializationHelper;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.jayton.admissionoffice.data.TestData.*;

public class DirectionServiceImplTest {

    private DirectionService directionService = (DirectionService)
            BeanContextHolder.getInstance().getActualContext().getBean("directionService");

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
        DbInitializationHelper.getInstance().executeDbPopulate("populateForServiceTest.sql");
    }

    @Test
    public void addTest() throws Exception {
        long id = directionService.add(NEW_DIRECTION);
        Assert.assertEquals(NEW_ID, id);

        Direction added = new Direction(id, NEW_DIRECTION.getName(), NEW_DIRECTION.getAverageCoefficient(),
                NEW_DIRECTION.getCountOfStudents(), NEW_DIRECTION.getFacultyId(), NEW_DIRECTION.getEntranceSubjects());
        Assert.assertTrue(matcher.compare(added, directionService.get(id)));
    }

    @Test
    public void addWithIncorrectFacultyTest() throws Exception {
        expected.expect(ServiceException.class);
        directionService.add(DIRECTION_WITH_INCORRECT_OWNER);
    }

    @Test
    public void addWithNullableFieldsTest() throws Exception {
        expected.expect(ServiceException.class);
        directionService.add(DIRECTION_WITH_NULLABLE_FIELDS);
    }

    @Test
    public void addNullableTest() throws Exception {
        expected.expect(ServiceException.class);
        directionService.add(null);
    }

    @Test
    public void getTest() throws Exception {
        Direction retrieved = directionService.get(DIRECTION1.getId());
        Assert.assertTrue(matcher.compare(retrieved, DIRECTION1));

        Assert.assertEquals(Collections.emptyMap(),
                directionService.get(DIRECTION4.getId()).getEntranceSubjects());
    }

    @Test
    public void getByIncorrectIdTest() throws Exception {
        Assert.assertNull(directionService.get(INCORRECT_ID));
    }

    @Test
    public void updateTest() throws Exception {
        Assert.assertTrue(directionService.update(UPDATED_DIRECTION));

        Assert.assertTrue(matcher.compare(UPDATED_DIRECTION, directionService.get(DIRECTION2.getId())));
    }

    @Test
    public void updateNewTest() throws Exception {
        Assert.assertFalse(directionService.update(NEW_DIRECTION));
    }

    @Test
    public void updateWithNullableFieldsTest() throws Exception {
        expected.expect(ServiceException.class);
        Assert.assertFalse(directionService.update(DIRECTION_WITH_NULLABLE_FIELDS));
    }

    @Test
    public void updateNullableTest() throws Exception {
        expected.expect(ServiceVerificationException.class);
        Assert.assertFalse(directionService.update(null));
    }

    @Test
    public void deleteTest() throws Exception {
        directionService.delete(DIRECTION2.getId());

        Assert.assertFalse(directionService.getAll().contains(DIRECTION2));
    }

    @Test
    public void deleteByIncorrectIdTest() throws Exception {
        Assert.assertFalse(directionService.delete(INCORRECT_ID));
    }

    @Test
    public void getByFacultyTest() throws Exception {
        PaginationDTO<Direction> dto = directionService.getWithCountByFaculty(FACULTY1.getId(), 0, 100);

        System.out.println(dto.getEntries());
        Assert.assertTrue(matcher.compareLists(dto.getEntries(), Arrays.asList(DIRECTION1, DIRECTION2, DIRECTION3)));
        Assert.assertEquals(dto.getCount(), 3);
    }

    @Test
    public void getByFacultyWithSingleResultTest() throws Exception {
        PaginationDTO<Direction> singleDto = directionService.getWithCountByFaculty(FACULTY1.getId(), 0, 1);

        Assert.assertTrue(matcher.compareLists(singleDto.getEntries(), Collections.singletonList(DIRECTION1)));
        Assert.assertEquals(singleDto.getCount(), 3);
    }

    @Test
    public void getByFacultyByIncorrectIdTest() throws Exception {
        PaginationDTO<Direction> emptyDto = directionService.getWithCountByFaculty(INCORRECT_ID, 0, 100);

        Assert.assertEquals(Collections.emptyList(), emptyDto.getEntries());
    }

    @Test
    public void getAllTest() throws Exception {
        List<Direction> retrieved = directionService.getAll();

        Assert.assertTrue(matcher.compareLists(retrieved, Arrays.asList(DIRECTION1, DIRECTION2,
                DIRECTION4, DIRECTION5, DIRECTION3)));
    }

    @Test
    public void addSubjectTest() throws Exception {
        Assert.assertTrue(directionService.addEntranceSubject(DIRECTION3.getId(), SUBJECT3.getId(), new BigDecimal(0.2)));
    }

    @Test
    public void addSubjectOverCoefficientTest() throws Exception {
        expected.expect(ServiceVerificationException.class);
        expected.expectMessage("Total coefficient must be strictly equal to 1.");
        Assert.assertTrue(directionService.addEntranceSubject(DIRECTION3.getId(), SUBJECT3.getId(), new BigDecimal(0.4)));
    }

    @Test
    public void addSubjectLoverCoefficientTest() throws Exception {
        expected.expect(ServiceVerificationException.class);
        expected.expectMessage("Total coefficient must be strictly equal to 1.");
        Assert.assertTrue(directionService.addEntranceSubject(DIRECTION3.getId(), SUBJECT3.getId(), new BigDecimal(0.05)));
    }

    @Test
    public void addSubjectOverLimitTest() throws Exception {
        expected.expect(ServiceVerificationException.class);
        expected.expectMessage("Cannot add more than 3 entrance subjects.");
        Assert.assertTrue(directionService.addEntranceSubject(DIRECTION1.getId(), SUBJECT4.getId(), new BigDecimal(0.3)));
    }

    @Test
    public void addSubjectWithIncorrectIdTest() throws Exception {
        expected.expect(ServiceException.class);
        directionService.addEntranceSubject(INCORRECT_ID, SUBJECT1.getId(), new BigDecimal(0.1));
    }

    @Test
    public void addSubjectWithNullableCoefficientTest() throws Exception {
        expected.expect(ServiceException.class);
        directionService.addEntranceSubject(INCORRECT_ID, SUBJECT1.getId(), null);
    }

    @Test
    public void deleteSubjectTest() throws Exception {
        Assert.assertTrue(directionService.deleteEntranceSubject(DIRECTION2.getId(), SUBJECT1.getId()));

        Assert.assertEquals(directionService.get(DIRECTION2.getId()).getEntranceSubjects().size(), 2);
    }

    @Test
    public void deleteNonExistedSubjectTest() throws Exception {
        Assert.assertFalse(directionService.deleteEntranceSubject(DIRECTION3.getId(), SUBJECT2.getId()));
    }
}