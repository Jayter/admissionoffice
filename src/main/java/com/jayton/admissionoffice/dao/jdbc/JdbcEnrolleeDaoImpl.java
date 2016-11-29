package com.jayton.admissionoffice.dao.jdbc;

import com.jayton.admissionoffice.dao.EnrolleDao;
import com.jayton.admissionoffice.dao.exception.DAOException;
import com.jayton.admissionoffice.dao.jdbc.pool.PoolHelper;
import com.jayton.admissionoffice.model.to.ExamResult;
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

    public static final String SQL_GET_RESULTS = "SELECT * FROM exam_results WHERE enrollee_id=?";
    public static final String SQL_ADD_RESULTS = "INSERT INTO exam_results (enrollee_id, subject_id, mark) values (?, ?, ?)";
    public static final String SQL_UPDATE_RESULTS = "UPDATE exam_results SET mark=? WHERE enrollee_id=? AND subject_id=?";
    public static final String SQL_DELETE_RESULTS = "DELETE FROM exam_results WHERE enrollee_id=? AND subject_id=?";

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

    @Override
    public void addResults(List<ExamResult> results) throws DAOException {
        PreparedStatement statement = null;
        Connection connection = null;

        try {
            connection = PoolHelper.getDataSource().getPool().getConnection();
            statement = connection.prepareStatement(SQL_ADD_RESULTS);
            for(ExamResult result: results) {
                statement.setLong(1, result.getUserId());
                statement.setLong(2, result.getSubjectId());
                statement.setBigDecimal(3, result.getMark().setScale(2, BigDecimal.ROUND_HALF_UP));
                statement.addBatch();
            }

            int[] affectedRows = statement.executeBatch();
            for(int row: affectedRows) {
                if(row == 0) {
                    throw new DAOException("Failed to save results.");
                }
            }

        } catch (SQLException | NullPointerException e) {
            throw new DAOException("Failed to save results.", e);
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
    public void deleteResults(ExamResult result) throws DAOException {
        PreparedStatement statement = null;
        Connection connection = null;

        try {
            connection = PoolHelper.getDataSource().getPool().getConnection();
            statement = connection.prepareStatement(SQL_DELETE_RESULTS);

            statement.setLong(1, result.getUserId());
            statement.setLong(2, result.getSubjectId());

            int affectedRows = statement.executeUpdate();
            if(affectedRows == 0) {
                throw new DAOException("Failed to delete result.");
            }

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
    public void updateResults(ExamResult result) throws DAOException {
        PreparedStatement statement = null;
        Connection connection = null;

        try {
            connection = PoolHelper.getDataSource().getPool().getConnection();
            statement = connection.prepareStatement(SQL_UPDATE_RESULTS);

            statement.setBigDecimal(1, result.getMark());
            statement.setLong(2, result.getUserId());
            statement.setLong(3, result.getSubjectId());

            int affectedRows = statement.executeUpdate();
            if(affectedRows == 0) {
                throw new DAOException("Failed to update result.");
            }

        } catch (SQLException | NullPointerException e) {
            throw new DAOException("Failed to update result.", e);
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
    public List<ExamResult> getResults(Long userId) throws DAOException {
        List<ExamResult> results = new ArrayList<>();
        PreparedStatement statement = null;
        Connection connection = null;

        try {
            connection = PoolHelper.getDataSource().getPool().getConnection();
            statement = connection.prepareStatement(SQL_GET_RESULTS);

            statement.setLong(1, userId);

            ExamResult result;

            try(ResultSet rs = statement.executeQuery()) {
                while(rs.next()) {
                    result = new ExamResult();

                    result.setUserId(rs.getLong("enrollee_id"));
                    result.setSubjectId(rs.getLong("subject_id"));
                    result.setMark(rs.getBigDecimal("mark").setScale(2, BigDecimal.ROUND_HALF_UP));

                    results.add(result);
                }
            }

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
        return results;
    }
}
