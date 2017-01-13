package com.jayton.admissionoffice.controller.listener;

import com.jayton.admissionoffice.dao.exception.FailedInitializationException;
import com.jayton.admissionoffice.dao.jdbc.pool.PoolHelper;
import com.jayton.admissionoffice.util.di.InjectionResolver;
import com.jayton.admissionoffice.util.di.exception.InjectionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class AdmissionOfficeContextCreate implements ServletContextListener {

    private final Logger logger = LoggerFactory.getLogger(ServletContextListener.class);

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        try {
            PoolHelper.getInstance().initStandardDataSource();
        } catch (FailedInitializationException e) {
            logger.error("Failed to initialize data source.", e);
            throw new RuntimeException("Failed to initialize data source.", e);
        }

        try {
            InjectionResolver resolver = new InjectionResolver();
            resolver.init();
            resolver.parse();
        } catch (InjectionException e) {
            logger.error("Failed to initialize beans.", e);
            throw new RuntimeException("Failed to initialize beans.", e);
        }

        logger.info("Data source is initialized.");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        PoolHelper.getInstance().destroyDataSource();
        logger.info("Data source is destroyed.");
    }
}