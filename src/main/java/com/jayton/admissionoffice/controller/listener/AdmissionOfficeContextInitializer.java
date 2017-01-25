package com.jayton.admissionoffice.controller.listener;

import com.jayton.admissionoffice.util.di.BeanContextHolder;
import com.jayton.admissionoffice.util.di.XmlBeanContext;
import com.jayton.admissionoffice.util.di.exception.InjectionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class AdmissionOfficeContextInitializer implements ServletContextListener {

    private final Logger logger = LoggerFactory.getLogger(ServletContextListener.class);
    private final String dependenciesPath = "di/dependencies.xml";

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        try {
            XmlBeanContext beanContext = new XmlBeanContext(dependenciesPath);
            beanContext.init();

            ServletContext servletContext = servletContextEvent.getServletContext();
            servletContext.setAttribute("beanContext", beanContext);

            BeanContextHolder beanFactory = BeanContextHolder.getInstance();
            beanFactory.init(beanContext);
        } catch (InjectionException e) {
            logger.error("Failed to initialize beans.", e);
            throw new RuntimeException("Failed to initialize beans.", e);
        }

        logger.info("Data source is initialized.");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        logger.info("Data source is destroyed.");
    }
}