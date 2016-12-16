package com.jayton.admissionoffice.dao.jdbc;

import com.jayton.admissionoffice.dao.UniversityDao;
import com.jayton.admissionoffice.dao.exception.DAOException;
import com.jayton.admissionoffice.dao.jdbc.pool.PoolHelper;
import com.jayton.admissionoffice.dao.jdbc.util.DaoHelper;
import com.jayton.admissionoffice.model.university.University;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class JdbcUniversityDaoImpl implements UniversityDao {

    private final ResourceBundle universityQueries = ResourceBundle.getBundle("db.queries.universityQueries");

    JdbcUniversityDaoImpl() {
    }

    @Override
    public University add(University university) throws DAOException {
        PreparedStatement statement = null;
        Connection connection = null;

        try {
            connection = PoolHelper.getInstance().getDataSource().getPool().getConnection();
            statement = connection.prepareStatement(universityQueries.getString("university.add"),
                    Statement.RETURN_GENERATED_KEYS);

            statement.setString(1, university.getName());
            statement.setString(2, university.getCity());
            statement.setString(3, university.getAddress());

            int affectedRows = statement.executeUpdate();
            if(affectedRows == 0) {
                throw new DAOException("Failed to save university.");
            }

            try (ResultSet rs = statement.getGeneratedKeys()) {
                if (rs.next()) {
                    return new University(rs.getLong(1), university.getName(), university.getCity(),
                            university.getAddress());
                } else {
                    throw new DAOException("Failed to get university`s id.");
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Failed to save university.", e);
        } finally {
            DaoHelper.closeResources(connection, statement);
        }
    }

    @Override
    public University get(long id) throws DAOException {
        PreparedStatement statement = null;
        Connection connection = null;

        try {
            connection = PoolHelper.getInstance().getDataSource().getPool().getConnection();
            statement = connection.prepareStatement(universityQueries.getString("university.get"));
            statement.setLong(1, id);

            try(ResultSet rs = statement.executeQuery()) {
                if(!rs.next()) {
                    return null;
                }

                String name = rs.getString("name");
                String city = rs.getString("city");
                String address = rs.getString("address");

                return new University(id, name, city, address);
            }
        } catch (SQLException e) {
            throw new DAOException("Failed to load university.", e);
        } finally {
            DaoHelper.closeResources(connection, statement);
        }
    }

    @Override
    public University update(University university) throws DAOException {
        PreparedStatement statement = null;
        Connection connection = null;

        try {
            connection = PoolHelper.getInstance().getDataSource().getPool().getConnection();
            statement = connection.prepareStatement(universityQueries.getString("university.update"));

            statement.setString(1, university.getName());
            statement.setString(2, university.getCity());
            statement.setString(3, university.getAddress());

            statement.setLong(4, university.getId());

            int affectedRows = statement.executeUpdate();
            if(affectedRows == 0) {
                throw new DAOException("Failed to update university.");
            }

            return university;

        } catch (SQLException e) {
            throw new DAOException("Failed to update university.", e);
        } finally {
            DaoHelper.closeResources(connection, statement);
        }
    }

    @Override
    public void delete(long id) throws DAOException {
        DaoHelper.delete(universityQueries.getString("university.delete"), "Failed to delete university.", id);
    }

    @Override
    public List<University> getByCity(String city, long offset, long count) throws DAOException {
        PreparedStatement statement = null;
        Connection connection = null;

        try {
            connection = PoolHelper.getInstance().getDataSource().getPool().getConnection();
            statement = connection.prepareStatement(universityQueries.getString("university.get.all.by_city"));
            statement.setString(1, city);
            statement.setLong(2, count);
            statement.setLong(3, offset);

            return getByStatement(statement);

        } catch (SQLException e) {
            throw new DAOException("Failed to load universities.", e);
        } finally {
            DaoHelper.closeResources(connection, statement);
        }
    }

    @Override
    public List<University> getAll(long offset, long count) throws DAOException {
        PreparedStatement statement = null;
        Connection connection = null;

        try {
            connection = PoolHelper.getInstance().getDataSource().getPool().getConnection();
            statement = connection.prepareStatement(universityQueries.getString("university.get.all"));
            statement.setLong(1, count);
            statement.setLong(2, offset);

            return getByStatement(statement);

        } catch (SQLException e) {
            throw new DAOException("Failed to load universities.", e);
        } finally {
            DaoHelper.closeResources(connection, statement);
        }
    }

    @Override
    public long getTotalCount() throws DAOException {
        return DaoHelper.getCount(universityQueries.getString("university.count"),
                "Failed to get count of universities.");
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