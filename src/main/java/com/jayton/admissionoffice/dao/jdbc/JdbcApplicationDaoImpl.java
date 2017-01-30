package com.jayton.admissionoffice.dao.jdbc;

import com.jayton.admissionoffice.dao.ApplicationDao;
import com.jayton.admissionoffice.dao.exception.DAOException;
import com.jayton.admissionoffice.dao.jdbc.util.DaoHelper;
import com.jayton.admissionoffice.model.to.Application;
import com.jayton.admissionoffice.model.to.ApplicationDto;
import com.jayton.admissionoffice.model.to.Status;
import com.jayton.admissionoffice.util.di.Injected;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;

public class JdbcApplicationDaoImpl implements ApplicationDao {

    private final ResourceBundle applicationQueries = ResourceBundle.getBundle("db.queries.applicationQueries");
    private final Logger logger = LoggerFactory.getLogger(JdbcApplicationDaoImpl.class);

    @Injected
    private DataSource dataSource;
    @Injected
    private DaoHelper daoHelper;

    public JdbcApplicationDaoImpl() {
    }

    @Override
    public long add(Application application) throws DAOException {
        logger.info("Adding application: %s.", application.toString());
        try(Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(applicationQueries.getString("application.add"),
                    Statement.RETURN_GENERATED_KEYS)) {

            statement.setLong(1, application.getUserId());
            statement.setLong(2, application.getDirectionId());
            statement.setTimestamp(3, Timestamp.valueOf(application.getCreationTime()));
            statement.setBigDecimal(4, application.getMark());

            logger.info("Executing query: \"%s\"", statement.toString());

            statement.executeUpdate();
            try (ResultSet rs = statement.getGeneratedKeys()) {
                rs.next();
                return rs.getLong(1);
            }
        } catch (SQLException e) {
            logger.error("Failed to save application.", e);
            throw new DAOException("Failed to save application.", e);
        }
    }

    @Override
    public Application get(long id) throws DAOException {
        logger.info("Getting application by id: %d.", id);
        Application application = null;
        try(Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(applicationQueries.getString("application.get"))) {

            statement.setLong(1, id);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    long userId = rs.getLong("user_id");
                    long directionId = rs.getLong("direction_id");
                    LocalDateTime created = rs.getTimestamp("created_time").toLocalDateTime();
                    byte status = rs.getByte("status");
                    BigDecimal mark = rs.getBigDecimal("mark");

                    application = new Application(id, userId, directionId, created, Status.getByOrdinal(status), mark);
                }
            }
        } catch (SQLException e) {
            logger.error("Failed to get applications.", e);
            throw new DAOException("Failed to get applications.", e);
        }
        return application;
    }

    @Override
    public boolean update(long id, Status status) throws DAOException {
        logger.info("Updating application: id: %d, status: %s.", id, status);
        try(Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(applicationQueries.getString("application.update"))) {

            statement.setInt(1, status.ordinal());
            statement.setLong(2, id);

            return statement.executeUpdate() != 0;
        } catch (SQLException e) {
            logger.error("Failed to get application.", e);
            throw new DAOException("Failed to get application.", e);
        }
    }

    @Override
    public boolean delete(long id) throws DAOException {
        logger.info("Deleting application by id: %d.", id);
        return daoHelper.delete(applicationQueries.getString("application.delete"), "Failed to delete application.", id);
    }

    @Override
    public boolean updateAll(List<Application> applications, Status status) throws DAOException {
        logger.info("Updating applications with status: %s.", status);
        boolean updated = true;
        try(Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(applicationQueries.getString("application.update"))) {

            for(Application application: applications) {
                statement.setInt(1, status.ordinal());
                statement.setLong(2, application.getId());
                statement.addBatch();
            }

            for(int affectedRow: statement.executeBatch()) {
                if(affectedRow == 0) {
                    return false;
                }
            }
        } catch (SQLException e) {
            logger.error("Failed to update applications.", e);
            throw new DAOException("Failed to update applications.", e);
        }
        return updated;
    }

    @Override
    public ApplicationDto getByDirection(long directionId, long offset, long count) throws DAOException {
        logger.info("Getting applications by direction id: %d, offset: %d, count: %d.", directionId, offset, count);
        try(Connection connection = dataSource.getConnection();
            PreparedStatement getApplicationsSt = connection.prepareStatement(applicationQueries.getString("application.get.all.by_direction"));
            PreparedStatement getTotalCountSt = connection.prepareStatement(applicationQueries.getString("application.count"))) {

            getApplicationsSt.setLong(1, directionId);
            getApplicationsSt.setLong(2, count);
            getApplicationsSt.setLong(3, offset);

            List<Application> applications = new ArrayList<>();
            Map<Long, String> userNames = new HashMap<>();

            try (ResultSet rs = getApplicationsSt.executeQuery()) {
                while (rs.next()) {
                    long id = rs.getLong("id");
                    long userId = rs.getLong("user_id");
                    LocalDateTime created = rs.getTimestamp("created_time").toLocalDateTime();
                    byte status = rs.getByte("status");
                    BigDecimal mark = rs.getBigDecimal("mark").setScale(2, BigDecimal.ROUND_HALF_UP);
                    String name = rs.getString("name");
                    String lastName = rs.getString("last_name");

                    applications.add(new Application(id, userId, directionId, created, Status.getByOrdinal(status), mark));
                    userNames.put(userId, lastName + " " + name);
                }
            }
            long totalCount = daoHelper.getCount(getTotalCountSt, directionId);

            return new ApplicationDto(applications, userNames, totalCount);
        } catch (SQLException e) {
            logger.error("Failed to get applications.", e);
            throw new DAOException("Failed to get applications.", e);
        }
    }

    @Override
    public List<Application> getByUser(long userId) throws DAOException {
        logger.info("Getting applications by user id: %d.", userId);
        try(Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(applicationQueries.getString("application.get.all.by_user"))) {
            statement.setLong(1, userId);

            return getApplicationsByStatement(statement);
        } catch (SQLException e) {
            logger.error("Failed to get applications.", e);
            throw new DAOException("Failed to get applications.", e);
        }
    }

    @Override
    public List<Application> getAll() throws DAOException {
        logger.info("Getting all applications.");
        try(Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(applicationQueries.getString("application.get.all"))) {

            return getApplicationsByStatement(statement);
        } catch (SQLException e) {
            logger.error("Failed to get applications.", e);
            throw new DAOException("Failed to get applications.", e);
        }
    }

    private List<Application> getApplicationsByStatement(PreparedStatement statement) throws SQLException {
        List<Application> applications = new ArrayList<>();

        try (ResultSet rs = statement.executeQuery()) {
            while (rs.next()) {
                long id = rs.getLong("id");
                long userId = rs.getLong("user_id");
                long directionId = rs.getLong("direction_id");
                LocalDateTime created = rs.getTimestamp("created_time").toLocalDateTime();
                byte status = rs.getByte("status");
                BigDecimal mark = rs.getBigDecimal("mark");

                applications.add(new Application(id, userId, directionId, created, Status.getByOrdinal(status), mark));
            }
            return applications;
        }
    }
}