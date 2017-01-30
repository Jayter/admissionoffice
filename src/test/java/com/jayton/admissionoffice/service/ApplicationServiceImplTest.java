package com.jayton.admissionoffice.service;

import com.jayton.admissionoffice.data.ApplicationMatcher;
import com.jayton.admissionoffice.model.to.Application;
import com.jayton.admissionoffice.model.to.ApplicationDto;
import com.jayton.admissionoffice.model.to.Status;
import com.jayton.admissionoffice.service.exception.ServiceException;
import com.jayton.admissionoffice.util.di.BeanContextHolder;
import com.jayton.admissionoffice.util.di.exception.InjectionException;
import org.junit.*;
import org.junit.rules.ExpectedException;
import util.ContextInitializationHelper;
import util.DbInitializationHelper;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.jayton.admissionoffice.data.TestData.*;

public class ApplicationServiceImplTest {

    private ApplicationService applicationService = (ApplicationService)
            BeanContextHolder.getInstance().getActualContext().getBean("applicationService");

    private ApplicationMatcher matcher = new ApplicationMatcher();

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
    public void addDuplicatedTest() throws Exception {
        expected.expect(ServiceException.class);
        applicationService.add(USER1, DUPLICATED_APPLICATION.getDirectionId(), DUPLICATED_APPLICATION.getCreationTime());
    }

    @Test
    public void getTest() throws Exception {
        Application retrieved = applicationService.get(APPLICATION3.getId());

        Assert.assertTrue(matcher.compare(retrieved, APPLICATION3));
    }

    @Test
    public void getByIncorrectIdTest() throws Exception {
        Assert.assertNull(applicationService.get(INCORRECT_ID));
    }

    @Test
    public void updateTest() throws Exception {
        Assert.assertTrue(applicationService.update(APPLICATION2.getId(), Status.APPROVED));

        Assert.assertTrue(matcher.compare(UPDATED_APPLICATION, applicationService.get(APPLICATION2.getId())));
    }

    @Test
    public void updateByIncorrectIdTest() throws Exception {
        Assert.assertFalse(applicationService.update(INCORRECT_ID, Status.APPROVED));
    }

    @Test
    public void deleteTest() throws Exception {
        Assert.assertTrue(applicationService.delete(APPLICATION1.getId()));

        Assert.assertFalse(applicationService.getByUser(USER1.getId()).contains(APPLICATION1));
    }

    @Test
    public void deleteByIncorrectIdTest() throws Exception {
        Assert.assertFalse(applicationService.delete(INCORRECT_ID));
    }

    @Test
    public void getByUserTest() throws Exception {
        List<Application> list = Arrays.asList(APPLICATION1, APPLICATION3, APPLICATION2);
        Assert.assertTrue(matcher.compareLists(list, applicationService.getByUser(USER1.getId())));
    }

    @Test
    public void getByUserByIncorrectId() throws Exception {
        Assert.assertEquals(Collections.emptyList(), applicationService.getByUser(INCORRECT_ID));
    }

    @Test
    public void getByDirectionTest() throws Exception {
        ApplicationDto dto = applicationService.getByDirection(DIRECTION1.getId(), 0, 5);

        Assert.assertTrue(matcher.compareLists(Collections.singletonList(APPLICATION2), dto.getApplications()));
        Assert.assertEquals(dto.getCount(), 1);
    }

    @Test
    public void getByDirectionByIncorrectIdTest() throws Exception {
        Assert.assertEquals(Collections.emptyList(), applicationService.getByDirection(INCORRECT_ID, 0, 5).getApplications());
    }

}