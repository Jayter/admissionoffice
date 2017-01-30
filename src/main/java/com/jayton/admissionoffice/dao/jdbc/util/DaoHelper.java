package com.jayton.admissionoffice.dao.jdbc.util;

import com.jayton.admissionoffice.dao.exception.DAOException;
import com.jayton.admissionoffice.util.di.Injected;
import org.apache.tomcat.jdbc.pool.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;

public class DaoHelper {

    private final Logger logger = LoggerFactory.getLogger(DaoHelper.class);

    @Injected
    private DataSource dataSource;

    public DaoHelper() {
    }

    public boolean delete(String script, String errorMessage, Long... params) throws DAOException {
        try(Connection connection = dataSource.getConnection(); PreparedStatement statement =
                connection.prepareStatement(script)) {
            logger.info("Executing delete query: \"%s\".", script);
            if(params == null) {
                throw new DAOException(errorMessage);
            }
            for(int i = 0; i < params.length; i++) {
                statement.setLong(i + 1, params[i]);
            }

            return statement.executeUpdate() != 0;
        } catch (SQLException e) {
            logger.error(errorMessage, e);
            throw new DAOException(errorMessage, e);
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
}