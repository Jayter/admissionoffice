package com.jayton.admissionoffice.dao.jdbc;

import com.jayton.admissionoffice.dao.DirectionDao;
import com.jayton.admissionoffice.dao.exception.DAOException;
import com.jayton.admissionoffice.dao.jdbc.pool.PoolHelper;
import com.jayton.admissionoffice.model.university.Direction;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public static final String SQL_ADD_SUBJECT = "INSERT INTO entrance_subjects (direction_id, subject_id, coefficient)" +
            " VALUES (?, ?, ?)";
    public static final String SQL_UPDATE_SUBJECT = "UPDATE entrance_subjects SET coefficient=?" +
            "WHERE direction_id=? AND subject_id=?";
    public static final String SQL_GET_SUBJECTS = "SELECT subject_id, coefficient FROM entrance_subjects WHERE direction_id=?";
    public static final String SQL_DELETE_SUBJECT = "DELETE FROM entrance_subjects WHERE direction_id=? AND subject_id=?";

    JdbcDirectionDaoImpl() {
    }

    public Long add(Direction direction) throws DAOException {
        PreparedStatement addDirectionSt = null;
        PreparedStatement addSubjectSt = null;
        Connection connection = null;

        try {
            connection = PoolHelper.getInstance().getDataSource().getPool().getConnection();
            addDirectionSt = connection.prepareStatement(SQL_ADD, Statement.RETURN_GENERATED_KEYS);
            addSubjectSt = connection.prepareStatement(SQL_ADD_SUBJECT);
            connection.setAutoCommit(false);

            addDirectionSt.setString(1, direction.getName());
            addDirectionSt.setBigDecimal(2, scale(direction.getAverageCoefficient(), 2));
            addDirectionSt.setInt(3, direction.getCountOfStudents());
            addDirectionSt.setLong(4, direction.getFacultyId());

            int affectedRow = addDirectionSt.executeUpdate();
            if(affectedRow == 0) {
                throw new DAOException("Failed to save direction.");
            }

            Long id;
            try (ResultSet rs = addDirectionSt.getGeneratedKeys()) {
                if (rs.next()) {
                    id = rs.getLong(1);
                } else {
                    throw new DAOException("Failed to get direction`s id.");
                }
            }

            for(Map.Entry<Long, BigDecimal> pair: direction.getEntranceSubjects().entrySet()) {
                addSubjectSt.setLong(1, id);
                addSubjectSt.setLong(2, pair.getKey());
                addSubjectSt.setBigDecimal(3, scale(pair.getValue(), 2));
                addSubjectSt.addBatch();
            }

            int[] affectedRows = addSubjectSt.executeBatch();
            for (int row : affectedRows) {
                if (row == 0) {
                    throw new DAOException("Failed to save entrance subjects.");
                }
            }
            connection.commit();

            return id;
        } catch (SQLException | NullPointerException e) {
            throw new DAOException("Failed to save direction.", e);
        } finally {
            if(addDirectionSt != null) {
                try {
                    addDirectionSt.close();
                } catch (SQLException e) {
                    //will log it
                }
            }
            if(addSubjectSt != null) {
                try {
                    addSubjectSt.close();
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
    public Direction get(Long id) throws DAOException {
        PreparedStatement getDirectionSt = null;
        PreparedStatement getSubjectsSt = null;
        Connection connection = null;

        try {
            connection = PoolHelper.getInstance().getDataSource().getPool().getConnection();
            getDirectionSt = connection.prepareStatement(SQL_GET);
            getSubjectsSt = connection.prepareStatement(SQL_GET_SUBJECTS);
            connection.setAutoCommit(false);

            getDirectionSt.setLong(1, id);
            getSubjectsSt.setLong(1, id);

            try(ResultSet directionRs = getDirectionSt.executeQuery()) {
                if(!directionRs.next()) {
                    return null;
                }

                String name = directionRs.getString("name");
                BigDecimal averageCoef = scale(directionRs.getBigDecimal("average_coef"), 2);
                Integer countOfStudents = directionRs.getInt("count_of_students");
                Long facultyId = directionRs.getLong("faculty_id");

                Map<Long, BigDecimal> subjects = new HashMap<>();

                try(ResultSet subjectRs = getSubjectsSt.executeQuery()) {
                    while (subjectRs.next()) {
                        subjects.put(subjectRs.getLong("subject_id"), scale(subjectRs.getBigDecimal("coefficient"), 2));
                    }
                }
                connection.commit();

                return new Direction(id, name, averageCoef, countOfStudents, facultyId, subjects);
            }
        } catch (SQLException e) {
            throw new DAOException("Failed to load direction.", e);
        } finally {
            if(getDirectionSt != null) {
                try {
                    getDirectionSt.close();
                } catch (SQLException e) {
                    //will log it
                }
            }
            if(getSubjectsSt != null) {
                try {
                    getSubjectsSt.close();
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
        PreparedStatement getByFacultySt = null;
        PreparedStatement getSubjectsSt = null;
        Connection connection = null;

        try {
            connection = PoolHelper.getInstance().getDataSource().getPool().getConnection();
            getByFacultySt = connection.prepareStatement(SQL_GET_BY_FACULTY);
            getSubjectsSt = connection.prepareStatement(SQL_GET_SUBJECTS);
            connection.setAutoCommit(false);

            getByFacultySt.setLong(1, facultyId);

            List<Direction> directions = new ArrayList<>();

            try(ResultSet rs = getByFacultySt.executeQuery()) {
                while (rs.next()) {

                    Long id = rs.getLong("id");
                    String name = rs.getString("name");
                    BigDecimal averageCoef = scale(rs.getBigDecimal("average_coef"), 2);
                    Integer countOfStudents = rs.getInt("count_of_students");

                    getSubjectsSt.setLong(1, id);

                    Map<Long, BigDecimal> subjects = new HashMap<>();

                    try(ResultSet subjectRs = getSubjectsSt.executeQuery()) {
                        while (subjectRs.next()) {
                            subjects.put(subjectRs.getLong("subject_id"), scale(subjectRs.getBigDecimal("coefficient"),2));
                        }
                    }

                    directions.add(new Direction(id, name, averageCoef, countOfStudents, facultyId, subjects));
                }
            }
            connection.commit();

            return directions;

        } catch (SQLException e) {
            throw new DAOException("Failed to load directions.", e);
        } finally {
            if(getByFacultySt != null) {
                try {
                    getByFacultySt.close();
                } catch (SQLException e) {
                    //will log it
                }
            }
            if(getSubjectsSt != null) {
                try {
                    getSubjectsSt.close();
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
        PreparedStatement getByFacultySt = null;
        PreparedStatement getSubjectsSt = null;
        Connection connection = null;

        try {
            connection = PoolHelper.getInstance().getDataSource().getPool().getConnection();
            getByFacultySt = connection.prepareStatement(SQL_GET_ALL);
            getSubjectsSt = connection.prepareStatement(SQL_GET_SUBJECTS);
            connection.setAutoCommit(false);

            List<Direction> directions = new ArrayList<>();

            try(ResultSet rs = getByFacultySt.executeQuery()) {
                while (rs.next()) {

                    Long id = rs.getLong("id");
                    String name = rs.getString("name");
                    BigDecimal averageCoef = scale(rs.getBigDecimal("average_coef"), 2);
                    Integer countOfStudents = rs.getInt("count_of_students");
                    Long facultyId = rs.getLong("faculty_id");

                    getSubjectsSt.setLong(1, id);

                    Map<Long, BigDecimal> subjects = new HashMap<>();

                    try(ResultSet subjectRs = getSubjectsSt.executeQuery()) {
                        while (subjectRs.next()) {
                            subjects.put(subjectRs.getLong("subject_id"), scale(subjectRs.getBigDecimal("coefficient"), 2));
                        }
                    }

                    directions.add(new Direction(id, name, averageCoef, countOfStudents, facultyId, subjects));
                }
            }
            connection.commit();

            return directions;

        } catch (SQLException e) {
            throw new DAOException("Failed to load directions.", e);
        } finally {
            if(getByFacultySt != null) {
                try {
                    getByFacultySt.close();
                } catch (SQLException e) {
                    //will log it
                }
            }
            if(getSubjectsSt != null) {
                try {
                    getSubjectsSt.close();
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
        PreparedStatement updateDirection = null;
        PreparedStatement updateSubject = null;
        Connection connection = null;

        try {
            connection = PoolHelper.getInstance().getDataSource().getPool().getConnection();
            updateDirection = connection.prepareStatement(SQL_UPDATE);
            updateSubject = connection.prepareStatement(SQL_UPDATE_SUBJECT);
            connection.setAutoCommit(false);

            updateDirection.setString(1, direction.getName());
            updateDirection.setBigDecimal(2, scale(direction.getAverageCoefficient(), 2));
            updateDirection.setInt(3, direction.getCountOfStudents());
            updateDirection.setLong(4, direction.getId());

            int affectedRow = updateDirection.executeUpdate();
            if(affectedRow == 0) {
                throw new DAOException("Failed to update direction.");
            }

            for(Map.Entry<Long, BigDecimal> pair: direction.getEntranceSubjects().entrySet()) {
                updateSubject.setBigDecimal(1, scale(pair.getValue(), 2));
                updateSubject.setLong(2, direction.getId());
                updateSubject.setLong(3, pair.getKey());
                updateSubject.addBatch();
            }

            int[] affectedRows = updateSubject.executeBatch();
            for (int row : affectedRows) {
                if (row == 0) {
                    throw new DAOException("Failed to save entrance subjects.");
                }
            }
            connection.commit();

        } catch (SQLException | NullPointerException e) {
            throw new DAOException("Failed to update direction.", e);
        } finally {
            if(updateDirection != null) {
                try {
                    updateDirection.close();
                } catch (SQLException e) {
                    //will log it
                }
            }
            if(updateSubject != null) {
                try {
                    updateSubject.close();
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

    @Override
    public void addSubject(Long directionId, Long subjectId, BigDecimal coef) throws DAOException {
        PreparedStatement statement = null;
        Connection connection = null;

        try {
            connection = PoolHelper.getInstance().getDataSource().getPool().getConnection();
            statement = connection.prepareStatement(SQL_ADD_SUBJECT);

            statement.setLong(1, directionId);
            statement.setLong(2, subjectId);
            statement.setBigDecimal(3, coef);

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
    public boolean deleteSubject(Long directionId, Long subjectId) throws DAOException {
        PreparedStatement statement = null;
        Connection connection = null;

        try {
            connection = PoolHelper.getInstance().getDataSource().getPool().getConnection();
            statement = connection.prepareStatement(SQL_DELETE_SUBJECT);

            statement.setLong(1, directionId);
            statement.setLong(2, subjectId);

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
    public Map<Long, BigDecimal> getByDirection(Long directionId) throws DAOException {
        PreparedStatement statement = null;
        Connection connection = null;

        try {
            connection = PoolHelper.getInstance().getDataSource().getPool().getConnection();
            statement = connection.prepareStatement(SQL_GET_SUBJECTS);
            statement.setLong(1, directionId);

            Map<Long, BigDecimal> results = new HashMap<>();

            try(ResultSet rs = statement.executeQuery()) {
                while(rs.next()) {
                    Long subjectId = rs.getLong("subject_id");
                    BigDecimal coef = scale(rs.getBigDecimal("coefficient"), 2);

                    results.put(subjectId, coef);
                }
            }
            return results;

        } catch (SQLException | NullPointerException e) {
            throw new DAOException("Failed to get subjects.", e);
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
