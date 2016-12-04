package com.jayton.admissionoffice.controller.listener;

import com.jayton.admissionoffice.dao.jdbc.pool.PoolHelper;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Created by Jayton on 28.11.2016.
 */
public class AdmissionOfficeContextCreate implements ServletContextListener {

    public void contextInitialized(ServletContextEvent sce) {
        PoolHelper.getInstance().initStandardDataSource();
    }

    public void contextDestroyed(ServletContextEvent sce) {
        PoolHelper.getInstance().destroyDataSource();
    }
}
