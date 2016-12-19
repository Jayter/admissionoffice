package com.jayton.admissionoffice.controller.listener;

import com.jayton.admissionoffice.dao.exception.FailedInitializationException;
import com.jayton.admissionoffice.dao.jdbc.pool.PoolHelper;
import com.jayton.admissionoffice.model.Subject;
import com.jayton.admissionoffice.model.to.SessionTerms;
import com.jayton.admissionoffice.service.ServiceFactory;
import com.jayton.admissionoffice.service.UtilService;
import com.jayton.admissionoffice.service.exception.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdmissionOfficeContextCreate implements ServletContextListener {

    private final Logger logger = LoggerFactory.getLogger(ServletContextListener.class);

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        try {
            PoolHelper.getInstance().initStandardDataSource();
        } catch (FailedInitializationException e) {
            logger.error("Failed to initialize data source.", e);
            throw new RuntimeException("Failed to initialize data source.");
        }

        ServletContext context = servletContextEvent.getServletContext();
        UtilService utilService = ServiceFactory.getInstance().getUtilService();

        List<Subject> subjects;
        SessionTerms sessionTerms;

        try {
            subjects = utilService.getAllSubjects();
            sessionTerms = utilService.getSessionTerms((short) LocalDate.now().getYear());
        } catch (ServiceException e) {
            logger.error("Failed to load application data.", e);
            throw new RuntimeException("Failed to load application data.", e);
        }

        Map<Long, Subject> subjectsMap = new HashMap<>();
        subjects.forEach(subject -> subjectsMap.put(subject.getId(), subject));

        context.setAttribute("subjects", subjectsMap);
        context.setAttribute("sessionTerms", sessionTerms);
        logger.info("Data source is initialized.");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        PoolHelper.getInstance().destroyDataSource();
        logger.info("Data source is destroyed.");
    }
}