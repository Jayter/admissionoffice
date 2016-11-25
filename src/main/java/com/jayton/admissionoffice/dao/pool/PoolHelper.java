package com.jayton.admissionoffice.dao.pool;

import com.jayton.admissionoffice.model.Subject;
import org.apache.tomcat.jdbc.pool.DataSource;
import org.apache.tomcat.jdbc.pool.PoolProperties;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Created by Jayton on 24.11.2016.
 */
public class PoolHelper {

    private static final String DB_PROPERTIES_FILE = "db.db";

    private static final String DRIVER = "db.driver";
    private static final String JDBC_URL = "db.url";
    private static final String DB_USERNAME = "db.username";
    private static final String DB_PASSWORD = "db.password";

    private static DataSource dataSource;
    static {
        initDataSource();
    }

    private PoolHelper() {
    }

    private static void initDataSource(){
        ResourceBundle resourceBundle = ResourceBundle.getBundle(DB_PROPERTIES_FILE);

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

    public static DataSource getDataSource() {
        return dataSource;
    }
}
