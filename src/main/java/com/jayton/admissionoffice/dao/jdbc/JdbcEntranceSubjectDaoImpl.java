package com.jayton.admissionoffice.dao.jdbc;

import com.jayton.admissionoffice.dao.EntranceSubjectDao;
import com.jayton.admissionoffice.dao.exception.DAOException;
import com.jayton.admissionoffice.dao.jdbc.pool.PoolHelper;
import com.jayton.admissionoffice.model.to.EntranceSubject;

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
public class JdbcEntranceSubjectDaoImpl implements EntranceSubjectDao {

    public static final String SQL_GET = "SELECT * FROM entrance_subjects WHERE direction_id=?";
    public static final String SQL_ADD = "INSERT INTO entrance_subjects (direction_id, subject_id, coefficient)" +
            " VALUES (?, ?, ?)";
    public static final String SQL_DELETE = "DELETE FROM entrance_subjects WHERE direction_id=? AND subject_id=?";

    private static JdbcEntranceSubjectDaoImpl instance;

    private JdbcEntranceSubjectDaoImpl() {
    }

    public static JdbcEntranceSubjectDaoImpl getInstance() {
        if (instance == null) {
            instance = new JdbcEntranceSubjectDaoImpl();
        }
        return instance;
    }

    @Override
    public void addSubject(EntranceSubject subject) throws DAOException {
        PreparedStatement statement = null;
        Connection connection = null;

        try {
            connection = PoolHelper.getInstance().getDataSource().getPool().getConnection();
            statement = connection.prepareStatement(SQL_ADD);

            statement.setLong(1, subject.getDirectionId());
            statement.setLong(2, subject.getSubjectId());
            statement.setBigDecimal(3, scale(subject.getCoef(), 2));
            statement.addBatch();

            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new DAOException("Failed to save entrance subject.");
            }

        } catch (SQLException | NullPointerException e) {
            throw new DAOException("Failed to save entrance subject.", e);
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    //will log it
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    //will log it
                }
            }
        }
    }

    @Override
    public void addSubjects(List<EntranceSubject> subjects) throws DAOException {
        PreparedStatement statement = null;
        Connection connection = null;

        try {
            connection = PoolHelper.getInstance().getDataSource().getPool().getConnection();
            statement = connection.prepareStatement(SQL_ADD);
            for (EntranceSubject subject : subjects) {
                statement.setLong(1, subject.getDirectionId());
                statement.setLong(2, subject.getSubjectId());
                statement.setBigDecimal(3, scale(subject.getCoef(), 2));
                statement.addBatch();
            }

            int[] affectedRows = statement.executeBatch();
            for (int row : affectedRows) {
                if (row == 0) {
                    throw new DAOException("Failed to save entrance subjects.");
                }
            }

        } catch (SQLException | NullPointerException e) {
            throw new DAOException("Failed to save entrance subjects.", e);
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    //will log it
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    //will log it
                }
            }
        }
    }

    @Override
    public boolean deleteSubject(EntranceSubject subject) throws DAOException {
        PreparedStatement statement = null;
        Connection connection = null;

        try {
            connection = PoolHelper.getInstance().getDataSource().getPool().getConnection();
            statement = connection.prepareStatement(SQL_DELETE);

            statement.setLong(1, subject.getDirectionId());
            statement.setLong(2, subject.getSubjectId());

            int affectedRows = statement.executeUpdate();
            return affectedRows != 0;

        } catch (SQLException | NullPointerException e) {
            throw new DAOException("Failed to delete entrance subject.", e);
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    //will log it
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    //will log it
                }
            }
        }
    }

    @Override
    public List<EntranceSubject> getSubjects(Long directionId) throws DAOException {
        PreparedStatement statement = null;
        Connection connection = null;

        try {
            connection = PoolHelper.getInstance().getDataSource().getPool().getConnection();
            statement = connection.prepareStatement(SQL_GET);
            statement.setLong(1, directionId);

            List<EntranceSubject> subjects = new ArrayList<>();

            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    Long subjectId = rs.getLong("subject_id");
                    BigDecimal coef = scale(rs.getBigDecimal("coefficient"), 2);

                    subjects.add(new EntranceSubject(directionId, subjectId, coef));
                }
            }
            return subjects;

        } catch (SQLException | NullPointerException e) {
            throw new DAOException("Failed to get entrance subjects.", e);
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    //will log it
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    //will log it
                }
            }
        }
    }
}
