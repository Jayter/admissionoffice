package com.jayton.admissionoffice.dao.jdbc;

import com.jayton.admissionoffice.dao.UserDao;
import com.jayton.admissionoffice.dao.exception.DAOException;
import com.jayton.admissionoffice.dao.jdbc.pool.PoolHelper;
import com.jayton.admissionoffice.model.to.AuthorizationResult;
import com.jayton.admissionoffice.model.user.User;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.jayton.admissionoffice.dao.jdbc.util.NumericHelper.scale;

/**
 * Created by Jayton on 27.11.2016.
 */
public class JdbcUserDaoImpl implements UserDao {

    public static final String SQL_GET = "SELECT * FROM users WHERE id=?";
    public static final String SQL_GET_ALL = "SELECT * FROM users";
    public static final String SQL_ADD = "INSERT INTO users (name, last_name, address, email, phone_number, birth_date, average_mark)" +
            " VALUES (?, ?, ?, ?, ?, ?, ?)";
    public static final String SQL_UPDATE = "UPDATE users SET name=?, last_name=?, address=?," +
            " phone_number=?, birth_date=?, average_mark=? WHERE id=?";
    public static final String SQL_DELETE = "DELETE FROM users WHERE id=?";
    public static final String SQL_GET_COUNT = "SELECT COUNT(*) FROM users WHERE email=?";

    public static final String SQL_GET_RESULTS = "SELECT subject_id, mark FROM exam_results WHERE user_id=?";
    public static final String SQL_ADD_RESULT = "INSERT INTO exam_results (user_id, subject_id, mark) values (?, ?, ?)";
    public static final String SQL_UPDATE_RESULT = "UPDATE exam_results SET mark=? WHERE user_id=? AND subject_id=?";
    public static final String SQL_DELETE_RESULT = "DELETE FROM exam_results WHERE user_id=? AND subject_id=?";

    public static final String SQL_ADD_CREDENTIALS = "INSERT INTO credentials(login, password) VALUES (?, ?)";
    public static final String SQL_AUTHORIZE = "SELECT is_admin FROM credentials WHERE login=? and password=?";

    JdbcUserDaoImpl() {
    }

