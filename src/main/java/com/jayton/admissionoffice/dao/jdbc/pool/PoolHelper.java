package com.jayton.admissionoffice.dao.jdbc.pool;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.apache.tomcat.jdbc.pool.PoolProperties;

import java.util.ResourceBundle;

/**
 * Created by Jayton on 24.11.2016.
 */
public class PoolHelper {

    private static final String STANDARD_DB_PROPERTIES = "db.db";

    private static final String DRIVER = "db.driver";
    private static final String JDBC_URL = "db.url";
    private static final String DB_USERNAME = "db.username";
    private static final String DB_PASSWORD = "db.password";

    private DataSource dataSource;

    private static PoolHelper instance;

    private PoolHelper() {
    }

    public static PoolHelper getInstance() {
        if(instance == null) {
            instance = new PoolHelper();
        }
        return instance;
    }


    //should be called from listener
    public void initStandardDataSource() {
        initDataSource(STANDARD_DB_PROPERTIES);
    }

    public void initDataSource(String path) {
        ResourceBundle resourceBundle = ResourceBundle.getBundle(path);

        String url = resourceBundle.getString(JDBC_URL);
        String driver = resourceBundle.getString(DRIVER);
        String username = resourceBundle.getString(DB_USERNAME);
        String password = resourceBundle.getString(DB_PASSWORD);

        PoolProperties properties = new PoolProperties();
        properties.setUrl(url);
        properties.setDriverClassName(driver);
        properties.setUsername(username);
        properties.setPassword(password);

        dataSource = new DataSource(properties);
    }

    public void destroyDataSource() {
        dataSource.close();
    }

    public DataSource getDataSource() {
        return dataSource;
    }
}
