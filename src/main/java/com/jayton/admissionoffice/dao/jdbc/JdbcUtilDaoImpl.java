package com.jayton.admissionoffice.dao.jdbc;

import com.jayton.admissionoffice.dao.UtilDao;
import com.jayton.admissionoffice.dao.exception.DAOException;
import com.jayton.admissionoffice.dao.jdbc.util.DaoHelper;
import com.jayton.admissionoffice.model.Subject;
import com.jayton.admissionoffice.model.to.SessionTerms;
import com.jayton.admissionoffice.util.di.Injected;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class JdbcUtilDaoImpl implements UtilDao {

    private final ResourceBundle utilQueries = ResourceBundle.getBundle("db.queries.utilQueries");

    @Injected
    private DataSource dataSource;
    @Injected
    private DaoHelper daoHelper;

    public JdbcUtilDaoImpl() {
    }

    @Override
    public SessionTerms getSessionTerms(short currentYear) throws DAOException {
        SessionTerms terms = null;
        try(Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(utilQueries.getString("sessionDate.get"))) {

            statement.setShort(1, currentYear);

            try(ResultSet rs = statement.executeQuery()) {
                if(rs.next()) {
                    LocalDateTime start = rs.getTimestamp("session_start").toLocalDateTime();
                    LocalDateTime end = rs.getTimestamp("session_end").toLocalDateTime();

                    terms = new SessionTerms(currentYear, start, end);
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Failed to load session terms.", e);
        }
        return terms;
    }

    @Override
    public List<Subject> getAllSubjects() throws DAOException {
        List<Subject> subjects = new ArrayList<>();

        try(Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(utilQueries.getString("subject.get.all"))) {

            try(ResultSet rs = statement.executeQuery()) {
                while(rs.next()) {
                    long id = rs.getLong("id");
                    String name = rs.getString("name");

                    subjects.add(new Subject(id, name));
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Failed to load subjects.", e);
        }
        return subjects;
    }

    @Override
    public boolean updateSessionTerms(SessionTerms terms) throws DAOException {
        try(Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(utilQueries.getString("sessionDate.update"))) {

            statement.setTimestamp(1, Timestamp.valueOf(terms.getSessionStart()));
            statement.setTimestamp(2, Timestamp.valueOf(terms.getSessionEnd()));
            statement.setShort(3, terms.getYear());

            int affectedRows = statement.executeUpdate();
            return affectedRows != 0;
        } catch (SQLException e) {
            throw new DAOException("Failed to update session terms.", e);
        }
    }

    @Override
    public boolean createSessionTerms(SessionTerms terms) throws DAOException {
        try(Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(utilQueries.getString("sessionDate.create"))) {

            statement.setShort(1, terms.getYear());
            statement.setTimestamp(2, Timestamp.valueOf(terms.getSessionStart()));
            statement.setTimestamp(3, Timestamp.valueOf(terms.getSessionEnd()));

            int affectedRows = statement.executeUpdate();
            return affectedRows != 0;
        } catch (SQLException e) {
            throw new DAOException("Failed to create session terms.", e);
        }
    }
}