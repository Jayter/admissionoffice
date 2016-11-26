package com.jayton.admissionoffice.dao.jdbc;

import com.jayton.admissionoffice.dao.UniversityDao;
import com.jayton.admissionoffice.dao.exception.DAOException;
import com.jayton.admissionoffice.dao.jdbc.pool.PoolHelper;
import com.jayton.admissionoffice.model.university.University;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jayton on 26.11.2016.
 */
public class JdbcUniversityDaoImpl implements UniversityDao {

    public static final String SQL_GET = "SELECT * FROM universities WHERE id=?";
    public static final String SQL_GET_BY_CITY = "SELECT * FROM universities WHERE city=?";
    public static final String SQL_GET_ALL = "SELECT * FROM universities";
    public static final String SQL_ADD = "INSERT INTO universities (name, city) VALUES (?, ?)";
    public static final String SQL_UPDATE = "UPDATE universities SET name=?, city=? WHERE id=?";
    public static final String SQL_DELETE = "DELETE FROM universities WHERE id=?";

    private static JdbcUniversityDaoImpl instance;

    private JdbcUniversityDaoImpl() {
    }

    public static synchronized JdbcUniversityDaoImpl getInstance() {
        if(instance == null) {
            instance = new JdbcUniversityDaoImpl();
        }

        return instance;
    }

    @Override
    public University get(Long id) throws DAOException {
        PreparedStatement statement = null;
        Connection connection = null;

        try {
            connection = PoolHelper.getDataSource().getPool().getConnection();
            statement = connection.prepareStatement(SQL_GET);
            statement.setLong(1, id);


            University university = new University();
            try(ResultSet rs = statement.executeQuery()) {
                if(!rs.next()) {
                    return null;
                }

                university.setId(rs.getLong("id"));
                university.setName(rs.getString("name"));
                university.setCity(rs.getString("city"));
            }

            return university;
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
        List<University> universities = new ArrayList<>();
        PreparedStatement statement = null;
        Connection connection = null;

        try {
            connection = PoolHelper.getDataSource().getPool().getConnection();
            statement = connection.prepareStatement(SQL_GET_BY_CITY);
            statement.setString(1, city);

            University university = null;

            try(ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    university = new University();

                    university.setId(rs.getLong("id"));
                    university.setName(rs.getString("name"));
                    university.setCity(rs.getString("city"));
                }
            }

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
        return universities;
    }

    @Override
    public List<University> getAll() throws DAOException {
        List<University> universities = new ArrayList<>();
        PreparedStatement statement = null;
        Connection connection = null;

        try {
            connection = PoolHelper.getDataSource().getPool().getConnection();
            statement = connection.prepareStatement(SQL_GET_ALL);

            University university;

            try(ResultSet rs = statement.executeQuery()) {
                while(rs.next()) {
                    university = new University();

                    university.setId(rs.getLong("id"));
                    university.setName(rs.getString("name"));
                    university.setCity(rs.getString("city"));

                    universities.add(university);
                }
            }

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
        return universities;
    }

    @Override
    public void add(University university) throws DAOException {
        PreparedStatement statement = null;
        Connection connection = null;

        try {
            connection = PoolHelper.getDataSource().getPool().getConnection();
            statement = connection.prepareStatement(SQL_ADD);

            statement.setString(1, university.getName());
            statement.setString(2, university.getCity());

            int row = statement.executeUpdate();
            if(row == 0) {
                throw new DAOException("Failed to save university.");
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
            connection = PoolHelper.getDataSource().getPool().getConnection();
            statement = connection.prepareStatement(SQL_UPDATE);

            statement.setString(1, university.getName());
            statement.setString(2, university.getCity());
            statement.setLong(3, university.getId());

            int row = statement.executeUpdate();
            if(row == 0) {
                throw new DAOException("Failed to update university.");
            }

        } catch (SQLException e) {
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
    public void delete(University university) throws DAOException {
        delete(university.getId());
    }

    @Override
    public void delete(Long id) throws DAOException {
        PreparedStatement statement = null;
        Connection connection = null;

        try {
            connection = PoolHelper.getDataSource().getPool().getConnection();
            statement = connection.prepareStatement(SQL_DELETE);

            statement.setLong(1, id);

            int row = statement.executeUpdate();
            if(row == 0) {
                throw new DAOException("Failed to delete university.");
            }

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
