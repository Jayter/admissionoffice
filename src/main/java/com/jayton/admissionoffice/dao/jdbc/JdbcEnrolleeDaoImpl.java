package com.jayton.admissionoffice.dao.jdbc;

import com.jayton.admissionoffice.dao.EnrolleDao;
import com.jayton.admissionoffice.dao.exception.DAOException;
import com.jayton.admissionoffice.dao.jdbc.pool.PoolHelper;
import com.jayton.admissionoffice.model.user.Enrollee;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jayton on 27.11.2016.
 */
public class JdbcEnrolleeDaoImpl implements EnrolleDao {

    public static final String SQL_GET = "SELECT * FROM enrollees WHERE id=?";
    public static final String SQL_GET_ALL = "SELECT * FROM enrollees";
    public static final String SQL_ADD = "INSERT INTO enrollees (name, second_name, address, email, password," +
            " phone_number, birth_date, average_mark) values (?, ?, ?, ?, ?, ?, ?, ?)";
    public static final String SQL_UPDATE = "UPDATE enrollees SET name=?, second_name=?, address=?, email=?," +
            " password=?, phone_number=?, birth_date=?, average_mark=? WHERE id=?";
    public static final String SQL_DELETE = "DELETE FROM enrollees WHERE id=?";

    private static JdbcEnrolleeDaoImpl instance;

    private JdbcEnrolleeDaoImpl() {
    }

    public static synchronized JdbcEnrolleeDaoImpl getInstance() {
        if(instance == null) {
            instance = new JdbcEnrolleeDaoImpl();
        }
        return instance;
    }

    @Override
    public Enrollee get(Long id) throws DAOException {
        PreparedStatement statement = null;
        Connection connection = null;

        try {
            connection = PoolHelper.getDataSource().getPool().getConnection();
            statement = connection.prepareStatement(SQL_GET);
            statement.setLong(1, id);

            Enrollee enrollee = new Enrollee();

            try(ResultSet rs = statement.executeQuery()) {
                if(!rs.next()) {
                    return null;
                }

                enrollee.setId(rs.getLong("id"));
                enrollee.setName(rs.getString("name"));
                enrollee.setSecondName(rs.getString("second_name"));
                enrollee.setAddress(rs.getString("address"));
                enrollee.setEmail(rs.getString("email"));
                enrollee.setPassword(rs.getString("password"));
                enrollee.setPhoneNumber(rs.getString("phone_number"));
                enrollee.setBirthDate(rs.getDate("birth_date").toLocalDate());
                enrollee.setAverageMark(rs.getBigDecimal("average_mark").setScale(2, BigDecimal.ROUND_HALF_UP));
            }

            return enrollee;
        } catch (SQLException e) {
            throw new DAOException("Failed to load enrollee.", e);
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

    public List<Enrollee> getAll() throws DAOException {
        List<Enrollee> enrollees = new ArrayList<>();
        PreparedStatement statement = null;
        Connection connection = null;

        try {
            connection = PoolHelper.getDataSource().getPool().getConnection();
            statement = connection.prepareStatement(SQL_GET_ALL);

            Enrollee enrollee;

            try(ResultSet rs = statement.executeQuery()) {
                while(rs.next()) {
                    enrollee = new Enrollee();

                    enrollee.setId(rs.getLong("id"));
                    enrollee.setName(rs.getString("name"));
                    enrollee.setSecondName(rs.getString("second_name"));
                    enrollee.setAddress(rs.getString("address"));
                    enrollee.setEmail(rs.getString("email"));
                    enrollee.setPassword(rs.getString("password"));
                    enrollee.setPhoneNumber(rs.getString("phone_number"));
                    enrollee.setBirthDate(rs.getDate("birth_date").toLocalDate());
                    enrollee.setAverageMark(rs.getBigDecimal("average_mark").setScale(2, BigDecimal.ROUND_HALF_UP));

                    enrollees.add(enrollee);
                }
            }

        } catch (SQLException e) {
            throw new DAOException("Failed to load enrollees.", e);
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
        return enrollees;
    }

    @Override
    public void add(Enrollee enrollee) throws DAOException {
        PreparedStatement statement = null;
        Connection connection = null;

        try {
            connection = PoolHelper.getDataSource().getPool().getConnection();
            statement = connection.prepareStatement(SQL_ADD, Statement.RETURN_GENERATED_KEYS);

            statement.setString(1, enrollee.getName());
            statement.setString(2, enrollee.getSecondName());
            statement.setString(3, enrollee.getAddress());
            statement.setString(4, enrollee.getEmail());
            statement.setString(5, enrollee.getPassword());
            statement.setString(6, enrollee.getPhoneNumber());
            statement.setDate(7, Date.valueOf(enrollee.getBirthDate()));
            statement.setBigDecimal(8, enrollee.getAverageMark().setScale(2, BigDecimal.ROUND_HALF_UP));

            int affectedRows = statement.executeUpdate();
            if(affectedRows == 0) {
                throw new DAOException("Failed to save enrollee.");
            }

            try (ResultSet rs = statement.getGeneratedKeys()) {
                if (rs.next()) {
                    enrollee.setId(rs.getLong(1));
                } else {
                    throw new DAOException("Failed to get enrollee`s id.");
                }
            }

        } catch (SQLException | NullPointerException e) {
            throw new DAOException("Failed to save enrollee.", e);
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
    public void update(Enrollee enrollee) throws DAOException {
        PreparedStatement statement = null;
        Connection connection = null;

        try {
            connection = PoolHelper.getDataSource().getPool().getConnection();
            statement = connection.prepareStatement(SQL_UPDATE);

            statement.setString(1, enrollee.getName());
            statement.setString(2, enrollee.getSecondName());
            statement.setString(3, enrollee.getAddress());
            statement.setString(4, enrollee.getEmail());
            statement.setString(5, enrollee.getPassword());
            statement.setString(6, enrollee.getPhoneNumber());
            statement.setDate(7, Date.valueOf(enrollee.getBirthDate()));
            statement.setBigDecimal(8, enrollee.getAverageMark().setScale(2, BigDecimal.ROUND_HALF_UP));
            statement.setLong(9, enrollee.getId());

            int affectedRows = statement.executeUpdate();
            if(affectedRows == 0) {
                throw new DAOException("Failed to update enrollee.");
            }

        } catch (SQLException | NullPointerException e) {
            throw new DAOException("Failed to update enrollee.", e);
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
    public void delete(Enrollee enrollee) throws DAOException {
        this.delete(enrollee.getId());
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
                throw new DAOException("Failed to delete enrollee.");
            }

        } catch (SQLException | NullPointerException e) {
            throw new DAOException("Failed to delete enrollee.", e);
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
