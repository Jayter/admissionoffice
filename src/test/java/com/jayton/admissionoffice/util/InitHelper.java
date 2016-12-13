package com.jayton.admissionoffice.util;

import com.jayton.admissionoffice.dao.exception.FailedInitializationException;
import com.jayton.admissionoffice.dao.jdbc.pool.PoolHelper;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.Reader;
import java.sql.Connection;
import java.sql.SQLException;

public class InitHelper {

    private static final String TEST_DB_PROPERTIES = "db.testDb";
    private static final String DB_TEST_RESOURCES_PATH = "src\\test\\resources\\db\\";
    private static final String INIT_DB_PATH = "db\\initTestDb.sql";

    private static boolean dataSourceInitialized;

    private InitHelper() {
    }
    static {
        initDefaultDataSource();
    }
    public static void initDefaultDataSource() {
        try {
            initDataSource(TEST_DB_PROPERTIES);
        } catch (FailedInitializationException e) {
            throw new RuntimeException("Failed to init data source.", e);
        }
    }

    public static void initDataSource(String path) throws FailedInitializationException {
        if(dataSourceInitialized) {
            throw new UnsupportedOperationException("Data source is already initialized.");
        }
        PoolHelper.getInstance().initDataSource(path);
        dataSourceInitialized = true;
    }

    public static void initDb() throws SQLException {
        executeScript(DB_TEST_RESOURCES_PATH + INIT_DB_PATH);
    }

    public static void executeDbPopulate(String path) throws SQLException {
        executeScript(DB_TEST_RESOURCES_PATH + path);
    }

    private static void executeScript(String path) throws SQLException {
        Connection connection = PoolHelper.getInstance().getDataSource().getConnection();
        try {
            SqlScriptRunner runner = new SqlScriptRunner(connection, true);

            Reader reader = new BufferedReader(
                    new FileReader(path));

            runner.runScript(reader);

            connection.close();
        } catch (Exception e) {
            System.err.println("Failed to Execute" + path + " The error is " + e.getMessage());
        }
    }
}