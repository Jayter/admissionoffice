package com.jayton.admissionoffice.dao.jdbc;

import com.jayton.admissionoffice.dao.FacultyDao;
import com.jayton.admissionoffice.dao.exception.DAOException;
import com.jayton.admissionoffice.dao.jdbc.pool.PoolHelper;
import com.jayton.admissionoffice.model.university.Faculty;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by Jayton on 27.11.2016.
 */
public class JdbcFacultyDaoImpl implements FacultyDao {

    private final ResourceBundle facultyQueries = ResourceBundle.getBundle("db.queries.facultyQueries");

    JdbcFacultyDaoImpl() {
    }

    @Override
    public Faculty get(Long id) throws DAOException {
        PreparedStatement statement = null;
        Connection connection = null;

        try {
            connection = PoolHelper.getInstance().getDataSource().getPool().getConnection();
            statement = connection.prepareStatement(facultyQueries.getString("faculty.get"));
            statement.setLong(1, id);

            try(ResultSet rs = statement.executeQuery()) {
                if(!rs.next()) {
                    return null;
                }

                String name = rs.getString("name");
                String phone = rs.getString("office_phone");
                String email = rs.getString("office_email");
                String address = rs.getString("address");
                Long universityId = rs.getLong("university_id");

                return new Faculty(id, name, phone, email, address, universityId);
            }
        } catch (SQLException e) {
            throw new DAOException("Failed to load faculty.", e);
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
    public List<Faculty> getByUniversity(Long universityId) throws DAOException {
        PreparedStatement statement = null;
        Connection connection = null;

        try {
            connection = PoolHelper.getInstance().getDataSource().getPool().getConnection();
            statement = connection.prepareStatement(facultyQueries.getString("faculty.get.all.by_university"));
            statement.setLong(1, universityId);

            List<Faculty> faculties = new ArrayList<>();

            try(ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    Long id = rs.getLong("id");
                    String name = rs.getString("name");
                    String phone = rs.getString("office_phone");
                    String email = rs.getString("office_email");
                    String address = rs.getString("address");

                    faculties.add(new Faculty(id, name, phone, email, address, universityId));
                }
            }
            return faculties;

        } catch (SQLException e) {
            throw new DAOException("Failed to load faculties.", e);
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
    public List<Faculty> getAll() throws DAOException {
        PreparedStatement statement = null;
        Connection connection = null;

        try {
            connection = PoolHelper.getInstance().getDataSource().getPool().getConnection();
            statement = connection.prepareStatement(facultyQueries.getString("faculty.get.all"));

            List<Faculty> faculties = new ArrayList<>();

            try(ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    Long id = rs.getLong("id");
                    String name = rs.getString("name");
                    String phone = rs.getString("office_phone");
                    String email = rs.getString("office_email");
                    String address = rs.getString("address");
                    Long universityId = rs.getLong("university_id");

                    faculties.add(new Faculty(id, name, phone, email, address, universityId));
                }
            }
            return faculties;

        } catch (SQLException e) {
            throw new DAOException("Failed to load faculties.", e);
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
    public Long add(Faculty faculty) throws DAOException {
        PreparedStatement statement = null;
        Connection connection = null;

        try {
            connection = PoolHelper.getInstance().getDataSource().getPool().getConnection();
            statement = connection.prepareStatement(facultyQueries.getString("faculty.add"), Statement.RETURN_GENERATED_KEYS);

            statement.setString(1, faculty.getName());
            statement.setString(2, faculty.getOfficePhone());
            statement.setString(3, faculty.getOfficeEmail());
            statement.setString(4, faculty.getOfficeAddress());
            statement.setLong(5, faculty.getUniversityId());

            int affectedRows = statement.executeUpdate();
            if(affectedRows == 0) {
                throw new DAOException("Failed to save faculty.");
            }

            try (ResultSet rs = statement.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getLong(1);
                } else {
                    throw new DAOException("Failed to get faculty`s id.");
                }
            }

        } catch (SQLException e) {
            throw new DAOException("Failed to save faculty.", e);
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
    public void update(Faculty faculty) throws DAOException {
        PreparedStatement statement = null;
        Connection connection = null;

        try {
            connection = PoolHelper.getInstance().getDataSource().getPool().getConnection();
            statement = connection.prepareStatement(facultyQueries.getString("faculty.update"));

            statement.setString(1, faculty.getName());
            statement.setString(2, faculty.getOfficePhone());
            statement.setString(3, faculty.getOfficeEmail());
            statement.setString(4, faculty.getOfficeAddress());

            statement.setLong(5, faculty.getId());

            int affectedRows = statement.executeUpdate();
            if(affectedRows == 0) {
                throw new DAOException("Failed to update faculty.");
            }

        } catch (SQLException | NullPointerException e) {
            throw new DAOException("Failed to update faculty.", e);
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
            statement = connection.prepareStatement(facultyQueries.getString("faculty.delete"));

            statement.setLong(1, id);

            int affectedRows = statement.executeUpdate();
            return affectedRows != 0;

        } catch (SQLException e) {
            throw new DAOException("Failed to delete faculty.", e);
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
