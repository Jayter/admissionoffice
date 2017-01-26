package com.jayton.admissionoffice.dao.jdbc.util;

import com.jayton.admissionoffice.dao.exception.DAOException;
import com.jayton.admissionoffice.util.di.Injected;
import org.apache.tomcat.jdbc.pool.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;

public class DaoHelper {

    private static final Logger logger = LoggerFactory.getLogger(DaoHelper.class);

    @Injected
    private DataSource dataSource;

    public DaoHelper() {
    }

    public void delete(String script, String errorMessage, Long... params) throws DAOException {
        PreparedStatement statement = null;
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            statement = connection.prepareStatement(script);

            if(params == null) {
                throw new DAOException(errorMessage);
            }
            for(int i = 0; i < params.length; i++) {
                statement.setLong(i + 1, params[i]);
            }

            int affectedRows = statement.executeUpdate();
            if(affectedRows == 0) {
                throw new DAOException(errorMessage);
            }
        } catch (SQLException e) {
            throw new DAOException(errorMessage, e);
        }
        finally {
            closeResources(connection, statement);
        }
    }

    public Long getCount(PreparedStatement statement, Long... params) throws SQLException {
        for(int i = 0; i < params.length; i++) {
            statement.setLong(i + 1, params[i]);
        }

        long result = 0;
        try (ResultSet rs = statement.executeQuery()) {
            if(rs.next()) {
                result = rs.getLong(1);
            }
        }
        return result;
    }

    public void rollback(Connection connection) {
        if (connection != null) {
            try {
                connection.rollback();
            } catch (SQLException e) {
                logger.error("Failed to rollback connection.", e);
            }
        }
    }

    public void closeResources(AutoCloseable... resources) {
        try {
            if(resources != null) {
                for(AutoCloseable resource: resources) {
                    if(resource != null) {
                        resource.close();
                    }
                }
            }
        } catch (Exception e) {
            logger.error("Failed to close db resources.", e);
        }
    }
}