package com.jayton.admissionoffice.dao.jdbc;

import com.jayton.admissionoffice.dao.ExamResultDao;
import com.jayton.admissionoffice.dao.exception.DAOException;
import com.jayton.admissionoffice.dao.jdbc.pool.PoolHelper;
import com.jayton.admissionoffice.model.to.ExamResult;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static com.jayton.admissionoffice.dao.jdbc.util.NumericHelper.scale;

/**
 * Created by Jayton on 29.11.2016.
 */
public class JdbcExamResultDaoImpl implements ExamResultDao {

    public static final String SQL_GET = "SELECT * FROM exam_results WHERE user_id=?";
    public static final String SQL_ADD = "INSERT INTO exam_results (user_id, subject_id, mark) values (?, ?, ?)";
    public static final String SQL_UPDATE = "UPDATE exam_results SET mark=? WHERE user_id=? AND subject_id=?";
    public static final String SQL_DELETE = "DELETE FROM exam_results WHERE user_id=? AND subject_id=?";

    private static JdbcExamResultDaoImpl instance;

    private JdbcExamResultDaoImpl() {
    }

    public static JdbcExamResultDaoImpl getInstance() {
        if(instance == null) {
            instance = new JdbcExamResultDaoImpl();
        }
        return instance;
    }

    @Override
    public void add(List<ExamResult> results) throws DAOException {
        PreparedStatement statement = null;
        Connection connection = null;

        try {
            connection = PoolHelper.getInstance().getDataSource().getPool().getConnection();
            statement = connection.prepareStatement(SQL_ADD);

            for(ExamResult result: results) {
                statement.setLong(1, result.getUserId());
                statement.setLong(2, result.getSubjectId());
                statement.setBigDecimal(3, scale(result.getMark(), 2));
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
    public void add(ExamResult result) throws DAOException {
        PreparedStatement statement = null;
        Connection connection = null;

        try {
            connection = PoolHelper.getInstance().getDataSource().getPool().getConnection();
            statement = connection.prepareStatement(SQL_ADD);

            statement.setLong(1, result.getUserId());
            statement.setLong(2, result.getSubjectId());
            statement.setBigDecimal(3, scale(result.getMark(), 2));

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
    public boolean delete(ExamResult result) throws DAOException {
        PreparedStatement statement = null;
        Connection connection = null;

        try {
            connection = PoolHelper.getInstance().getDataSource().getPool().getConnection();
            statement = connection.prepareStatement(SQL_DELETE);

            statement.setLong(1, result.getUserId());
            statement.setLong(2, result.getSubjectId());

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
    public void update(ExamResult result) throws DAOException {
        PreparedStatement statement = null;
        Connection connection = null;

        try {
            connection = PoolHelper.getInstance().getDataSource().getPool().getConnection();
            statement = connection.prepareStatement(SQL_UPDATE);

            statement.setBigDecimal(1, scale(result.getMark(), 2));
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
    public List<ExamResult> getByUser(Long userId) throws DAOException {
        PreparedStatement statement = null;
        Connection connection = null;

        try {
            connection = PoolHelper.getInstance().getDataSource().getPool().getConnection();
            statement = connection.prepareStatement(SQL_GET);
            statement.setLong(1, userId);

            List<ExamResult> results = new ArrayList<>();

            try(ResultSet rs = statement.executeQuery()) {
                while(rs.next()) {
                    Long enrolleeId = rs.getLong("user_id");
                    Long subjectId = rs.getLong("subject_id");
                    BigDecimal mark = scale(rs.getBigDecimal("mark"), 2);

                    results.add(new ExamResult(enrolleeId, subjectId, mark));
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
}
