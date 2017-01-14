package com.jayton.admissionoffice.dao.jdbc.util;

import com.jayton.admissionoffice.dao.exception.DAOException;
import com.jayton.admissionoffice.dao.jdbc.pool.PoolHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;

public class DaoHelper {

    private static final Logger logger = LoggerFactory.getLogger(DaoHelper.class);

    private DaoHelper() {
    }

    public static void delete(String script, String errorMessage, Long... params) throws DAOException {
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = PoolHelper.getInstance().getConnection();
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
            DaoHelper.closeResources(connection, statement);
        }
    }

    public static Long getCount(String script, String errorMessage, Long... params) throws DAOException {
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = PoolHelper.getInstance().getConnection();
            statement = connection.prepareStatement(script);

            if(params == null) {
                throw new DAOException(errorMessage);
            }
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
        } catch (SQLException e) {
            throw new DAOException(errorMessage, e);
        }
        finally {
            DaoHelper.closeResources(connection, statement);
        }
    }

    public static void rollback(Connection connection) {
        if (connection != null) {
            try {
                connection.rollback();
            } catch (SQLException e) {
                logger.error("Failed to rollback connection.", e);
            }
        }
    }

    public static void closeResources(Connection connection, Statement... statements) {
        try {
            if(statements != null) {
                for(Statement st: statements) {
                    if(st != null) {
                        st.close();
                    }
                }
            }
            if(connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            logger.error("Failed to close db resources.", e);
        }
    }
}