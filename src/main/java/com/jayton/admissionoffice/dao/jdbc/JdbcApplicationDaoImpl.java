package com.jayton.admissionoffice.dao.jdbc;

import com.jayton.admissionoffice.dao.ApplicationDao;
import com.jayton.admissionoffice.dao.exception.DAOException;
import com.jayton.admissionoffice.dao.jdbc.pool.PoolHelper;
import com.jayton.admissionoffice.model.to.Application;
import com.jayton.admissionoffice.model.to.Status;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jayton on 30.11.2016.
 */
public class JdbcApplicationDaoImpl implements ApplicationDao {

    public static final String GET_BY_USER = "SELECT * FROM applications WHERE user_id=? ORDER BY created_time ASC";
    public static final String SQL_GET = "SELECT * FROM applications WHERE id=?";
    public static final String SQL_ADD = "INSERT INTO applications (user_id, direction_id)" +
            " VALUES (?, ?)";
    public static final String SQL_UPDATE = "UPDATE applications SET status=? WHERE id=?";
    public static final String SQL_DELETE = "DELETE FROM applications WHERE id=?";

    private static JdbcApplicationDaoImpl instance;

    private JdbcApplicationDaoImpl() {
    }

    public static JdbcApplicationDaoImpl getInstance() {
        if(instance == null) {
            instance = new JdbcApplicationDaoImpl();
        }
        return instance;
    }

    @Override
    public Long add(Application application) throws DAOException {
        PreparedStatement statement = null;
        Connection connection = null;

        try {
            connection = PoolHelper.getInstance().getDataSource().getPool().getConnection();
            statement = connection.prepareStatement(SQL_ADD, Statement.RETURN_GENERATED_KEYS);

            statement.setLong(1, application.getUserId());
            statement.setLong(2, application.getDirectionId());

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
    public void update(Long id, Status status) throws DAOException {
        PreparedStatement statement = null;
        Connection connection = null;

        try {
            connection = PoolHelper.getInstance().getDataSource().getPool().getConnection();
            statement = connection.prepareStatement(SQL_UPDATE);

            statement.setString(1, status.getValue());
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
            statement = connection.prepareStatement(SQL_DELETE);
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
    public Application get(Long id) throws DAOException {
        PreparedStatement statement = null;
        Connection connection = null;

        try {
            connection = PoolHelper.getInstance().getDataSource().getPool().getConnection();
            statement = connection.prepareStatement(SQL_GET);
            statement.setLong(1, id);

            try(ResultSet rs = statement.executeQuery()) {
                if(!rs.next()) {
                    return null;
                }

                Long userId = rs.getLong("user_id");
                Long directionId = rs.getLong("direction_id");
                LocalDateTime created = rs.getTimestamp("created_time").toLocalDateTime();
                String status = rs.getString("status");

                return new Application(id, userId, directionId, created, Status.getByValue(status));
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
    public List<Application> getByUser(Long userId) throws DAOException {
        PreparedStatement statement = null;
        Connection connection = null;

        try {
            connection = PoolHelper.getInstance().getDataSource().getPool().getConnection();
            statement = connection.prepareStatement(GET_BY_USER);
            statement.setLong(1, userId);

            List<Application> applications = new ArrayList<>();

            try(ResultSet rs = statement.executeQuery()) {
                while(rs.next()) {
                    Long id = rs.getLong("id");
                    Long directionId = rs.getLong("direction_id");
                    LocalDateTime created = rs.getTimestamp("created_time").toLocalDateTime();
                    String status = rs.getString("status");

                    applications.add(new Application(id, userId, directionId, created, Status.getByValue(status)));
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
