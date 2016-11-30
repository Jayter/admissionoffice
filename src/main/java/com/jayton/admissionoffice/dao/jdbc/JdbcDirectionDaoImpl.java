package com.jayton.admissionoffice.dao.jdbc;

import com.jayton.admissionoffice.dao.DirectionDao;
import com.jayton.admissionoffice.dao.exception.DAOException;
import com.jayton.admissionoffice.dao.jdbc.pool.PoolHelper;
import com.jayton.admissionoffice.model.university.Direction;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static com.jayton.admissionoffice.dao.jdbc.util.NumericHelper.scale;

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
            connection = PoolHelper.getInstance().getDataSource().getPool().getConnection();
            statement = connection.prepareStatement(SQL_GET);
            statement.setLong(1, id);

            try(ResultSet rs = statement.executeQuery()) {
                if(!rs.next()) {
                    return null;
                }

                String name = rs.getString("name");
                BigDecimal averageCoef = scale(rs.getBigDecimal("average_coef"), 2);
                Integer countOfStudents = rs.getInt("count_of_students");
                Long facultyId = rs.getLong("faculty_id");

                return new Direction(id, name, averageCoef, countOfStudents, facultyId);
            }
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
        PreparedStatement statement = null;
        Connection connection = null;

        try {
            connection = PoolHelper.getInstance().getDataSource().getPool().getConnection();
            statement = connection.prepareStatement(SQL_GET_BY_FACULTY);
            statement.setLong(1, facultyId);

            List<Direction> directions = new ArrayList<>();

            try(ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {

                    Long id = rs.getLong("id");
                    String name = rs.getString("name");
                    BigDecimal averageCoef = scale(rs.getBigDecimal("average_coef"), 2);
                    Integer countOfStudents = rs.getInt("count_of_students");

                    directions.add(new Direction(id, name, averageCoef, countOfStudents, facultyId));
                }
            }
            return directions;

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
    }

    @Override
    public List<Direction> getAll() throws DAOException {
        PreparedStatement statement = null;
        Connection connection = null;

        try {
            connection = PoolHelper.getInstance().getDataSource().getPool().getConnection();
            statement = connection.prepareStatement(SQL_GET_ALL);

            List<Direction> directions = new ArrayList<>();

            try(ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {

                    Long id = rs.getLong("id");
                    String name = rs.getString("name");
                    BigDecimal averageCoef = scale(rs.getBigDecimal("average_coef"), 2);
                    Integer countOfStudents = rs.getInt("count_of_students");
                    Long facultyId = rs.getLong("faculty_id");

                    directions.add(new Direction(id, name, averageCoef, countOfStudents, facultyId));
                }
            }
            return directions;

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
    }

    @Override
    public Long add(Direction direction) throws DAOException {
        PreparedStatement statement = null;
        Connection connection = null;

        try {
            connection = PoolHelper.getInstance().getDataSource().getPool().getConnection();
            statement = connection.prepareStatement(SQL_ADD, Statement.RETURN_GENERATED_KEYS);

            statement.setString(1, direction.getName());
            statement.setBigDecimal(2, scale(direction.getAverageCoefficient(), 2));
            statement.setInt(3, direction.getCountOfStudents());
            statement.setLong(4, direction.getFacultyId());

            int affectedRows = statement.executeUpdate();
            if(affectedRows == 0) {
                throw new DAOException("Failed to save direction.");
            }

            try (ResultSet rs = statement.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getLong(1);
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
            connection = PoolHelper.getInstance().getDataSource().getPool().getConnection();
            statement = connection.prepareStatement(SQL_UPDATE);

            statement.setString(1, direction.getName());
            statement.setBigDecimal(2, scale(direction.getAverageCoefficient(), 2));
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
