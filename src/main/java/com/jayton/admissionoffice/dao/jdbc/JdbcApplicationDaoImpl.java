package com.jayton.admissionoffice.dao.jdbc;

import com.jayton.admissionoffice.dao.ApplicationDao;
import com.jayton.admissionoffice.dao.exception.DAOException;
import com.jayton.admissionoffice.dao.jdbc.util.DaoHelper;
import com.jayton.admissionoffice.model.to.Application;
import com.jayton.admissionoffice.model.to.ApplicationDTO;
import com.jayton.admissionoffice.model.to.Status;
import com.jayton.admissionoffice.util.di.Injected;

import javax.sql.DataSource;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;

public class JdbcApplicationDaoImpl implements ApplicationDao {

    private final ResourceBundle applicationQueries = ResourceBundle.getBundle("db.queries.applicationQueries");

    @Injected
    private DataSource dataSource;
    @Injected
    private DaoHelper daoHelper;

    public JdbcApplicationDaoImpl() {
    }

    @Override
    public Application add(Application application) throws DAOException {
        PreparedStatement statement = null;
        Connection connection = null;

        try {
            connection = dataSource.getConnection();
            statement = connection.prepareStatement(applicationQueries.getString("application.add"),
                    Statement.RETURN_GENERATED_KEYS);

            statement.setLong(1, application.getUserId());
            statement.setLong(2, application.getDirectionId());
            statement.setTimestamp(3, Timestamp.valueOf(application.getCreationTime()));
            statement.setBigDecimal(4, application.getMark());

            int affectedRows = statement.executeUpdate();
            if(affectedRows == 0) {
                throw new DAOException("Failed to save application.");
            }

            try (ResultSet rs = statement.getGeneratedKeys()) {
                if (rs.next()) {
                    return new Application(rs.getLong(1), application.getUserId(), application.getDirectionId(),
                            application.getCreationTime(), Status.CREATED, application.getMark());
                } else {
                    throw new DAOException("Failed to get application`s id.");
                }
            }

        } catch (SQLException e) {
            throw new DAOException("Failed to save application.", e);
        } finally {
            daoHelper.closeResources(connection, statement);
        }
    }

    @Override
    public Application get(long id) throws DAOException {
        PreparedStatement statement = null;
        Connection connection = null;

        try {
            connection = dataSource.getConnection();
            statement = connection.prepareStatement(applicationQueries.getString("application.get"));
            statement.setLong(1, id);

            try (ResultSet rs = statement.executeQuery()) {
                if (!rs.next()) {
                    return null;
                }

                long userId = rs.getLong("user_id");
                long directionId = rs.getLong("direction_id");
                LocalDateTime created = rs.getTimestamp("created_time").toLocalDateTime();
                byte status = rs.getByte("status");
                BigDecimal mark = rs.getBigDecimal("mark").setScale(2, BigDecimal.ROUND_HALF_UP);

                return new Application(id, userId, directionId, created, Status.getByOrdinal(status), mark);
            }
        } catch (SQLException e) {
            throw new DAOException("Failed to get applications.", e);
        } finally {
            daoHelper.closeResources(connection, statement);
        }
    }

    @Override
    public void update(long id, Status status) throws DAOException {
        PreparedStatement statement = null;
        Connection connection = null;

        try {
            connection = dataSource.getConnection();
            statement = connection.prepareStatement(applicationQueries.getString("application.update"));

            statement.setInt(1, status.ordinal());
            statement.setLong(2, id);

            int affectedRows = statement.executeUpdate();
            if(affectedRows == 0) {
                throw new DAOException("Failed to update application");
            }
        } catch (SQLException e) {
            throw new DAOException("Failed to get application.", e);
        } finally {
            daoHelper.closeResources(connection, statement);
        }
    }

    @Override
    public void delete(long id) throws DAOException {
        daoHelper.delete(applicationQueries.getString("application.delete"), "Failed to delete application.", id);
    }

    @Override
    public void updateAll(List<Application> applications, Status status) throws DAOException {
        PreparedStatement statement = null;
        Connection connection = null;

        try {
            connection = dataSource.getConnection();
            statement = connection.prepareStatement(applicationQueries.getString("application.update"));

            for(Application application: applications) {
                statement.setInt(1, status.ordinal());
                statement.setLong(2, application.getId());
                statement.addBatch();
            }

            for(int affectedRow: statement.executeBatch()) {
                if(affectedRow == 0) {
                    throw new DAOException("Failed to update applications.");
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Failed to update applications.", e);
        } finally {
            daoHelper.closeResources(connection, statement);
        }
    }

    @Override
    public ApplicationDTO getByDirection(long directionId, long offset, long count) throws DAOException {
        Connection connection = null;
        PreparedStatement getApplicationsSt = null;
        PreparedStatement getTotalCountSt = null;

        try {
            connection = dataSource.getConnection();
            getApplicationsSt = connection.prepareStatement(applicationQueries.getString("application.get.all.by_direction"));
            getTotalCountSt = connection.prepareStatement(applicationQueries.getString("application.count"));
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

            return new ApplicationDTO(applications, userNames, totalCount);
        } catch (SQLException e) {
            throw new DAOException("Failed to get applications.", e);
        } finally {
            daoHelper.closeResources(connection, getApplicationsSt, getTotalCountSt);
        }
    }

    @Override
    public List<Application> getByUser(long userId) throws DAOException {
        PreparedStatement statement = null;
        Connection connection = null;

        try {
            connection = dataSource.getConnection();
            statement = connection.prepareStatement(applicationQueries.getString("application.get.all.by_user"));
            statement.setLong(1, userId);

            return getByStatement(statement);

        } catch (SQLException e) {
            throw new DAOException("Failed to get applications.", e);
        } finally {
            daoHelper.closeResources(connection, statement);
        }
    }

    @Override
    public List<Application> getAll() throws DAOException {
        PreparedStatement statement = null;
        Connection connection = null;

        try {
            connection = dataSource.getConnection();
            statement = connection.prepareStatement(applicationQueries.getString("application.get.all"));

            return getByStatement(statement);

        } catch (SQLException e) {
            throw new DAOException("Failed to get applications.", e);
        } finally {
            daoHelper.closeResources(connection, statement);
        }
    }

    private List<Application> getByStatement(PreparedStatement statement) throws SQLException {
        List<Application> applications = new ArrayList<>();

        try (ResultSet rs = statement.executeQuery()) {
            while (rs.next()) {
                long id = rs.getLong("id");
                long userId = rs.getLong("user_id");
                long directionId = rs.getLong("direction_id");
                LocalDateTime created = rs.getTimestamp("created_time").toLocalDateTime();
                byte status = rs.getByte("status");
                BigDecimal mark = rs.getBigDecimal("mark").setScale(2, BigDecimal.ROUND_HALF_UP);

                applications.add(new Application(id, userId, directionId, created, Status.getByOrdinal(status), mark));
            }
            return applications;
        }
    }
}