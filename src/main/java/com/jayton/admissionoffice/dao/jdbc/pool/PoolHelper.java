package com.jayton.admissionoffice.dao.jdbc.pool;

import com.jayton.admissionoffice.dao.exception.FailedInitializationException;
import org.apache.tomcat.jdbc.pool.DataSource;
import org.apache.tomcat.jdbc.pool.PoolProperties;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ResourceBundle;

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

    public void initStandardDataSource() throws FailedInitializationException {
        initDataSource(STANDARD_DB_PROPERTIES);
    }

    public void initDataSource(String path) throws FailedInitializationException {
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

        try {
            Connection connection = dataSource.getConnection();
        } catch (SQLException e) {
            throw new FailedInitializationException("Failed to init data source.", e);
        }
    }

    public void destroyDataSource() {
        dataSource.close();
    }

    public DataSource getDataSource() {
        return dataSource;
    }
}