    @Override
    public Long add(User user) throws DAOException {
        PreparedStatement addUserSt = null;
        PreparedStatement addCredentialsSt = null;
        PreparedStatement addResultsSt = null;
        Connection connection = null;

        try {
            connection = PoolHelper.getInstance().getDataSource().getPool().getConnection();
            addUserSt = connection.prepareStatement(SQL_ADD, Statement.RETURN_GENERATED_KEYS);
            addCredentialsSt = connection.prepareStatement(SQL_ADD_CREDENTIALS);
            addResultsSt = connection.prepareStatement(SQL_ADD_RESULT);
            connection.setAutoCommit(false);

            addUserSt.setString(1, user.getName());
            addUserSt.setString(2, user.getLastName());
            addUserSt.setString(3, user.getAddress());
            addUserSt.setString(4, user.getEmail());
            addUserSt.setString(5, user.getPhoneNumber());
            addUserSt.setDate(6, Date.valueOf(user.getBirthDate()));
            addUserSt.setBigDecimal(7, scale(user.getAverageMark(), 2));

            addCredentialsSt.setString(1, user.getEmail());
            addCredentialsSt.setString(2, user.getPassword());

            int affectedUsers = addUserSt.executeUpdate();
            int affectedCredentials = addCredentialsSt.executeUpdate();

            if(affectedUsers == 0 || affectedCredentials == 0) {
                throw new DAOException("Failed to save user.");
            }

            Long id;
            try (ResultSet rs = addUserSt.getGeneratedKeys()) {
                if (rs.next()) {
                    id =  rs.getLong(1);
                } else {
                    throw new DAOException("Failed to get user`s id.");
                }
            }

            for(Map.Entry<Long, BigDecimal> results: user.getResults().entrySet()) {
                addResultsSt.setLong(1, id);
                addResultsSt.setLong(2, results.getKey());
                addResultsSt.setBigDecimal(3, scale(results.getValue(), 2));
                addResultsSt.addBatch();
            }
            int[] affectedRows = addResultsSt.executeBatch();

            for(int row: affectedRows) {
                if(row == 0) {
                    throw new DAOException("Failed to save user`s subject.");
                }
            }
            connection.commit();
            return id;

        } catch (SQLException | NullPointerException e) {
            throw new DAOException("Failed to save user.", e);
        } finally {
            if(addUserSt != null) {
                try {
                    addUserSt.close();
                } catch (SQLException e) {
                    //will log it
                }
            }
            if(addCredentialsSt != null) {
                try {
                    addCredentialsSt.close();
                } catch (SQLException e) {
                    //will log it
                }
            }
            if(addResultsSt != null) {
                try {
                    addResultsSt.close();
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
    public User get(Long id) throws DAOException {
        PreparedStatement getUserSt = null;
        PreparedStatement getResultsSt = null;
        Connection connection = null;

        try {
            connection = PoolHelper.getInstance().getDataSource().getPool().getConnection();
            getUserSt = connection.prepareStatement(SQL_GET);
            getResultsSt = connection.prepareStatement(SQL_GET_RESULTS);
            getUserSt.setLong(1, id);

            try(ResultSet userRs = getUserSt.executeQuery()) {
                if(!userRs.next()) {
                    return null;
                }

                String name = userRs.getString("name");
                String secondName = userRs.getString("last_name");
                String address = userRs.getString("address");
                String email = userRs.getString("email");
                String phoneNumber = userRs.getString("phone_number");
                LocalDate birthDate = userRs.getDate("birth_date").toLocalDate();
                BigDecimal averageMark = scale(userRs.getBigDecimal("average_mark"), 2);

                getResultsSt.setLong(1, id);

                Map<Long, BigDecimal> results = new HashMap<>();
                try(ResultSet resultRs = getResultsSt.executeQuery()) {
                    while (resultRs.next()) {
                        results.put(resultRs.getLong("subject_id"), scale(resultRs.getBigDecimal("mark"), 2));
                    }
                }
                return new User(id, name, secondName, address, email, phoneNumber, birthDate, averageMark, results);
            }
        } catch (SQLException e) {
            throw new DAOException("Failed to load enrollee.", e);
        } finally {
            if(getUserSt != null) {
                try {
                    getUserSt.close();
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
    public List<User> getAll() throws DAOException {
        PreparedStatement getUsersSt = null;
        PreparedStatement getResultsSt = null;
        Connection connection = null;

        try {
            connection = PoolHelper.getInstance().getDataSource().getPool().getConnection();
            getUsersSt = connection.prepareStatement(SQL_GET_ALL);
            getResultsSt = connection.prepareStatement(SQL_GET_RESULTS);
            connection.setAutoCommit(false);

            List<User> users = new ArrayList<>();

            try(ResultSet rs = getUsersSt.executeQuery()) {
                while(rs.next()) {
                    Long id = rs.getLong("id");
                    String name = rs.getString("name");
                    String secondName = rs.getString("last_name");
                    String address = rs.getString("address");
                    String email = rs.getString("email");
                    String phoneNumber = rs.getString("phone_number");
                    LocalDate birthDate = rs.getDate("birth_date").toLocalDate();
                    BigDecimal averageMark = scale(rs.getBigDecimal("average_mark"), 2);

                    getResultsSt.setLong(1, id);

                    Map<Long, BigDecimal> results = new HashMap<>();
                    try(ResultSet resultRs = getResultsSt.executeQuery()) {
                        while (resultRs.next()) {
                            results.put(resultRs.getLong("subject_id"), scale(resultRs.getBigDecimal("mark"), 2));
                        }
                    }

                    users.add(new User(id, name, secondName, address, email, phoneNumber, birthDate, averageMark, results));
                }
            }
            connection.commit();
            return users;

        } catch (SQLException e) {
            throw new DAOException("Failed to load users.", e);
        } finally {
            if(getUsersSt != null) {
                try {
                    getUsersSt.close();
                } catch (SQLException e) {
                    //will log it
                }
            }
            if(getResultsSt != null) {
                try {
                    getResultsSt.close();
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
        PreparedStatement updateUserSt = null;
        PreparedStatement updateResultSt = null;
        Connection connection = null;

        try {
            connection = PoolHelper.getInstance().getDataSource().getPool().getConnection();
            updateUserSt = connection.prepareStatement(SQL_UPDATE);
            updateResultSt = connection.prepareStatement(SQL_UPDATE_RESULT);
            connection.setAutoCommit(false);

            updateUserSt.setString(1, user.getName());
            updateUserSt.setString(2, user.getLastName());
            updateUserSt.setString(3, user.getAddress());
            updateUserSt.setString(4, user.getPhoneNumber());
            updateUserSt.setDate(5, Date.valueOf(user.getBirthDate()));
            updateUserSt.setBigDecimal(6, scale(user.getAverageMark(), 2));
            updateUserSt.setLong(7, user.getId());

            int affectedRow = updateUserSt.executeUpdate();
            if(affectedRow == 0) {
                throw new DAOException("Failed to update user.");
            }

            for(Map.Entry<Long, BigDecimal> results: user.getResults().entrySet()) {
                updateResultSt.setBigDecimal(1, scale(results.getValue(), 2));
                updateResultSt.setLong(2, user.getId());
                updateResultSt.setLong(3, results.getKey());
                updateResultSt.addBatch();
            }
            updateResultSt.executeBatch();

            connection.commit();

        } catch (SQLException | NullPointerException e) {throw new DAOException("Failed to update user.", e);
        } finally {
            if(updateUserSt != null) {
                try {
                    updateUserSt.close();
                } catch (SQLException e) {
                    //will log it
                }
            }
            if(updateResultSt != null) {
                try {
                    updateResultSt.close();
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

    @Override
    public void addResult(Long userId, Long subjectId, BigDecimal mark) throws DAOException {
        PreparedStatement statement = null;
        Connection connection = null;

        try {
            connection = PoolHelper.getInstance().getDataSource().getPool().getConnection();
            statement = connection.prepareStatement(SQL_ADD_RESULT);

            statement.setLong(1, userId);
            statement.setLong(2, subjectId);
            statement.setBigDecimal(3, scale(mark, 2));

            int affectedRows = statement.executeUpdate();
            if(affectedRows == 0) {
                throw new DAOException("Failed to save result.");
            }

        } catch (SQLException | NullPointerException e) {
            throw new DAOException("Failed to save result.", e);
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
    public AuthorizationResult authorize(String login, String password) throws DAOException {
        PreparedStatement statement = null;
        Connection connection = null;

        try {
            connection = PoolHelper.getInstance().getDataSource().getPool().getConnection();
            statement = connection.prepareStatement(SQL_AUTHORIZE);

            statement.setString(1, login);
            statement.setString(2, password);

            try(ResultSet rs = statement.executeQuery()) {
                if(rs.next()) {
                    if(rs.getBoolean("is_admin")) {
                        return AuthorizationResult.ADMIN;
                    }
                    return AuthorizationResult.USER;
                }
                else {
                    return AuthorizationResult.NULL;
                }
            }

        } catch (SQLException | NullPointerException e) {
            throw new DAOException("Failed to authorize.", e);
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
    public Map<Long, BigDecimal> getByUser(Long userId) throws DAOException {
        PreparedStatement statement = null;
        Connection connection = null;

        try {
            connection = PoolHelper.getInstance().getDataSource().getPool().getConnection();
            statement = connection.prepareStatement(SQL_GET_RESULTS);
            statement.setLong(1, userId);

            Map<Long, BigDecimal> results = new HashMap<>();

            try(ResultSet rs = statement.executeQuery()) {
                while(rs.next()) {
                    Long subjectId = rs.getLong("subject_id");
                    BigDecimal mark = scale(rs.getBigDecimal("mark"), 2);

                    results.put(subjectId, mark);
                }
            }
            return results;

        } catch (SQLException | NullPointerException e) {
            throw new DAOException("Failed to get results.", e);
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
    public boolean deleteResult(Long userId, Long subjectId) throws DAOException {
        PreparedStatement statement = null;
        Connection connection = null;

        try {
            connection = PoolHelper.getInstance().getDataSource().getPool().getConnection();
            statement = connection.prepareStatement(SQL_DELETE_RESULT);

            statement.setLong(1, userId);
            statement.setLong(2, subjectId);

            int affectedRows = statement.executeUpdate();
            return affectedRows != 0;

        } catch (SQLException | NullPointerException e) {
            throw new DAOException("Failed to delete result.", e);
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
    public int checkEmail(String email) throws DAOException {
        PreparedStatement statement = null;
        Connection connection = null;

        try {
            connection = PoolHelper.getInstance().getDataSource().getPool().getConnection();
            statement = connection.prepareStatement(SQL_GET_COUNT);

            statement.setString(1, email);

            try(ResultSet rs = statement.executeQuery()) {
                if(rs.next()) {
                    return rs.getInt(1);
                }
                else {
                    throw new DAOException("Failed to check email.");
                }
            }

        } catch (SQLException | NullPointerException e) {
            throw new DAOException("Failed to check email.", e);
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
