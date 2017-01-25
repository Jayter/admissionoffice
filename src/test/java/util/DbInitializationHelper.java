package util;

import javax.sql.DataSource;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.Reader;
import java.sql.Connection;
import java.sql.SQLException;

public class DbInitializationHelper {

    private static final String DB_TEST_RESOURCES_PATH = "src\\test\\resources\\db\\";
    private static final String INIT_DB_PATH = "db\\initTestDb.sql";

    private static DbInitializationHelper instance = new DbInitializationHelper();

    private DataSource dataSource;

    private DbInitializationHelper() {
    }

    public static DbInitializationHelper getInstance() {
        return instance;
    }

    public void initDb() throws SQLException {
        executeScript(DB_TEST_RESOURCES_PATH + INIT_DB_PATH);
    }

    public void executeDbPopulate(String path) throws SQLException {
        executeScript(DB_TEST_RESOURCES_PATH + path);
    }

    private void executeScript(String path) throws SQLException {
        Connection connection = dataSource.getConnection();
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