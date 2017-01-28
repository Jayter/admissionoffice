package com.jayton.admissionoffice.dao.jdbc;

import com.jayton.admissionoffice.dao.DirectionDao;
import com.jayton.admissionoffice.dao.exception.DAOException;
import com.jayton.admissionoffice.dao.jdbc.util.DaoHelper;
import com.jayton.admissionoffice.model.to.EntriesWithAssociatedPairsDto;
import com.jayton.admissionoffice.model.to.AssociatedPairDto;
import com.jayton.admissionoffice.model.to.PaginationDTO;
import com.jayton.admissionoffice.model.university.Direction;
import com.jayton.admissionoffice.util.di.Injected;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.*;
import java.util.*;

public class JdbcDirectionDaoImpl implements DirectionDao {

    private final ResourceBundle directionQueries = ResourceBundle.getBundle("db.queries.directionQueries");

    @Injected
    private DataSource dataSource;
    @Injected
    private DaoHelper daoHelper;

    public JdbcDirectionDaoImpl() {
    }

    @Override
    public long add(Direction direction) throws DAOException {
        try(Connection connection = dataSource.getConnection();
            PreparedStatement addDirectionSt = connection.prepareStatement(directionQueries.getString("direction.add"),
                    Statement.RETURN_GENERATED_KEYS);
            PreparedStatement addSubjectSt = connection.prepareStatement(directionQueries.getString("subject.add"))) {
            connection.setAutoCommit(false);

            long id;
            try {
                addDirectionSt.setString(1, direction.getName());
                addDirectionSt.setBigDecimal(2, direction.getAverageCoefficient());
                addDirectionSt.setInt(3, direction.getCountOfStudents());
                addDirectionSt.setLong(4, direction.getFacultyId());

                addDirectionSt.executeUpdate();

                try (ResultSet rs = addDirectionSt.getGeneratedKeys()) {
                    rs.next();
                    id = rs.getLong(1);
                }
                for(Map.Entry<Long, BigDecimal> pair: direction.getEntranceSubjects().entrySet()) {
                    addSubjectSt.setLong(1, id);
                    addSubjectSt.setLong(2, pair.getKey());
                    addSubjectSt.setBigDecimal(3, pair.getValue());
                    addSubjectSt.addBatch();
                }
                addSubjectSt.executeBatch();
            } catch (SQLException e) {
                connection.rollback();
                throw e;
            }
            connection.commit();

            return id;
        } catch (SQLException e) {
            throw new DAOException("Failed to save direction.", e);
        }
    }

    @Override
    public Direction get(long id) throws DAOException {
        Direction direction = null;
        try(Connection connection = dataSource.getConnection();
            PreparedStatement getDirectionSt = connection.prepareStatement(directionQueries.getString("direction.get"));
            PreparedStatement getSubjectsSt = connection.prepareStatement(directionQueries.getString("subject.get.all.by_direction"))) {
            connection.setAutoCommit(false);
            //read committed
            getDirectionSt.setLong(1, id);
            getSubjectsSt.setLong(1, id);

            try(ResultSet getDirectionRs = getDirectionSt.executeQuery()) {
               if (getDirectionRs.next()) {
                   String name = getDirectionRs.getString("name");
                   BigDecimal averageCoef = getDirectionRs.getBigDecimal("average_coef").setScale(2, BigDecimal.ROUND_HALF_UP);
                   int countOfStudents = getDirectionRs.getInt("count_of_students");
                   long facultyId = getDirectionRs.getLong("faculty_id");

                   Map<Long, BigDecimal> subjects = new HashMap<>();

                   try (ResultSet getSubjectsRs = getSubjectsSt.executeQuery()) {
                       while (getSubjectsRs.next()) {
                           long subjectId = getSubjectsRs.getLong("subject_id");
                           BigDecimal coefficient = getSubjectsRs.getBigDecimal("coefficient");
                           subjects.put(subjectId, coefficient);
                       }
                   }
                   direction = new Direction(id, name, averageCoef, countOfStudents, facultyId, subjects);
               }
            }
            connection.commit();
        } catch (SQLException e) {
            throw new DAOException("Failed to load direction.", e);
        }
        return direction;
    }

    @Override
    public boolean update(Direction direction) throws DAOException {
        try(Connection connection = dataSource.getConnection();
            PreparedStatement updateDirectionSt = connection.prepareStatement(directionQueries.getString("direction.update"));) {

            updateDirectionSt.setString(1, direction.getName());
            updateDirectionSt.setBigDecimal(2, direction.getAverageCoefficient());
            updateDirectionSt.setInt(3, direction.getCountOfStudents());
            updateDirectionSt.setLong(4, direction.getId());

            return updateDirectionSt.executeUpdate() != 0;
        } catch (SQLException e) {
            throw new DAOException("Failed to update direction.", e);
        }
    }

    @Override
    public boolean delete(long id) throws DAOException {
        return daoHelper.delete(directionQueries.getString("direction.delete"), "Failed to delete direction.", id);
    }

