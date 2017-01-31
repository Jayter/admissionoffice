package com.jayton.admissionoffice.dao.jdbc;

import com.jayton.admissionoffice.dao.UtilDao;
import com.jayton.admissionoffice.dao.exception.DAOException;
import com.jayton.admissionoffice.dao.jdbc.util.DaoHelper;
import com.jayton.admissionoffice.model.Subject;
import com.jayton.admissionoffice.model.to.SessionTerms;
import com.jayton.admissionoffice.util.di.Injected;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;

public class JdbcUtilDaoImpl implements UtilDao {

    private final ResourceBundle utilQueries = ResourceBundle.getBundle("db.queries.utilQueries");
    private final Logger logger = LoggerFactory.getLogger(JdbcUtilDaoImpl.class);

    private Map<Short, SessionTerms> termsCache = new HashMap<>();
    private List<Subject> subjectsCache = new ArrayList<>();

    @Injected
    private DataSource dataSource;
    @Injected
    private DaoHelper daoHelper;

    public JdbcUtilDaoImpl() {
    }

    @Override
    public SessionTerms getSessionTerms(short currentYear) throws DAOException {
        logger.info("Getting session terms by year {}.", currentYear);
        SessionTerms terms = null;
        terms = termsCache.get(currentYear);
        if(terms == null) {
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
                logger.error("Failed to load session terms.", e);
                throw new DAOException("Failed to load session terms.", e);
            }
            termsCache.put(currentYear, terms);
        }
        return terms;
    }

    @Override
    public List<Subject> getAllSubjects() throws DAOException {
        logger.info("Getting all subjects.");
        if(subjectsCache.isEmpty()) {
            try (Connection connection = dataSource.getConnection();
                 PreparedStatement statement = connection.prepareStatement(utilQueries.getString("subject.get.all"))) {

                try (ResultSet rs = statement.executeQuery()) {
                    while (rs.next()) {
                        long id = rs.getLong("id");
                        String name = rs.getString("name");

                        subjectsCache.add(new Subject(id, name));
                    }
                }
            } catch (SQLException e) {
                logger.error("Failed to load subjects.", e);
                throw new DAOException("Failed to load subjects.", e);
            }
        }
        return Collections.unmodifiableList(subjectsCache);
    }

    @Override
    public boolean updateSessionTerms(SessionTerms terms) throws DAOException {
        logger.info("Updating session terms: {}.", terms.toString());
        try(Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(utilQueries.getString("sessionDate.update"))) {

            statement.setTimestamp(1, Timestamp.valueOf(terms.getSessionStart()));
            statement.setTimestamp(2, Timestamp.valueOf(terms.getSessionEnd()));
            statement.setShort(3, terms.getYear());

            int affectedRows = statement.executeUpdate();
            boolean updated = affectedRows != 0;

            if(updated) {
                termsCache.remove(terms.getYear());
            }

            return updated;
        } catch (SQLException e) {
            logger.error("Failed to update session terms.", e);
            throw new DAOException("Failed to update session terms.", e);
        }
    }

    @Override
    public boolean createSessionTerms(SessionTerms terms) throws DAOException {
        logger.info("Adding session terms: {}.", terms.toString());
        try(Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(utilQueries.getString("sessionDate.create"))) {

            statement.setShort(1, terms.getYear());
            statement.setTimestamp(2, Timestamp.valueOf(terms.getSessionStart()));
            statement.setTimestamp(3, Timestamp.valueOf(terms.getSessionEnd()));

            int affectedRows = statement.executeUpdate();
            boolean created = affectedRows != 0;

            if (created) {
                termsCache.put(terms.getYear(), terms);
            }

            return created;
        } catch (SQLException e) {
            logger.error("Failed to create session terms.", e);
            throw new DAOException("Failed to create session terms.", e);
        }
    }
}