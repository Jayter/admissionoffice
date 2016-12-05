package com.jayton.admissionoffice.controller.listener;

import com.jayton.admissionoffice.dao.jdbc.pool.PoolHelper;
import com.jayton.admissionoffice.model.Subject;
import com.jayton.admissionoffice.service.ServiceFactory;
import com.jayton.admissionoffice.service.SubjectService;
import com.jayton.admissionoffice.service.exception.ServiceException;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
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

        List<Subject> subjects = null;
        try {
            subjects = service.getAll();
        } catch (ServiceException e) {
            throw new RuntimeException(e);
        }
        Map<Long, Subject> subjectsMap = new HashMap<>();
        subjects.forEach(subject -> subjectsMap.put(subject.getId(), subject));

        context.setAttribute("subjects", subjectsMap);
    }

    public void contextDestroyed(ServletContextEvent sce) {
        PoolHelper.getInstance().destroyDataSource();
    }
}
