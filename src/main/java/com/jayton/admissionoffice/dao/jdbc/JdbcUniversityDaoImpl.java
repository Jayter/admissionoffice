package com.jayton.admissionoffice.dao.jdbc;

import com.jayton.admissionoffice.dao.UniversityDao;
import com.jayton.admissionoffice.dao.exception.DAOException;
import com.jayton.admissionoffice.dao.jdbc.util.DaoHelper;
import com.jayton.admissionoffice.model.to.PaginationDTO;
import com.jayton.admissionoffice.model.university.University;
import com.jayton.admissionoffice.util.di.Injected;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class JdbcUniversityDaoImpl implements UniversityDao {

    private final ResourceBundle universityQueries = ResourceBundle.getBundle("db.queries.universityQueries");

    @Injected
    private DataSource dataSource;
    @Injected
    private DaoHelper daoHelper;

    public JdbcUniversityDaoImpl() {
    }

    @Override
    public long add(University university) throws DAOException {
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
            throw new DAOException("Failed to save university.", e);
        }
    }

    @Override
    public University get(long id) throws DAOException {
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
            throw new DAOException("Failed to load university.", e);
        }
        return university;
    }

    @Override
    public boolean update(University university) throws DAOException {
        try(Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(universityQueries.getString("university.update"))) {

            statement.setString(1, university.getName());
            statement.setString(2, university.getCity());
            statement.setString(3, university.getAddress());
            statement.setLong(4, university.getId());

            return statement.executeUpdate() != 0;
        } catch (SQLException e) {
            throw new DAOException("Failed to update university.", e);
        }
    }

    @Override
    public boolean delete(long id) throws DAOException {
        return daoHelper.delete(universityQueries.getString("university.delete"), "Failed to delete university.", id);
    }

    @Override
    public PaginationDTO<University> getWithCountByCity(String city, long offset, long count) throws DAOException {
        try(Connection connection = dataSource.getConnection();
            PreparedStatement getUniversitiesSt = connection.prepareStatement(universityQueries.getString("university.get.all.by_city"));
            PreparedStatement getTotalCountSt = connection.prepareStatement(universityQueries.getString("university.count.by_city"))) {

            getUniversitiesSt.setString(1, city);
            getUniversitiesSt.setLong(2, count);
            getUniversitiesSt.setLong(3, offset);
            getTotalCountSt.setString(1, city);

            List<University> universities = getByStatement(getUniversitiesSt);
            long totalCount = daoHelper.getCount(getTotalCountSt);

            return new PaginationDTO<>(universities, totalCount);
        } catch (SQLException e) {
            throw new DAOException("Failed to load universities.", e);
        }
    }

    @Override
    public PaginationDTO<University> getWithCount(long offset, long count) throws DAOException {
        try(Connection connection = dataSource.getConnection();
            PreparedStatement getUniversitiesSt = connection.prepareStatement(universityQueries.getString("university.get.all"));
            PreparedStatement getTotalCountSt = connection.prepareStatement(universityQueries.getString("university.count"))) {

            getUniversitiesSt.setLong(1, count);
            getUniversitiesSt.setLong(2, offset);

            List<University> universities = getByStatement(getUniversitiesSt);
            long totalCount = daoHelper.getCount(getTotalCountSt);

            return new PaginationDTO<>(universities, totalCount);
        } catch (SQLException e) {
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