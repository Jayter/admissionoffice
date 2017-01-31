package com.jayton.admissionoffice.dao.jdbc;

import com.jayton.admissionoffice.dao.UserDao;
import com.jayton.admissionoffice.dao.exception.DAOException;
import com.jayton.admissionoffice.dao.jdbc.util.DaoHelper;
import com.jayton.admissionoffice.model.to.AssociatedPairDto;
import com.jayton.admissionoffice.model.to.AuthorizationResult;
import com.jayton.admissionoffice.model.to.EntriesWithAssociatedPairsDto;
import com.jayton.admissionoffice.model.to.PaginationDto;
import com.jayton.admissionoffice.model.user.User;
import com.jayton.admissionoffice.util.di.Injected;
import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

public class JdbcUserDaoImpl implements UserDao {

    private final ResourceBundle userQueries = ResourceBundle.getBundle("db.queries.userQueries");
    private final Logger logger = LoggerFactory.getLogger(JdbcUserDaoImpl.class);

    @Injected
    private DataSource dataSource;
    @Injected
    private DaoHelper daoHelper;

    public JdbcUserDaoImpl() {
    }

    @Override
    public long add(User user) throws DAOException {
        logger.info("Adding user: {}.", user);
        try(Connection connection = dataSource.getConnection();
            PreparedStatement addUserSt = connection.prepareStatement(userQueries.getString("user.add"),
                    Statement.RETURN_GENERATED_KEYS);
            PreparedStatement addCredentialsSt = connection.prepareStatement(userQueries.getString("credentials.add"))) {
            connection.setAutoCommit(false);

            long id;
            try {
                addUserSt.setString(1, user.getName());
                addUserSt.setString(2, user.getLastName());
                addUserSt.setString(3, user.getAddress());
                addUserSt.setString(4, user.getEmail());
                addUserSt.setString(5, user.getPhoneNumber());
                addUserSt.setDate(6, Date.valueOf(user.getBirthDate()));
                addUserSt.setByte(7, user.getAverageMark());
                addCredentialsSt.setString(1, user.getEmail());
                addCredentialsSt.setString(2, BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));

                addUserSt.executeUpdate();
                addCredentialsSt.executeUpdate();
                try (ResultSet rs = addUserSt.getGeneratedKeys()) {
                    rs.next();
                    id = rs.getLong(1);
                }
            } catch (SQLException e) {
                connection.rollback();
                throw e;
            }
            connection.commit();

            return id;
        } catch (SQLException e) {
            logger.error("Failed to save user.", e);
            throw new DAOException("Failed to save user.", e);
        }
    }

    @Override
    public User get(long id) throws DAOException {
        logger.info("Getting user by id: {}.", id);
        try(Connection connection = dataSource.getConnection();
            PreparedStatement getUserSt = connection.prepareStatement(userQueries.getString("user.get"));
            PreparedStatement getResultsSt = connection.prepareStatement(userQueries.getString("result.get.all.by_id"))) {

            getUserSt.setLong(1, id);
            getResultsSt.setLong(1, id);

            return getUserByStatements(getUserSt, getResultsSt);
        } catch (SQLException e) {
            logger.error("Failed to load user.", e);
            throw new DAOException("Failed to load user.", e);
        }
    }

    @Override
    public boolean update(User user) throws DAOException {
        logger.info("Updating user: {}.", user);
        try(Connection connection = dataSource.getConnection();
            PreparedStatement updateUserSt = connection.prepareStatement(userQueries.getString("user.update"))) {

            updateUserSt.setString(1, user.getName());
            updateUserSt.setString(2, user.getLastName());
            updateUserSt.setString(3, user.getAddress());
            updateUserSt.setString(4, user.getPhoneNumber());
            updateUserSt.setDate(5, Date.valueOf(user.getBirthDate()));
            updateUserSt.setByte(6, user.getAverageMark());
            updateUserSt.setLong(7, user.getId());

            return updateUserSt.executeUpdate() != 0;
        } catch (SQLException e) {
            logger.error("Failed to update user.", e);
            throw new DAOException("Failed to update user.", e);
        }
    }

    @Override
    public User getByEmail(String email) throws DAOException {
        logger.info("Getting user by email: {}.", email);
        try(Connection connection = dataSource.getConnection();
            PreparedStatement getUserSt = connection.prepareStatement(userQueries.getString("user.get.all.by_email"));
            PreparedStatement getResultsSt = connection.prepareStatement(userQueries.getString("result.get.all.by_email"))) {

            getUserSt.setString(1, email);
            getResultsSt.setString(1, email);

            return getUserByStatements(getUserSt, getResultsSt);
        } catch (SQLException e) {
            logger.error("Failed to load user.", e);
            throw new DAOException("Failed to load user.", e);
        }
    }

    @Override
    public boolean delete(long id) throws DAOException {
        logger.info("Deleting user by id: {}.", id);
        return daoHelper.delete(userQueries.getString("user.delete"), "Failed to delete user.", id);
    }

    @Override
    public PaginationDto<EntriesWithAssociatedPairsDto<User, Long, Long, Short>> getAllWithCount(long offset, long count) throws DAOException {
        logger.info("Getting users: offset: {}, count: {}.", offset, count);
        try(Connection connection = dataSource.getConnection();
            PreparedStatement getUserSt = connection.prepareStatement(userQueries.getString("user.get.all"));
            PreparedStatement getResultsSt = connection.prepareStatement(userQueries.getString("result.get.all"));
            PreparedStatement getTotalCountSt = connection.prepareStatement(userQueries.getString("user.count"))) {

            getUserSt.setLong(1, count);
            getUserSt.setLong(2, offset);

            List<User> users = getUsersByStatement(getUserSt);
            List<AssociatedPairDto<Long, Long, Short>> results = getResultsByStatement(getResultsSt);
            long totalCount = daoHelper.getCount(getTotalCountSt);

            EntriesWithAssociatedPairsDto<User, Long, Long, Short> usersDTO
                    = new EntriesWithAssociatedPairsDto<>(users, results);
            return new PaginationDto(Collections.singletonList(usersDTO), totalCount);
        } catch (SQLException e) {
            logger.error("Failed to load users.", e);
            throw new DAOException("Failed to load users.", e);
        }
    }

    @Override
    public boolean addResult(long userId, long subjectId, short mark) throws DAOException {
        logger.info("Adding user result: userId: {}, subjectId: {}, mark: {}.", userId, subjectId, mark);
        try(Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(userQueries.getString("result.add"))) {

            statement.setLong(1, userId);
            statement.setLong(2, subjectId);
            statement.setShort(3, mark);

            return statement.executeUpdate() != 0;
        } catch (SQLException e) {
            logger.error("Failed to save user result.", e);
            throw new DAOException("Failed to save user result.", e);
        }
    }

    @Override
    public Map<Long, Short> getUserResults(long userId) throws DAOException {
        logger.info("Getting results of user by id: {}.", userId);
        Map<Long, Short> results = new HashMap<>();

        try(Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(userQueries.getString("result.get.all.by_id"))) {

            statement.setLong(1, userId);

            try(ResultSet rs = statement.executeQuery()) {
                while(rs.next()) {
                    results.put(rs.getLong("subject_id"), rs.getShort("mark"));
                }
            }
        } catch (SQLException e) {
            logger.error("Failed to get user results.", e);
            throw new DAOException("Failed to get user results.", e);
        }
        return results;
    }

    @Override
    public boolean deleteResult(long userId, long subjectId) throws DAOException {
        logger.info("Deleting user result: userId: {}, subjectId: {}.", userId, subjectId);
        return daoHelper.delete(userQueries.getString("result.delete"), "Failed to delete exam result.", userId, subjectId);
    }

    @Override
    public int checkEmail(String email) throws DAOException {
        logger.info("Checking email: {}.", email);
        try(Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(userQueries.getString("user.get.email_count"));) {

            statement.setString(1, email);

            try(ResultSet rs = statement.executeQuery()) {
                rs.next();
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            logger.error("Failed to check email.", e);
            throw new DAOException("Failed to check email.", e);
        }
    }

    @Override
    public AuthorizationResult authorize(String login, String password) throws DAOException {
        logger.info("Authorizing by login: {}.", login);
        try(Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(userQueries.getString("user.authorize"))) {

            statement.setString(1, login);

            try(ResultSet rs = statement.executeQuery()) {
                if(rs.next()) {
                    Boolean isCorrect = BCrypt.checkpw(password, rs.getString("password"));
                    if(isCorrect) {
                        if (rs.getBoolean("is_admin")) {
                            return AuthorizationResult.ADMIN;
                        }
                        return AuthorizationResult.USER;
                    }
                }
                return AuthorizationResult.ABSENT;
            }
        } catch (SQLException e) {
            logger.error("Failed to authorize.", e);
            throw new DAOException("Failed to authorize.", e);
        }
    }

    @Override
    public Map<Long, String> getDirectionNames(long userId) throws DAOException {
        logger.info("Getting direction names by user id: {}.", userId);
        Map<Long, String> names = new HashMap<>();

        try(Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(userQueries.getString("user.get.directions"))) {
            statement.setLong(1, userId);

            try(ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    names.put(rs.getLong("id"), rs.getString("name"));
                }
            }
        } catch (SQLException e) {
            logger.error("Failed to get direction names.", e);
            throw new DAOException("Failed to get direction names.", e);
        }
        return names;
    }

    private User getUserByStatements(PreparedStatement getUserSt, PreparedStatement getResultsSt) throws SQLException {
        User user = null;
        try(ResultSet getUserRs = getUserSt.executeQuery()) {
            if(getUserRs.next()) {
                long id = getUserRs.getLong("id");
                String name = getUserRs.getString("name");
                String secondName = getUserRs.getString("last_name");
                String address = getUserRs.getString("address");
                String email = getUserRs.getString("email");
                String phoneNumber = getUserRs.getString("phone_number");
                LocalDate birthDate = getUserRs.getDate("birth_date").toLocalDate();
                byte averageMark = getUserRs.getByte("average_mark");

                Map<Long, Short> results = new HashMap<>();
                try (ResultSet getResultsRs = getResultsSt.executeQuery()) {
                    while (getResultsRs.next()) {
                        results.put(getResultsRs.getLong("subject_id"), getResultsRs.getShort("mark"));
                    }
                }
                user = new User(id, name, secondName, address, email, phoneNumber, birthDate, averageMark, results);
            }
        }
        return user;
    }

    private List<User> getUsersByStatement(PreparedStatement getUserSt) throws SQLException {
        List<User> users = new ArrayList<>();
        try(ResultSet getUserRs = getUserSt.executeQuery()) {
            while(getUserRs.next()) {
                long id = getUserRs.getLong("id");
                String name = getUserRs.getString("name");
                String secondName = getUserRs.getString("last_name");
                String address = getUserRs.getString("address");
                String email = getUserRs.getString("email");
                String phoneNumber = getUserRs.getString("phone_number");
                LocalDate birthDate = getUserRs.getDate("birth_date").toLocalDate();
                byte averageMark = getUserRs.getByte("average_mark");

                users.add(new User(id, name, secondName, address, email, phoneNumber, birthDate, averageMark));
            }
        }
        return users;
    }

    private List<AssociatedPairDto<Long, Long, Short>> getResultsByStatement(PreparedStatement statement) throws SQLException {
        List<AssociatedPairDto<Long, Long, Short>> results = new ArrayList<>();

        try(ResultSet rs = statement.executeQuery()) {
            while(rs.next()) {
                long userId = rs.getLong("user_id");
                long subjectId = rs.getLong("subject_id");
                short mark = rs.getShort("mark");

                results.add(new AssociatedPairDto<>(userId, subjectId, mark));
            }
        }
        return results;
    }
}