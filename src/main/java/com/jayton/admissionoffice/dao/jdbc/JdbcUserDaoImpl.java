package com.jayton.admissionoffice.dao.jdbc;

import com.jayton.admissionoffice.dao.UserDao;
import com.jayton.admissionoffice.dao.exception.DAOException;
import com.jayton.admissionoffice.dao.jdbc.pool.PoolHelper;
import com.jayton.admissionoffice.dao.jdbc.util.DaoHelper;
import com.jayton.admissionoffice.model.to.AuthorizationResult;
import com.jayton.admissionoffice.model.user.User;

import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

public class JdbcUserDaoImpl implements UserDao {

    private final ResourceBundle userQueries = ResourceBundle.getBundle("db.queries.userQueries");

    JdbcUserDaoImpl() {
    }

    @Override
    public User add(User user) throws DAOException {
        PreparedStatement addUserSt = null;
        PreparedStatement addCredentialsSt = null;
        Connection connection = null;

        try {
            connection = PoolHelper.getInstance().getDataSource().getPool().getConnection();
            addUserSt = connection.prepareStatement(userQueries.getString("user.add"), Statement.RETURN_GENERATED_KEYS);
            addCredentialsSt = connection.prepareStatement(userQueries.getString("credentials.add"));
            connection.setAutoCommit(false);

            addUserSt.setString(1, user.getName());
            addUserSt.setString(2, user.getLastName());
            addUserSt.setString(3, user.getAddress());
            addUserSt.setString(4, user.getEmail());
            addUserSt.setString(5, user.getPhoneNumber());
            addUserSt.setDate(6, Date.valueOf(user.getBirthDate()));
            addUserSt.setByte(7, user.getAverageMark());

            addCredentialsSt.setString(1, user.getEmail());
            addCredentialsSt.setString(2, user.getPassword());

            int affectedUsers = addUserSt.executeUpdate();
            int affectedCredentials = addCredentialsSt.executeUpdate();

            if(affectedUsers == 0 || affectedCredentials == 0) {
                throw new DAOException("Failed to save user.");
            }
            connection.commit();

            try (ResultSet rs = addUserSt.getGeneratedKeys()) {
                if (rs.next()) {
                    Long id = rs.getLong(1);
                    return new User(id, user.getName(), user.getLastName(), user.getAddress(), user.getEmail(),
                            user.getPhoneNumber(), user.getBirthDate(), user.getAverageMark(), Collections.emptyMap());
                } else {
                    throw new DAOException("Failed to get user`s id.");
                }
            }
        } catch (SQLException e) {
            DaoHelper.rollback(connection);
            throw new DAOException("Failed to save user.", e);
        } finally {
            DaoHelper.closeResources(connection, addUserSt, addCredentialsSt);
        }
    }

    @Override
    public User get(Long id) throws DAOException {
        PreparedStatement statement = null;
        Connection connection = null;

        try {
            connection = PoolHelper.getInstance().getDataSource().getPool().getConnection();
            statement = connection.prepareStatement(userQueries.getString("user.get"));
            statement.setLong(1, id);

            return getSingleByStatement(statement);

        } catch (SQLException e) {
            throw new DAOException("Failed to load user.", e);
        } finally {
            DaoHelper.closeResources(connection, statement);
        }
    }

    @Override
    public User update(User user) throws DAOException {
        PreparedStatement updateUserSt = null;
        PreparedStatement getResultsSt = null;
        Connection connection = null;

        try {
            connection = PoolHelper.getInstance().getDataSource().getPool().getConnection();
            updateUserSt = connection.prepareStatement(userQueries.getString("user.update"));
            getResultsSt = connection.prepareStatement(userQueries.getString("result.get.all"));
            connection.setAutoCommit(false);

            updateUserSt.setString(1, user.getName());
            updateUserSt.setString(2, user.getLastName());
            updateUserSt.setString(3, user.getAddress());
            updateUserSt.setString(4, user.getPhoneNumber());
            updateUserSt.setDate(5, Date.valueOf(user.getBirthDate()));
            updateUserSt.setByte(6, user.getAverageMark());
            updateUserSt.setLong(7, user.getId());

            int affectedRow = updateUserSt.executeUpdate();
            if(affectedRow == 0) {
                throw new DAOException("Failed to update user.");
            }

            Map<Long, Short> examResults = new HashMap<>();

            getResultsSt.setLong(1, user.getId());

            try(ResultSet rs = getResultsSt.executeQuery()) {
                while (rs.next()) {
                    examResults.put(rs.getLong("subject_id"), rs.getShort("mark"));
                }
            }
            connection.commit();

            return new User(user.getId(), user.getName(), user.getLastName(), user.getEmail(), user.getAddress(),
                    user.getPhoneNumber(), user.getBirthDate(), user.getAverageMark(), examResults);

        } catch (SQLException e) {
            DaoHelper.rollback(connection);
            throw new DAOException("Failed to update user.", e);
        } finally {
            DaoHelper.closeResources(connection, updateUserSt, getResultsSt);
        }
    }

    @Override
    public void delete(Long id) throws DAOException {
        DaoHelper.delete(userQueries.getString("user.delete"), "Failed to delete user.", id);
    }

    @Override
    public List<User> getAll() throws DAOException {
        PreparedStatement statement = null;
        Connection connection = null;

        try {
            connection = PoolHelper.getInstance().getDataSource().getPool().getConnection();
            statement = connection.prepareStatement(userQueries.getString("user.get.all"));

            return getAllByStatement(statement);

        } catch (SQLException e) {
            throw new DAOException("Failed to load users.", e);
        } finally {
            DaoHelper.closeResources(connection, statement);
        }
    }

