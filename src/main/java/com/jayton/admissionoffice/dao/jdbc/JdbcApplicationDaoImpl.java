package com.jayton.admissionoffice.dao.jdbc;

import com.jayton.admissionoffice.dao.ApplicationDao;
import com.jayton.admissionoffice.dao.exception.DAOException;
import com.jayton.admissionoffice.dao.jdbc.pool.PoolHelper;
import com.jayton.admissionoffice.model.to.Application;
import com.jayton.admissionoffice.model.to.Status;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import static com.jayton.admissionoffice.dao.jdbc.util.NumericHelper.scale;

/**
 * Created by Jayton on 30.11.2016.
 */
public class JdbcApplicationDaoImpl implements ApplicationDao {

    private final ResourceBundle applicationQueries = ResourceBundle.getBundle("db.queries.applicationQueries");

    JdbcApplicationDaoImpl() {
    }

    @Override
    public Long add(Application application) throws DAOException {
        PreparedStatement statement = null;
        Connection connection = null;

        try {
            connection = PoolHelper.getInstance().getDataSource().getPool().getConnection();
            statement = connection.prepareStatement(applicationQueries.getString("application.add"),
                    Statement.RETURN_GENERATED_KEYS);

            statement.setLong(1, application.getUserId());
            statement.setLong(2, application.getDirectionId());
            statement.setTimestamp(3, Timestamp.valueOf(application.getCreationTime()));
            statement.setBigDecimal(4, scale(application.getMark(), 2));

            int affectedRows = statement.executeUpdate();
            if(affectedRows == 0) {
                throw new DAOException("Failed to save application.");
            }

            try (ResultSet rs = statement.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getLong(1);
                } else {
                    throw new DAOException("Failed to get application`s id.");
                }
            }

        } catch (SQLException | NullPointerException e) {
            throw new DAOException("Failed to save application.", e);
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
    public Application get(Long id) throws DAOException {
        PreparedStatement statement = null;
        Connection connection = null;

        try {
            connection = PoolHelper.getInstance().getDataSource().getPool().getConnection();
            statement = connection.prepareStatement(applicationQueries.getString("application.get"));
            statement.setLong(1, id);

            try(ResultSet rs = statement.executeQuery()) {
                if(!rs.next()) {
                    return null;
                }

                Long userId = rs.getLong("user_id");
                Long directionId = rs.getLong("direction_id");
                LocalDateTime created = rs.getTimestamp("created_time").toLocalDateTime();
                Integer status = rs.getInt("status");
                BigDecimal mark = scale(rs.getBigDecimal("mark"), 2);

                return new Application(id, userId, directionId, created, Status.getByOrdinal(status), mark);
            }
        } catch (SQLException | NullPointerException e) {
            throw new DAOException("Failed to get applications.", e);
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
    public void update(Long id, Status status) throws DAOException {
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
        } catch (SQLException | NullPointerException e) {
            throw new DAOException("Failed to get application.", e);
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
    public boolean delete(Long id) throws DAOException {
        PreparedStatement statement = null;
        Connection connection = null;

        try {
            connection = PoolHelper.getInstance().getDataSource().getPool().getConnection();
            statement = connection.prepareStatement(applicationQueries.getString("application.delete"));
            statement.setLong(1, id);

            int affectedRows = statement.executeUpdate();

            return affectedRows != 0;
        } catch (SQLException | NullPointerException e) {
            throw new DAOException("Failed to delete application.", e);
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
    public List<Application> getByUser(Long userId) throws DAOException {
        PreparedStatement statement = null;
        Connection connection = null;

        try {
            connection = PoolHelper.getInstance().getDataSource().getPool().getConnection();
            statement = connection.prepareStatement(applicationQueries.getString("application.get.all.by_user"));
            statement.setLong(1, userId);

            List<Application> applications = new ArrayList<>();

            try(ResultSet rs = statement.executeQuery()) {
                while(rs.next()) {
                    Long id = rs.getLong("id");
                    Long directionId = rs.getLong("direction_id");
                    LocalDateTime created = rs.getTimestamp("created_time").toLocalDateTime();
                    Integer status = rs.getInt("status");
                    BigDecimal mark = scale(rs.getBigDecimal("mark"), 2);

                    applications.add(new Application(id, userId, directionId, created, Status.getByOrdinal(status), mark));
                }
            }
            return applications;

        } catch (SQLException | NullPointerException e) {
            throw new DAOException("Failed to get applications.", e);
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
    public List<Application> getByDirection(Long directionId) throws DAOException {
        PreparedStatement statement = null;
        Connection connection = null;

        try {
            connection = PoolHelper.getInstance().getDataSource().getPool().getConnection();
            statement = connection.prepareStatement(applicationQueries.getString("application.get.all.by_direction"));
            statement.setLong(1, directionId);

            List<Application> applications = new ArrayList<>();

            try(ResultSet rs = statement.executeQuery()) {
                while(rs.next()) {
                    Long id = rs.getLong("id");
                    Long userId = rs.getLong("user_id");
                    LocalDateTime created = rs.getTimestamp("created_time").toLocalDateTime();
                    Integer status = rs.getInt("status");
                    BigDecimal mark = scale(rs.getBigDecimal("mark"), 2);

                    applications.add(new Application(id, userId, directionId, created, Status.getByOrdinal(status), mark));
                }
            }
            return applications;

        } catch (SQLException | NullPointerException e) {
            throw new DAOException("Failed to get applications.", e);
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
    public List<Application> getAll() throws DAOException {
        PreparedStatement statement = null;
        Connection connection = null;

        try {
            connection = PoolHelper.getInstance().getDataSource().getPool().getConnection();
            statement = connection.prepareStatement(applicationQueries.getString("application.get.all"));

            List<Application> applications = new ArrayList<>();

            try(ResultSet rs = statement.executeQuery()) {
                while(rs.next()) {
                    Long id = rs.getLong("id");
                    Long userId = rs.getLong("user_id");
                    Long directionId = rs.getLong("direction_id");
                    LocalDateTime created = rs.getTimestamp("created_time").toLocalDateTime();
                    Integer status = rs.getInt("status");
                    BigDecimal mark = scale(rs.getBigDecimal("mark"), 2);

                    applications.add(new Application(id, userId, directionId, created, Status.getByOrdinal(status), mark));
                }
            }
            return applications;

        } catch (SQLException | NullPointerException e) {
            throw new DAOException("Failed to get applications.", e);
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
