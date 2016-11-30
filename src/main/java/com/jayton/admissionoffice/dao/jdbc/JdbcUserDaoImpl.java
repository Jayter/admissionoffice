package com.jayton.admissionoffice.dao.jdbc;

import com.jayton.admissionoffice.dao.UserDao;
import com.jayton.admissionoffice.dao.exception.DAOException;
import com.jayton.admissionoffice.dao.jdbc.pool.PoolHelper;
import com.jayton.admissionoffice.model.user.User;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.jayton.admissionoffice.dao.jdbc.util.NumericHelper.scale;

/**
 * Created by Jayton on 27.11.2016.
 */
public class JdbcUserDaoImpl implements UserDao {

    public static final String SQL_GET = "SELECT * FROM users WHERE id=?";
    public static final String SQL_GET_ALL = "SELECT * FROM users";
    public static final String SQL_ADD_CREDENTIALS = "INSERT INTO credentials(login, password) VALUES (?, ?)";
    public static final String SQL_ADD = "INSERT INTO users (name, last_name, address, email, phone_number, birth_date, average_mark)" +
            " VALUES (?, ?, ?, ?, ?, ?, ?)";
    public static final String SQL_UPDATE = "UPDATE users SET name=?, last_name=?, address=?, email=?," +
            " phone_number=?, birth_date=?, average_mark=? WHERE id=?";
    public static final String SQL_DELETE = "DELETE FROM users WHERE id=?";

    private static JdbcUserDaoImpl instance;

    private JdbcUserDaoImpl() {
    }

    public static synchronized JdbcUserDaoImpl getInstance() {
        if(instance == null) {
            instance = new JdbcUserDaoImpl();
        }
        return instance;
    }

    @Override
    public User get(Long id) throws DAOException {
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
                String secondName = rs.getString("last_name");
                String address = rs.getString("address");
                String email = rs.getString("email");
                String phoneNumber = rs.getString("phone_number");
                LocalDate birthDate = rs.getDate("birth_date").toLocalDate();
                BigDecimal averageMark = scale(rs.getBigDecimal("average_mark"), 2);

                return new User(id, name, secondName, address, email, phoneNumber, birthDate, averageMark);
            }
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

    public List<User> getAll() throws DAOException {
        List<User> users = new ArrayList<>();
        PreparedStatement statement = null;
        Connection connection = null;

        try {
            connection = PoolHelper.getInstance().getDataSource().getPool().getConnection();
            statement = connection.prepareStatement(SQL_GET_ALL);

            try(ResultSet rs = statement.executeQuery()) {
                while(rs.next()) {
                    Long id = rs.getLong("id");
                    String name = rs.getString("name");
                    String secondName = rs.getString("last_name");
                    String address = rs.getString("address");
                    String email = rs.getString("email");
                    String phoneNumber = rs.getString("phone_number");
                    LocalDate birthDate = rs.getDate("birth_date").toLocalDate();
                    BigDecimal averageMark = scale(rs.getBigDecimal("average_mark"), 2);

                    users.add(new User(id, name, secondName, address, email, phoneNumber, birthDate, averageMark));
                }
            }

        } catch (SQLException e) {
            throw new DAOException("Failed to load users.", e);
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
        return users;
    }

    @Override
    public Long add(User user) throws DAOException {
        PreparedStatement addUser = null;
        PreparedStatement addCredentials = null;
        Connection connection = null;

        try {
            connection = PoolHelper.getInstance().getDataSource().getPool().getConnection();
            addUser = connection.prepareStatement(SQL_ADD, Statement.RETURN_GENERATED_KEYS);
            addCredentials = connection.prepareStatement(SQL_ADD_CREDENTIALS);
            connection.setAutoCommit(false);

            addUser.setString(1, user.getName());
            addUser.setString(2, user.getLastName());
            addUser.setString(3, user.getAddress());
            addUser.setString(4, user.getEmail());
            addUser.setString(5, user.getPhoneNumber());
            addUser.setDate(6, Date.valueOf(user.getBirthDate()));
            addUser.setBigDecimal(7, scale(user.getAverageMark(), 2));

            addCredentials.setString(1, user.getLogin());
            addCredentials.setString(2, user.getPassword());

            int affectedUsers = addUser.executeUpdate();
            int affectedCredentials = addCredentials.executeUpdate();

            if(affectedUsers == 0 || affectedCredentials == 0) {
                throw new DAOException("Failed to save user.");
            }

            connection.commit();

            try (ResultSet rs = addUser.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getLong(1);
                } else {
                    throw new DAOException("Failed to get user`s id.");
                }
            }

        } catch (SQLException | NullPointerException e) {
            throw new DAOException("Failed to save user.", e);
        } finally {
            if(addUser != null) {
                try {
                    addUser.close();
                } catch (SQLException e) {
                    //will log it
                }
            }
            if(addCredentials != null) {
                try {
                    addCredentials.close();
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
    public void update(User user) throws DAOException {
        PreparedStatement statement = null;
        Connection connection = null;

        try {
            connection = PoolHelper.getInstance().getDataSource().getPool().getConnection();
            statement = connection.prepareStatement(SQL_UPDATE);

            statement.setString(1, user.getName());
            statement.setString(2, user.getLastName());
            statement.setString(3, user.getAddress());
            statement.setString(4, user.getEmail());
            statement.setString(5, user.getPhoneNumber());
            statement.setDate(6, Date.valueOf(user.getBirthDate()));
            statement.setBigDecimal(7, scale(user.getAverageMark(), 2));

            statement.setLong(8, user.getId());

            int affectedRows = statement.executeUpdate();
            if(affectedRows == 0) {
                throw new DAOException("Failed to update user.");
            }

        } catch (SQLException | NullPointerException e) {throw new DAOException("Failed to update user.", e);
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
