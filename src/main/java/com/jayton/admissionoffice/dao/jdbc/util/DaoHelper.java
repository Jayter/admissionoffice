package com.jayton.admissionoffice.dao.jdbc.util;

import com.jayton.admissionoffice.dao.exception.DAOException;
import com.jayton.admissionoffice.dao.jdbc.pool.PoolHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class DaoHelper {

    private DaoHelper() {
    }

    public static void delete(String script, String errorMessage, Long... params) throws DAOException {
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = PoolHelper.getInstance().getDataSource().getPool().getConnection();
            statement = connection.prepareStatement(script);

            if(params == null) {
                throw new DAOException(errorMessage);
            }
            for(int i = 0; i < params.length; i++) {
                statement.setLong(i, params[i]);
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

    public static void rollback(Connection connection) {
        if (connection != null) {
            try {
                connection.rollback();
            } catch (SQLException e) {
                //log
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
            //log
        }
    }
}
