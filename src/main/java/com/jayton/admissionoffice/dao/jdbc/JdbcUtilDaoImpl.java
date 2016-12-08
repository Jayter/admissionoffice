package com.jayton.admissionoffice.dao.jdbc;

import com.jayton.admissionoffice.dao.UtilDao;
import com.jayton.admissionoffice.dao.exception.DAOException;
import com.jayton.admissionoffice.dao.jdbc.pool.PoolHelper;
import com.jayton.admissionoffice.model.NamedEntity;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by Jayton on 06.12.2016.
 */
public class JdbcUtilDaoImpl implements UtilDao {

    private final ResourceBundle utilQueries = ResourceBundle.getBundle("db.queries.utilQueries");

    @Override
    public List<LocalDateTime> getSessionDate(Integer currentYear) throws DAOException {
        PreparedStatement statement = null;
        Connection connection = null;

        try {
            connection = PoolHelper.getInstance().getDataSource().getPool().getConnection();
            statement = connection.prepareStatement(utilQueries.getString("sessionDate.get"));

            statement.setInt(1, currentYear);

            try(ResultSet rs = statement.executeQuery()) {
                if(rs.next()) {
                    LocalDateTime start = rs.getTimestamp("session_start").toLocalDateTime();
                    LocalDateTime end = rs.getTimestamp("session_end").toLocalDateTime();

                    return new ArrayList<>(Arrays.asList(start, end));
                } else {
                    throw new DAOException("Failed to load session dates.");
                }
            }

        } catch (SQLException | NullPointerException e) {
            throw new DAOException("Failed to load session dates.", e);
        } finally {
            if(statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    //will log it
                }
            }
            if(connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    //will log it
                }
            }
        }

    }

    @Override
    public List<NamedEntity> getAllSubjects() throws DAOException {
        PreparedStatement statement = null;
        Connection connection = null;

        try {
            connection = PoolHelper.getInstance().getDataSource().getPool().getConnection();
            statement = connection.prepareStatement(utilQueries.getString("subject.get.all"));

            List<NamedEntity> subjects = new ArrayList<>();

            try(ResultSet rs = statement.executeQuery()) {
                while(rs.next()) {
                    Long id = rs.getLong("id");
                    String name = rs.getString("name");

                    subjects.add(new NamedEntity(id, name));
                }
            }
            return subjects;

        } catch (SQLException e) {
            throw new DAOException("Failed to load subjects.", e);
        } finally {
            if(statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    //will log it
                }
            }
            if(connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    //will log it
                }
            }
        }
    }
}