    @Override
    public EntriesWithAssociatedPairsDto<Direction, Long, Long, BigDecimal> getAll() throws DAOException {
        try(Connection connection = dataSource.getConnection();
            PreparedStatement getDirectionsSt = connection.prepareStatement(directionQueries.getString("direction.get.all"));
            PreparedStatement getSubjectsSt = connection.prepareStatement(directionQueries.getString("subject.get.all"))) {
            
            List<Direction> directions = getDirectionsByStatement(getDirectionsSt);
            List<AssociatedPairDto<Long, Long, BigDecimal>> subjects = getSubjectsByStatement(getSubjectsSt);

            return new EntriesWithAssociatedPairsDto<>(directions, subjects);
        } catch (SQLException e) {
            throw new DAOException("Failed to load directions.", e);
        }
    }

    @Override
    public PaginationDTO<EntriesWithAssociatedPairsDto<Direction, Long, Long, BigDecimal>> getWithCountByFaculty(long facultyId, long offset, long count) throws DAOException {
        try(Connection connection = dataSource.getConnection();
            PreparedStatement getDirectionsSt = connection.prepareStatement(directionQueries.getString("direction.get.all.by_faculty"));
            PreparedStatement getSubjectsSt = connection.prepareStatement(directionQueries.getString("subject.get.all.by_faculty"));
            PreparedStatement getTotalCountSt = connection.prepareStatement(directionQueries.getString("direction.count"))) {

            getDirectionsSt.setLong(1, facultyId);
            getDirectionsSt.setLong(2, count);
            getDirectionsSt.setLong(3, offset);
            getSubjectsSt.setLong(1, facultyId);
            getSubjectsSt.setLong(2, count);
            getSubjectsSt.setLong(3, offset);

            List<Direction> directions = getDirectionsByStatement(getDirectionsSt);
            List<AssociatedPairDto<Long, Long, BigDecimal>> subjects = getSubjectsByStatement(getSubjectsSt);
            long totalCount = daoHelper.getCount(getTotalCountSt, facultyId);

            EntriesWithAssociatedPairsDto<Direction, Long, Long, BigDecimal> directionsDTO
                    = new EntriesWithAssociatedPairsDto<>(directions, subjects);
            return new PaginationDTO<>(Collections.singletonList(directionsDTO), totalCount);
        } catch (SQLException e) {
            throw new DAOException("Failed to load directions.", e);
        }
    }

    @Override
    public boolean addSubject(long directionId, long subjectId, BigDecimal coef) throws DAOException {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(directionQueries.getString("subject.add"))) {

            statement.setLong(1, directionId);
            statement.setLong(2, subjectId);
            statement.setBigDecimal(3, coef);

            return statement.executeUpdate() != 0;
        } catch (SQLException e) {
            throw new DAOException("Failed to save entrance subject.", e);
        }
    }

    @Override
    public boolean deleteSubject(long directionId, long subjectId) throws DAOException {
        return daoHelper.delete(directionQueries.getString("subject.delete"), "Failed to delete entrance subject.",
                directionId, subjectId);
    }

    @Override
    public Map<Long, BigDecimal> getSubjects(long directionId) throws DAOException {
        Map<Long, BigDecimal> entranceSubjects = new HashMap<>();
        try(Connection connection = dataSource.getConnection();
            PreparedStatement getDirectionsSt = connection.prepareStatement(directionQueries.getString("subject.get.all.by_direction"))) {

            getDirectionsSt.setLong(1, directionId);

            try(ResultSet rs = getDirectionsSt.executeQuery()) {
                while (rs.next()) {
                    entranceSubjects.put(rs.getLong("subject_id"), rs.getBigDecimal("coefficient"));
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Failed to get entrance subjects.", e);
        }
        return entranceSubjects;
    }

    private List<AssociatedPairDto<Long, Long, BigDecimal>> getSubjectsByStatement(PreparedStatement statement) throws SQLException {
        List<AssociatedPairDto<Long, Long, BigDecimal>> subjects = new ArrayList<>();

        try (ResultSet rs = statement.executeQuery()) {
            while (rs.next()) {
                long directionId = rs.getLong("direction_id");
                long subjectId = rs.getLong("subject_id");
                BigDecimal coefficient = rs.getBigDecimal("coefficient");

                subjects.add(new AssociatedPairDto<>(directionId, subjectId, coefficient));
            }
        }
        return subjects;
    }

    private List<Direction> getDirectionsByStatement(PreparedStatement statement) throws SQLException {
        List<Direction> directions = new ArrayList<>();

        try (ResultSet rs = statement.executeQuery()) {
            while (rs.next()) {
                long id = rs.getLong("id");
                long facultyId = rs.getLong("faculty_id");
                String name = rs.getString("name");
                BigDecimal averageCoef = rs.getBigDecimal("average_coef");
                int countOfStudents = rs.getInt("count_of_students");

                directions.add(new Direction(id, name, averageCoef, countOfStudents, facultyId));
            }
        }
        return directions;
    }
}