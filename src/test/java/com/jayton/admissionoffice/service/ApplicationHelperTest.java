package com.jayton.admissionoffice.service;

import com.jayton.admissionoffice.dao.ApplicationDao;
import com.jayton.admissionoffice.model.to.Application;
import com.jayton.admissionoffice.model.to.Status;
import com.jayton.admissionoffice.model.university.Direction;
import com.jayton.admissionoffice.service.util.ApplicationHandler;
import com.jayton.admissionoffice.util.di.BeanContextHolder;
import com.jayton.admissionoffice.util.di.exception.InjectionException;
import org.junit.*;
import util.ContextInitializationHelper;
import util.DbInitializationHelper;
import org.junit.rules.ExpectedException;

import java.util.List;

public class ApplicationHelperTest {

    private ApplicationDao applicationDao = (ApplicationDao)
            BeanContextHolder.getInstance().getActualContext().getBean("applicationDao");
    private DirectionService directionService = (DirectionService)
            BeanContextHolder.getInstance().getActualContext().getBean("directionService");
    private ApplicationHandler applicationHandler = (ApplicationHandler)
            BeanContextHolder.getInstance().getActualContext().getBean("applicationHandler");

    @Rule
    public ExpectedException expected = ExpectedException.none();

    @BeforeClass
    public static void initContext() throws InjectionException {
        ContextInitializationHelper helper = ContextInitializationHelper.getInstance();
        helper.initContext("di/dependencies.xml");
    }

    @Before
    public void setUpDb() throws Exception {
        DbInitializationHelper.getInstance().executeDbPopulate("populateForHandlerTest.sql");
    }

    @Test
    public void handleApplicationsTest() throws Exception {
        applicationHandler.handleApplications();

        List<Direction> directions = directionService.getAll();
        for(Direction d: directions) {
            List<Application> apps = applicationDao.getByDirection(d.getId(), 0, 100).getApplications();
            long count = apps.stream().filter(app -> app.getStatus() == Status.APPROVED).count();
            Assert.assertEquals(count, d.getCountOfStudents());
        }
    }
}