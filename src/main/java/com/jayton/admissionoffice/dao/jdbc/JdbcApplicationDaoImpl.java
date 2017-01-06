package com.jayton.admissionoffice.dao.jdbc;

import com.jayton.admissionoffice.dao.ApplicationDao;
import com.jayton.admissionoffice.dao.exception.DAOException;
import com.jayton.admissionoffice.dao.jdbc.pool.PoolHelper;
import com.jayton.admissionoffice.dao.jdbc.util.DaoHelper;
import com.jayton.admissionoffice.model.to.Application;
import com.jayton.admissionoffice.model.to.Status;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class JdbcApplicationDaoImpl implements ApplicationDao {

    private final ResourceBundle applicationQueries = ResourceBundle.getBundle("db.queries.applicationQueries");

    public JdbcApplicationDaoImpl() {
    }

    @Override
    public Application add(Application application) throws DAOException {
        PreparedStatement statement = null;
        Connection connection = null;

        try {
            connection = PoolHelper.getInstance().getDataSource().getPool().getConnection();
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
            DaoHelper.closeResources(connection, statement);
        }
    }

    @Override
    public Application get(long id) throws DAOException {
        PreparedStatement statement = null;
        Connection connection = null;

        try {
            connection = PoolHelper.getInstance().getDataSource().getPool().getConnection();
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
            DaoHelper.closeResources(connection, statement);
        }
    }

    @Override
    public void update(long id, Status status) throws DAOException {
        PreparedStatement statement = null;
        Connection connection = null;

        try {
            connection = PoolHelper.getInstance().getDataSource().getPool().getConnection();
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
            DaoHelper.closeResources(connection, statement);
        }
    }

    @Override
    public void delete(long id) throws DAOException {
        DaoHelper.delete(applicationQueries.getString("application.delete"), "Failed to delete application.", id);
    }

    @Override
    public void updateAll(List<Application> applications, Status status) throws DAOException {
        PreparedStatement statement = null;
        Connection connection = null;

        try {
            connection = PoolHelper.getInstance().getDataSource().getPool().getConnection();
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
            DaoHelper.closeResources(connection, statement);
        }
    }

    @Override
    public List<Application> getByDirection(long directionId, long offset, long count) throws DAOException {
        PreparedStatement statement = null;
        Connection connection = null;

        try {
            connection = PoolHelper.getInstance().getDataSource().getPool().getConnection();
            statement = connection.prepareStatement(applicationQueries.getString("application.get.all.by_direction"));
            statement.setLong(1, directionId);
            statement.setLong(2, count);
            statement.setLong(3, offset);

            return getByStatement(statement);

        } catch (SQLException e) {
            throw new DAOException("Failed to get applications.", e);
        } finally {
            DaoHelper.closeResources(connection, statement);
        }
    }

    @Override
    public List<Application> getByUser(long userId) throws DAOException {
        PreparedStatement statement = null;
        Connection connection = null;

        try {
            connection = PoolHelper.getInstance().getDataSource().getPool().getConnection();
            statement = connection.prepareStatement(applicationQueries.getString("application.get.all.by_user"));
            statement.setLong(1, userId);

            return getByStatement(statement);

        } catch (SQLException e) {
            throw new DAOException("Failed to get applications.", e);
        } finally {
            DaoHelper.closeResources(connection, statement);
        }
    }

    @Override
    public List<Application> getAll() throws DAOException {
        PreparedStatement statement = null;
        Connection connection = null;

        try {
            connection = PoolHelper.getInstance().getDataSource().getPool().getConnection();
            statement = connection.prepareStatement(applicationQueries.getString("application.get.all"));

            return getByStatement(statement);

        } catch (SQLException e) {
            throw new DAOException("Failed to get applications.", e);
        } finally {
            DaoHelper.closeResources(connection, statement);
        }
    }

    @Override
    public long getCount(long directionId) throws DAOException {
        return DaoHelper.getCount(applicationQueries.getString("application.count"),
                "Failed to get count of applications.", directionId);
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