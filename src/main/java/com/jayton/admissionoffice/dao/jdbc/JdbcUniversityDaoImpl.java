package com.jayton.admissionoffice.dao.jdbc;

import com.jayton.admissionoffice.dao.UniversityDao;
import com.jayton.admissionoffice.dao.exception.DAOException;
import com.jayton.admissionoffice.dao.jdbc.pool.PoolHelper;
import com.jayton.admissionoffice.model.university.University;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jayton on 26.11.2016.
 */
public class JdbcUniversityDaoImpl implements UniversityDao {

    public static final String SQL_GET = "SELECT * FROM universities WHERE id=?";
    public static final String SQL_GET_BY_CITY = "SELECT * FROM universities WHERE city=?";
    public static final String SQL_GET_ALL = "SELECT * FROM universities";
    public static final String SQL_ADD = "INSERT INTO universities (name, city, address) VALUES (?, ?, ?)";
    public static final String SQL_UPDATE = "UPDATE universities SET name=?, city=?, address=? WHERE id=?";
    public static final String SQL_DELETE = "DELETE FROM universities WHERE id=?";

    JdbcUniversityDaoImpl() {
    }

    @Override
    public University get(Long id) throws DAOException {
        PreparedStatement statement = null;
        Connection connection = null;

        try {
            connection = PoolHelper.getInstance().getDataSource().getPool().getConnection();
            statement = connection.prepareStatement(SQL_GET);
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
    public List<University> getByCity(String city) throws DAOException {
        PreparedStatement statement = null;
        Connection connection = null;

        try {
            connection = PoolHelper.getInstance().getDataSource().getPool().getConnection();
            statement = connection.prepareStatement(SQL_GET_BY_CITY);
            statement.setString(1, city);

            List<University> universities = new ArrayList<>();

            try(ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {

                    Long id = rs.getLong("id");
                    String name = rs.getString("name");
                    String address = rs.getString("address");

                    universities.add(new University(id, name, city, address));
                }
            }
            return universities;

        } catch (SQLException e) {
            throw new DAOException("Failed to load universities.", e);
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
    public List<University> getAll() throws DAOException {
        PreparedStatement statement = null;
        Connection connection = null;

        try {
            connection = PoolHelper.getInstance().getDataSource().getPool().getConnection();
            statement = connection.prepareStatement(SQL_GET_ALL);

            List<University> universities = new ArrayList<>();

            try(ResultSet rs = statement.executeQuery()) {
                while(rs.next()) {
                    Long id = rs.getLong("id");
                    String name = rs.getString("name");
                    String city = rs.getString("city");
                    String address = rs.getString("address");

                    universities.add(new University(id, name, city, address));
                }
            }
            return universities;

        } catch (SQLException e) {
            throw new DAOException("Failed to load universities.", e);
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
    public Long add(University university) throws DAOException {
        PreparedStatement statement = null;
        Connection connection = null;

        try {
            connection = PoolHelper.getInstance().getDataSource().getPool().getConnection();
            statement = connection.prepareStatement(SQL_ADD, Statement.RETURN_GENERATED_KEYS);

            statement.setString(1, university.getName());
            statement.setString(2, university.getCity());
            statement.setString(3, university.getAddress());

            int affectedRows = statement.executeUpdate();
            if(affectedRows == 0) {
                throw new DAOException("Failed to save university.");
            }

            try (ResultSet rs = statement.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getLong(1);
                } else {
                    throw new DAOException("Failed to get university id.");
                }
            }

        } catch (SQLException e) {
            throw new DAOException("Failed to save university.", e);
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
    public void update(University university) throws DAOException {
        PreparedStatement statement = null;
        Connection connection = null;

        try {
            connection = PoolHelper.getInstance().getDataSource().getPool().getConnection();
            statement = connection.prepareStatement(SQL_UPDATE);

            statement.setString(1, university.getName());
            statement.setString(2, university.getCity());
            statement.setString(3, university.getAddress());

            statement.setLong(4, university.getId());

            int affectedRows = statement.executeUpdate();
            if(affectedRows == 0) {
                throw new DAOException("Failed to update university.");
            }

        } catch (SQLException | NullPointerException e) {
            throw new DAOException("Failed to update university.", e);
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
    public boolean delete(Long id) throws DAOException {
        PreparedStatement statement = null;
        Connection connection = null;

        try {
            connection = PoolHelper.getInstance().getDataSource().getPool().getConnection();
            statement = connection.prepareStatement(SQL_DELETE);

            statement.setLong(1, id);

            int affectedRows = statement.executeUpdate();
            return affectedRows != 0;

        } catch (SQLException e) {
            throw new DAOException("Failed to delete university.", e);
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
