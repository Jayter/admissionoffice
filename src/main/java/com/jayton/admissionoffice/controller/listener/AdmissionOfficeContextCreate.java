package com.jayton.admissionoffice.controller.listener;

import com.jayton.admissionoffice.dao.FactoryProducer;
import com.jayton.admissionoffice.dao.UtilDao;
import com.jayton.admissionoffice.dao.exception.DAOException;
import com.jayton.admissionoffice.dao.jdbc.pool.PoolHelper;
import com.jayton.admissionoffice.model.Subject;
import com.jayton.admissionoffice.service.ServiceFactory;
import com.jayton.admissionoffice.service.SubjectService;
import com.jayton.admissionoffice.service.exception.ServiceException;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Jayton on 28.11.2016.
 */
public class AdmissionOfficeContextCreate implements ServletContextListener {

    public void contextInitialized(ServletContextEvent servletContextEvent) {
        PoolHelper.getInstance().initStandardDataSource();

        ServletContext context = servletContextEvent.getServletContext();
        SubjectService service = ServiceFactory.getInstance().getSubjectService();
        UtilDao utilDao = FactoryProducer.getInstance().getPostgresDaoFactory().getUtilDao();

        List<Subject> subjects;
        List<LocalDateTime> sessionTerms;
        try {
            subjects = service.getAll();
            sessionTerms = utilDao.getSessionDate(LocalDate.now().getYear());
        } catch (ServiceException | DAOException e) {
            throw new RuntimeException(e);
        }
        Map<Long, Subject> subjectsMap = new HashMap<>();
        subjects.forEach(subject -> subjectsMap.put(subject.getId(), subject));

        context.setAttribute("subjects", subjectsMap);
        context.setAttribute("sessionStart", sessionTerms.get(0));
        context.setAttribute("sessionEnd", sessionTerms.get(1));
    }

    public void contextDestroyed(ServletContextEvent sce) {
        PoolHelper.getInstance().destroyDataSource();
    }
}