    @Override
    public User getByEmail(String email) throws DAOException {
        PreparedStatement statement = null;
        Connection connection = null;

        try {
            connection = PoolHelper.getInstance().getDataSource().getPool().getConnection();
            statement = connection.prepareStatement(userQueries.getString("user.get.all.by_email"));
            statement.setString(1, email);

            return getSingleByStatement(statement);

        } catch (SQLException e) {
            throw new DAOException("Failed to load user.", e);
        } finally {
            DaoHelper.closeResources(connection, statement);
        }
    }

    @Override
    public void addResult(Long userId, Long subjectId, Short mark) throws DAOException {
        PreparedStatement statement = null;
        Connection connection = null;

        try {
            connection = PoolHelper.getInstance().getDataSource().getPool().getConnection();
            statement = connection.prepareStatement(userQueries.getString("result.add"));

            statement.setLong(1, userId);
            statement.setLong(2, subjectId);
            statement.setShort(3, mark);

            int affectedRows = statement.executeUpdate();
            if(affectedRows == 0) {
                throw new DAOException("Failed to save result.");
            }
        } catch (SQLException e) {
            throw new DAOException("Failed to save result.", e);
        } finally {
            DaoHelper.closeResources(connection, statement);
        }
    }

    @Override
    public Map<Long, Short> getResultsOfUser(Long userId) throws DAOException {
        PreparedStatement statement = null;
        Connection connection = null;

        try {
            connection = PoolHelper.getInstance().getDataSource().getPool().getConnection();
            statement = connection.prepareStatement(userQueries.getString("result.get.all"));
            statement.setLong(1, userId);

            Map<Long, Short> results = new HashMap<>();

            try(ResultSet rs = statement.executeQuery()) {
                while(rs.next()) {
                    results.put(rs.getLong("subject_id"), rs.getShort("mark"));
                }
            }
            return results;

        } catch (SQLException e) {
            throw new DAOException("Failed to get results.", e);
        } finally {
            DaoHelper.closeResources(connection, statement);
        }
    }

    @Override
    public void deleteResult(Long userId, Long subjectId) throws DAOException {
        DaoHelper.delete(userQueries.getString("result.delete"), "Failed to delete exam result.", userId, subjectId);
    }

    @Override
    public int checkEmail(String email) throws DAOException {
        PreparedStatement statement = null;
        Connection connection = null;

        try {
            connection = PoolHelper.getInstance().getDataSource().getPool().getConnection();
            statement = connection.prepareStatement(userQueries.getString("user.get.email_count"));

            statement.setString(1, email);

            try(ResultSet rs = statement.executeQuery()) {
                if(rs.next()) {
                    return rs.getInt(1);
                }
                else {
                    throw new DAOException("Failed to check email.");
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Failed to check email.", e);
        } finally {
            DaoHelper.closeResources(connection, statement);
        }
    }

    @Override
    public AuthorizationResult authorize(String login, String password) throws DAOException {
        PreparedStatement statement = null;
        Connection connection = null;

        try {
            connection = PoolHelper.getInstance().getDataSource().getPool().getConnection();
            statement = connection.prepareStatement(userQueries.getString("user.authorize"));

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
                    return AuthorizationResult.ABSENT;
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Failed to authorize.", e);
        } finally {
            DaoHelper.closeResources(connection, statement);
        }
    }

    private User getSingleByStatement(PreparedStatement statement) throws SQLException {
        try (ResultSet rs = statement.executeQuery()) {
            if (!rs.next()) {
                return null;
            }

            Long id = rs.getLong("id");
            String name = rs.getString("name");
            String secondName = rs.getString("last_name");
            String address = rs.getString("address");
            String email = rs.getString("email");
            String phoneNumber = rs.getString("phone_number");
            LocalDate birthDate = rs.getDate("birth_date").toLocalDate();
            Byte averageMark = rs.getByte("average_mark");

            Map<Long, Short> results = new HashMap<>();

            Long subjectId = rs.getLong("subject_id");
            Short mark = rs.getShort("mark");

            if (subjectId != null && mark != null) {
                results.put(subjectId, mark);
            }

            while (rs.next()) {
                results.put(rs.getLong("subject_id"), rs.getShort("mark"));
            }

            return new User(id, name, secondName, address, email, phoneNumber, birthDate, averageMark, results);
        }
    }

    private List<User> getAllByStatement(PreparedStatement statement) throws SQLException {
        List<User> users = new ArrayList<>();

        try(ResultSet rs = statement.executeQuery()) {
            while(rs.next()) {
                Long id = rs.getLong("id");
                String name = rs.getString("name");
                String secondName = rs.getString("last_name");
                String address = rs.getString("address");
                String email = rs.getString("email");
                String phoneNumber = rs.getString("phone_number");
                LocalDate birthDate = rs.getDate("birth_date").toLocalDate();
                Byte averageMark = rs.getByte("average_mark");

                Map<Long, Short> results = new HashMap<>();

                Long subjectId = rs.getLong("subject_id");
                Short mark = rs.getShort("mark");

                if (subjectId != null && mark != null) {
                    results.put(subjectId, mark);
                }

                while (rs.next()) {
                    Long nextId = rs.getLong("id");
                    if(id.equals(nextId)) {
                        results.put(rs.getLong("subject_id"), rs.getShort("mark"));
                    } else {
                        rs.previous();
                        break;
                    }
                }
                users.add(new User(id, name, secondName, address, email, phoneNumber, birthDate, averageMark, results));
            }
        }
        return users;
    }
}