package com.jayton.admissionoffice.dao.jdbc;

import com.jayton.admissionoffice.dao.UniversityDao;
import com.jayton.admissionoffice.dao.exception.DAOException;
import com.jayton.admissionoffice.dao.jdbc.util.DaoHelper;
import com.jayton.admissionoffice.model.to.PaginationDto;
import com.jayton.admissionoffice.model.university.University;
import com.jayton.admissionoffice.util.di.Injected;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class JdbcUniversityDaoImpl implements UniversityDao {

    private final ResourceBundle universityQueries = ResourceBundle.getBundle("db.queries.universityQueries");
    private final Logger logger = LoggerFactory.getLogger(JdbcUniversityDaoImpl.class);

    @Injected
    private DataSource dataSource;
    @Injected
    private DaoHelper daoHelper;

    public JdbcUniversityDaoImpl() {
    }

    @Override
    public long add(University university) throws DAOException {
        logger.info("Adding university: {}.", university);
        try(Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(universityQueries.getString("university.add"),
                    Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, university.getName());
            statement.setString(2, university.getCity());
            statement.setString(3, university.getAddress());

            statement.executeUpdate();
            try (ResultSet rs = statement.getGeneratedKeys()) {
                rs.next();
                return rs.getLong(1);
            }
        } catch (SQLException e) {
            logger.error("Failed to save university.", e);
            throw new DAOException("Failed to save university.", e);
        }
    }

    @Override
    public University get(long id) throws DAOException {
        logger.info("Getting university by id: {}.", id);
        University university = null;
        try(Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(universityQueries.getString("university.get"))) {

            statement.setLong(1, id);

            try(ResultSet rs = statement.executeQuery()) {
                if(rs.next()) {
                    String name = rs.getString("name");
                    String city = rs.getString("city");
                    String address = rs.getString("address");

                    university = new University(id, name, city, address);
                }
            }
        } catch (SQLException e) {
            logger.error("Failed to load university.", e);
            throw new DAOException("Failed to load university.", e);
        }
        return university;
    }

    @Override
    public boolean update(University university) throws DAOException {
        logger.info("Updating university: {}.", university);
        try(Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(universityQueries.getString("university.update"))) {

            statement.setString(1, university.getName());
            statement.setString(2, university.getCity());
            statement.setString(3, university.getAddress());
            statement.setLong(4, university.getId());

            return statement.executeUpdate() != 0;
        } catch (SQLException e) {
            logger.error("Failed to update university.", e);
            throw new DAOException("Failed to update university.", e);
        }
    }

    @Override
    public boolean delete(long id) throws DAOException {
        logger.info("Deleting university by id: {}.", id);
        return daoHelper.delete(universityQueries.getString("university.delete"), "Failed to delete university.", id);
    }

    @Override
    public PaginationDto<University> getWithCountByCity(String city, long offset, long count) throws DAOException {
        logger.info("Getting universitites by city: {}, offset: {}, count: {}.", city, offset, count);
        try(Connection connection = dataSource.getConnection();
            PreparedStatement getUniversitiesSt = connection.prepareStatement(universityQueries.getString("university.get.all.by_city"));
            PreparedStatement getTotalCountSt = connection.prepareStatement(universityQueries.getString("university.count.by_city"))) {

            getUniversitiesSt.setString(1, city);
            getUniversitiesSt.setLong(2, count);
            getUniversitiesSt.setLong(3, offset);
            getTotalCountSt.setString(1, city);

            List<University> universities = getByStatement(getUniversitiesSt);
            long totalCount = daoHelper.getCount(getTotalCountSt);

            return new PaginationDto<>(universities, totalCount);
        } catch (SQLException e) {
            logger.error("Failed to load universities.", e);
            throw new DAOException("Failed to load universities.", e);
        }
    }

    @Override
    public PaginationDto<University> getWithCount(long offset, long count) throws DAOException {
        logger.info("Getting universitites: offset: {}, count: {}.", offset, count);
        try(Connection connection = dataSource.getConnection();
            PreparedStatement getUniversitiesSt = connection.prepareStatement(universityQueries.getString("university.get.all"));
            PreparedStatement getTotalCountSt = connection.prepareStatement(universityQueries.getString("university.count"))) {

            getUniversitiesSt.setLong(1, count);
            getUniversitiesSt.setLong(2, offset);

            List<University> universities = getByStatement(getUniversitiesSt);
            long totalCount = daoHelper.getCount(getTotalCountSt);

            return new PaginationDto<>(universities, totalCount);
        } catch (SQLException e) {
            logger.error("Failed to load universities.", e);
            throw new DAOException("Failed to load universities.", e);
        }
    }

    private List<University> getByStatement(PreparedStatement statement) throws SQLException {
        List<University> universities = new ArrayList<>();

        try(ResultSet rs = statement.executeQuery()) {
            while(rs.next()) {
                long id = rs.getLong("id");
                String name = rs.getString("name");
                String city = rs.getString("city");
                String address = rs.getString("address");

                universities.add(new University(id, name, city, address));
            }
        }
        return universities;
    }
}