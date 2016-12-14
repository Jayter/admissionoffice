package com.jayton.admissionoffice.service;

import com.jayton.admissionoffice.dao.ApplicationDao;
import com.jayton.admissionoffice.dao.DirectionDao;
import com.jayton.admissionoffice.dao.FactoryProducer;
import com.jayton.admissionoffice.model.to.Application;
import com.jayton.admissionoffice.model.to.Status;
import com.jayton.admissionoffice.model.university.Direction;
import com.jayton.admissionoffice.service.util.ApplicationHandler;
import com.jayton.admissionoffice.util.InitHelper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.List;

public class ApplicationHelperTest {

    @Rule
    public ExpectedException expected = ExpectedException.none();

    @Before
    public void setUpDb() throws Exception {
        InitHelper.executeDbPopulate("populateForHandlerTest.sql");
    }

    @Test
    public void handleApplicationsTest() throws Exception {
        ApplicationHandler handler = new ApplicationHandler();
        handler.handleApplications();

        ApplicationDao dao = FactoryProducer.getInstance().getPostgresDaoFactory().getApplicationDao();
        DirectionDao directionDao = FactoryProducer.getInstance().getPostgresDaoFactory().getDirectionDao();

        List<Direction> directions = directionDao.getAll();
        for(Direction d: directions) {
            List<Application> apps = dao.getByDirection(d.getId());
            long count = apps.stream().filter(app -> app.getStatus() == Status.APPROVED).count();
            Assert.assertEquals(count, d.getCountOfStudents());
        }
    }
}