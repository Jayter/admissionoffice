package com.jayton.admissionoffice.dao.jdbc;

import com.jayton.admissionoffice.dao.DirectionDao;
import com.jayton.admissionoffice.dao.exception.DAOException;
import com.jayton.admissionoffice.dao.jdbc.pool.PoolHelper;
import com.jayton.admissionoffice.model.university.Direction;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jayton on 27.11.2016.
 */
public class JdbcDirectionDaoImpl implements DirectionDao {

    public static final String SQL_GET = "SELECT * FROM directions WHERE id=?";
    public static final String SQL_GET_BY_FACULTY = "SELECT * FROM directions WHERE faculty_id=?";
    public static final String SQL_GET_ALL = "SELECT * FROM directions";
    public static final String SQL_ADD = "INSERT INTO directions (name, average_coef, count_of_students, faculty_id)" +
            " VALUES (?, ?, ?, ?)";
    public static final String SQL_UPDATE = "UPDATE directions SET name=?, average_coef=?, count_of_students=? WHERE id=?";
    public static final String SQL_DELETE = "DELETE FROM directions WHERE id=?";

    private static JdbcDirectionDaoImpl instance;

    private JdbcDirectionDaoImpl() {
    }

    public static synchronized JdbcDirectionDaoImpl getInstance() {
        if(instance == null) {
            instance = new JdbcDirectionDaoImpl();
        }

        return instance;
    }

    @Override
    public Direction get(Long id) throws DAOException {
        PreparedStatement statement = null;
        Connection connection = null;

        try {
            connection = PoolHelper.getDataSource().getPool().getConnection();
            statement = connection.prepareStatement(SQL_GET);
            statement.setLong(1, id);


            Direction direction = new Direction();
            try(ResultSet rs = statement.executeQuery()) {
                if(!rs.next()) {
                    return null;
                }

                direction.setId(rs.getLong("id"));
                direction.setName(rs.getString("name"));
                direction.setAverageCoefficient(rs.getBigDecimal("average_coef").setScale(2, BigDecimal.ROUND_HALF_UP));
                direction.setCountOfStudents(rs.getInt("count_of_students"));
                direction.setOwnerId(rs.getLong("faculty_id"));
            }

            return direction;
        } catch (SQLException e) {
            throw new DAOException("Failed to load direction.", e);
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
    public List<Direction> getByFaculty(Long facultyId) throws DAOException {
        List<Direction> faculties = new ArrayList<>();
        PreparedStatement statement = null;
        Connection connection = null;

        try {
            connection = PoolHelper.getDataSource().getPool().getConnection();
            statement = connection.prepareStatement(SQL_GET_BY_FACULTY);
            statement.setLong(1, facultyId);

            Direction direction = null;

            try(ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    direction = new Direction();

                    direction.setId(rs.getLong("id"));
                    direction.setName(rs.getString("name"));
                    direction.setAverageCoefficient(rs.getBigDecimal("average_coef").setScale(2, BigDecimal.ROUND_HALF_UP));
                    direction.setCountOfStudents(rs.getInt("count_of_students"));
                    direction.setOwnerId(rs.getLong("faculty_id"));

                    faculties.add(direction);
                }
            }

        } catch (SQLException e) {
            throw new DAOException("Failed to load directions.", e);
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
        return faculties;
    }

    @Override
    public List<Direction> getAll() throws DAOException {
        List<Direction> faculties = new ArrayList<>();
        PreparedStatement statement = null;
        Connection connection = null;

        try {
            connection = PoolHelper.getDataSource().getPool().getConnection();
            statement = connection.prepareStatement(SQL_GET_ALL);

            Direction direction = null;

            try(ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    direction = new Direction();

                    direction.setId(rs.getLong("id"));
                    direction.setName(rs.getString("name"));
                    direction.setAverageCoefficient(rs.getBigDecimal("average_coef").setScale(2, BigDecimal.ROUND_HALF_UP));
                    direction.setCountOfStudents(rs.getInt("count_of_students"));
                    direction.setOwnerId(rs.getLong("faculty_id"));

                    faculties.add(direction);
                }
            }

        } catch (SQLException e) {
            throw new DAOException("Failed to load directions.", e);
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
        return faculties;
    }

    @Override
    public void add(Direction direction) throws DAOException {
        PreparedStatement statement = null;
        Connection connection = null;

        try {
            connection = PoolHelper.getDataSource().getPool().getConnection();
            statement = connection.prepareStatement(SQL_ADD, Statement.RETURN_GENERATED_KEYS);

            statement.setString(1, direction.getName());
            statement.setBigDecimal(2, direction.getAverageCoefficient().setScale(2, BigDecimal.ROUND_HALF_UP));
            statement.setInt(3, direction.getCountOfStudents());
            statement.setLong(4, direction.getOwnerId());

            int affectedRows = statement.executeUpdate();
            if(affectedRows == 0) {
                throw new DAOException("Failed to save direction.");
            }

            try (ResultSet rs = statement.getGeneratedKeys()) {
                if (rs.next()) {
                    direction.setId(rs.getLong(1));
                } else {
                    throw new DAOException("Failed to get direction`s id.");
                }
            }

        } catch (SQLException | NullPointerException e) {
            throw new DAOException("Failed to save direction.", e);
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
    public void update(Direction direction) throws DAOException {
        PreparedStatement statement = null;
        Connection connection = null;

        try {
            connection = PoolHelper.getDataSource().getPool().getConnection();
            statement = connection.prepareStatement(SQL_UPDATE);

            statement.setString(1, direction.getName());
            statement.setBigDecimal(2, direction.getAverageCoefficient().setScale(2, BigDecimal.ROUND_HALF_UP));
            statement.setInt(3, direction.getCountOfStudents());

            statement.setLong(4, direction.getId());

            int affectedRows = statement.executeUpdate();
            if(affectedRows == 0) {
                throw new DAOException("Failed to update direction.");
            }

        } catch (SQLException | NullPointerException e) {
            throw new DAOException("Failed to update direction.", e);
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
    public void delete(Direction direction) throws DAOException {
        delete(direction.getId());
    }

    @Override
    public void delete(Long id) throws DAOException {
        PreparedStatement statement = null;
        Connection connection = null;

        try {
            connection = PoolHelper.getDataSource().getPool().getConnection();
            statement = connection.prepareStatement(SQL_DELETE);

            statement.setLong(1, id);

            int affectedRows = statement.executeUpdate();
            if(affectedRows == 0) {
                throw new DAOException("Failed to delete direction.");
            }

        } catch (SQLException | NullPointerException e) {
            throw new DAOException("Failed to delete direction.", e);
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
