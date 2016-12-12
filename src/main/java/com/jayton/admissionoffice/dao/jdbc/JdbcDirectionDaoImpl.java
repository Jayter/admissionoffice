package com.jayton.admissionoffice.dao.jdbc;

import com.jayton.admissionoffice.dao.DirectionDao;
import com.jayton.admissionoffice.dao.exception.DAOException;
import com.jayton.admissionoffice.dao.jdbc.pool.PoolHelper;
import com.jayton.admissionoffice.dao.jdbc.util.DaoHelper;
import com.jayton.admissionoffice.model.university.Direction;

import java.math.BigDecimal;
import java.sql.*;
import java.util.*;

public class JdbcDirectionDaoImpl implements DirectionDao {

    private final ResourceBundle directionQueries = ResourceBundle.getBundle("db.queries.directionQueries");

    JdbcDirectionDaoImpl() {
    }

    public Direction add(Direction direction) throws DAOException {
        PreparedStatement addDirectionSt = null;
        PreparedStatement addSubjectSt = null;
        Connection connection = null;

        try {
            connection = PoolHelper.getInstance().getDataSource().getPool().getConnection();
            addDirectionSt = connection.prepareStatement(directionQueries.getString("direction.add"),
                    Statement.RETURN_GENERATED_KEYS);
            addSubjectSt = connection.prepareStatement(directionQueries.getString("subject.add"));
            connection.setAutoCommit(false);

            addDirectionSt.setString(1, direction.getName());
            addDirectionSt.setBigDecimal(2, direction.getAverageCoefficient());
            addDirectionSt.setInt(3, direction.getCountOfStudents());
            addDirectionSt.setLong(4, direction.getFacultyId());

            int affectedRow = addDirectionSt.executeUpdate();
            if(affectedRow == 0) {
                throw new DAOException("Failed to save direction.");
            }

            long id;
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
                addSubjectSt.setBigDecimal(3, pair.getValue());
                addSubjectSt.addBatch();
            }

            int[] affectedRows = addSubjectSt.executeBatch();
            for (int row : affectedRows) {
                if (row == 0) {
                    throw new DAOException("Failed to save entrance subjects.");
                }
            }
            connection.commit();

            return new Direction(id, direction.getName(), direction.getAverageCoefficient(),
                    direction.getCountOfStudents(), direction.getFacultyId(), direction.getEntranceSubjects());
        } catch (SQLException e) {
            throw new DAOException("Failed to save direction.", e);
        } finally {
            DaoHelper.closeResources(connection, addDirectionSt, addSubjectSt);
        }
    }

    @Override
    public Direction get(long id) throws DAOException {
        PreparedStatement getDirectionSt = null;
        Connection connection = null;

        try {
            connection = PoolHelper.getInstance().getDataSource().getPool().getConnection();
            getDirectionSt = connection.prepareStatement(directionQueries.getString("direction.get"));

            getDirectionSt.setLong(1, id);

            try(ResultSet rs = getDirectionSt.executeQuery()) {
                if(!rs.next()) {
                    return null;
                }

                String name = rs.getString("name");
                BigDecimal averageCoef = rs.getBigDecimal("average_coef");
                int countOfStudents = rs.getInt("count_of_students");
                long facultyId = rs.getLong("faculty_id");

                Map<Long, BigDecimal> subjects = new HashMap<>();

                long subjectId = rs.getLong("subject_id");
                BigDecimal coefficient = rs.getBigDecimal("coefficient");

                if(subjectId != 0 && coefficient != null) {
                    subjects.put(subjectId, coefficient);
                }

                while (rs.next()) {
                    subjects.put(rs.getLong("subject_id"), rs.getBigDecimal("coefficient"));
                }

                return new Direction(id, name, averageCoef, countOfStudents, facultyId, subjects);
            }
        } catch (SQLException e) {
            throw new DAOException("Failed to load direction.", e);
        } finally {
            DaoHelper.closeResources(connection, getDirectionSt);
        }
    }

    @Override
    public Direction update(Direction direction) throws DAOException {
        PreparedStatement updateDirectionSt = null;
        PreparedStatement getSubjectsSt = null;
        Connection connection = null;

        try {
            connection = PoolHelper.getInstance().getDataSource().getPool().getConnection();
            updateDirectionSt = connection.prepareStatement(directionQueries.getString("direction.update"));
            getSubjectsSt = connection.prepareStatement(directionQueries.getString("subject.get.all"));
            connection.setAutoCommit(false);

            updateDirectionSt.setString(1, direction.getName());
            updateDirectionSt.setBigDecimal(2, direction.getAverageCoefficient());
            updateDirectionSt.setInt(3, direction.getCountOfStudents());
            updateDirectionSt.setLong(4, direction.getId());

            int affectedRow = updateDirectionSt.executeUpdate();
            if(affectedRow == 0) {
                throw new DAOException("Failed to update direction.");
            }

            Map<Long, BigDecimal> entranceSubjects = new HashMap<>();

            getSubjectsSt.setLong(1, direction.getId());

            try(ResultSet rs = getSubjectsSt.executeQuery()) {
                while (rs.next()) {
                    entranceSubjects.put(rs.getLong("subject_id"), rs.getBigDecimal("coefficient"));
                }
            }
            connection.commit();

            return new Direction(direction.getId(), direction.getName(), direction.getAverageCoefficient(),
                    direction.getCountOfStudents(), direction.getFacultyId(), entranceSubjects);
        } catch (SQLException e) {
            DaoHelper.rollback(connection);
            throw new DAOException("Failed to update direction.", e);
        } finally {
            DaoHelper.closeResources(connection, updateDirectionSt, getSubjectsSt);
        }
    }

    @Override
    public void delete(long id) throws DAOException {
        DaoHelper.delete(directionQueries.getString("direction.delete"), "Failed to delete direction.", id);
    }

    @Override
    public List<Direction> getAll() throws DAOException {
        PreparedStatement statement = null;
        Connection connection = null;

        try {
            connection = PoolHelper.getInstance().getDataSource().getPool().getConnection();
            statement = connection.prepareStatement(directionQueries.getString("direction.get.all"));

            return getByStatement(statement);

        } catch (SQLException e) {
            throw new DAOException("Failed to load directions.", e);
        } finally {
            DaoHelper.closeResources(connection, statement);
        }
    }

    @Override
    public List<Direction> getByFaculty(long facultyId) throws DAOException {
        PreparedStatement statement = null;
        Connection connection = null;

        try {
            connection = PoolHelper.getInstance().getDataSource().getPool().getConnection();
            statement = connection.prepareStatement(directionQueries.getString("direction.get.all.by_faculty"));
            statement.setLong(1, facultyId);

            return getByStatement(statement);

        } catch (SQLException e) {
            throw new DAOException("Failed to load directions.", e);
        } finally {
            DaoHelper.closeResources(connection, statement);
        }
    }

    @Override
    public void addSubject(long directionId, long subjectId, BigDecimal coef) throws DAOException {
        PreparedStatement statement = null;
        Connection connection = null;

        try {
            connection = PoolHelper.getInstance().getDataSource().getPool().getConnection();
            statement = connection.prepareStatement(directionQueries.getString("subject.add"));

            statement.setLong(1, directionId);
            statement.setLong(2, subjectId);
            statement.setBigDecimal(3, coef);

            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new DAOException("Failed to save entrance subject.");
            }

        } catch (SQLException e) {
            throw new DAOException("Failed to save entrance subject.", e);
        } finally {
            DaoHelper.closeResources(connection, statement);
        }
    }

    @Override
    public void deleteSubject(long directionId, long subjectId) throws DAOException {
        DaoHelper.delete(directionQueries.getString("subject.delete"), "Failed to delete entrance subject.",
                directionId, subjectId);
    }

    @Override
    public Map<Long, BigDecimal> getEntranceSubjects(long directionId) throws DAOException {
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = PoolHelper.getInstance().getDataSource().getConnection();
            statement = connection.prepareStatement(directionQueries.getString("subject.get.all"));
            statement.setLong(1, directionId);

            Map<Long, BigDecimal> entranceSubjects = new HashMap<>();

            try(ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    entranceSubjects.put(rs.getLong("subject_id"), rs.getBigDecimal("coefficient"));
                }
            }
            return entranceSubjects;

        } catch (SQLException e) {
            throw new DAOException("Failed to get entrance subjects.", e);
        } finally {
            DaoHelper.closeResources(connection, statement);
        }
    }

    private List<Direction> getByStatement(PreparedStatement statement) throws SQLException {
        List<Direction> directions = new ArrayList<>();

        try (ResultSet rs = statement.executeQuery()) {
            while (rs.next()) {
                long id = rs.getLong("id");
                long facultyId = rs.getLong("faculty_id");
                String name = rs.getString("name");
                BigDecimal averageCoef = rs.getBigDecimal("average_coef");
                int countOfStudents = rs.getInt("count_of_students");

                Map<Long, BigDecimal> subjects = new HashMap<>();

                long subjectId = rs.getLong("subject_id");
                BigDecimal coefficient = rs.getBigDecimal("coefficient");

                if(subjectId != 0 && coefficient != null) {
                    subjects.put(subjectId, coefficient);
                }

                while (rs.next()) {
                    long nextId = rs.getLong("id");
                    if (id == nextId) {
                        subjects.put(rs.getLong("subject_id"), rs.getBigDecimal("coefficient"));
                    } else {
                        rs.previous();
                        break;
                    }
                }
                directions.add(new Direction(id, name, averageCoef, countOfStudents, facultyId, subjects));
            }
        }
        return directions;
    }
}