package com.jayton.admissionoffice.dao.jdbc;

import com.jayton.admissionoffice.dao.FacultyDao;
import com.jayton.admissionoffice.dao.exception.DAOException;
import com.jayton.admissionoffice.dao.jdbc.pool.PoolHelper;
import com.jayton.admissionoffice.model.university.Faculty;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jayton on 27.11.2016.
 */
public class JdbcFacultyDaoImpl implements FacultyDao {

    public static final String SQL_GET = "SELECT * FROM faculties WHERE id=?";
    public static final String SQL_GET_BY_UNIVERSITY = "SELECT * FROM faculties WHERE university_id=?";
    public static final String SQL_GET_ALL = "SELECT * FROM faculties";
    public static final String SQL_ADD = "INSERT INTO faculties (name, university_id, office_phone, office_email, address)" +
            " VALUES (?, ?, ?, ?, ?)";
    public static final String SQL_UPDATE = "UPDATE faculties SET name=?, office_phone=?, office_email=?, address=? WHERE id=?";
    public static final String SQL_DELETE = "DELETE FROM faculties WHERE id=?";

    private static JdbcFacultyDaoImpl instance;

    private JdbcFacultyDaoImpl() {
    }

    public static synchronized JdbcFacultyDaoImpl getInstance() {
        if(instance == null) {
            instance = new JdbcFacultyDaoImpl();
        }

        return instance;
    }


    @Override
    public Faculty get(Long id) throws DAOException {
        PreparedStatement statement = null;
        Connection connection = null;

        try {
            connection = PoolHelper.getDataSource().getPool().getConnection();
            statement = connection.prepareStatement(SQL_GET);
            statement.setLong(1, id);


            Faculty faculty = new Faculty();
            try(ResultSet rs = statement.executeQuery()) {
                if(!rs.next()) {
                    return null;
                }

                faculty.setId(rs.getLong("id"));
                faculty.setName(rs.getString("name"));
                faculty.setUniversityId(rs.getLong("university_id"));
                faculty.setOfficePhone(rs.getString("office_phone"));
                faculty.setOfficeEmail(rs.getString("office_email"));
                faculty.setOfficeAddress(rs.getString("address"));
            }

            return faculty;
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
        List<Faculty> faculties = new ArrayList<>();
        PreparedStatement statement = null;
        Connection connection = null;

        try {
            connection = PoolHelper.getDataSource().getPool().getConnection();
            statement = connection.prepareStatement(SQL_GET_BY_UNIVERSITY);
            statement.setLong(1, universityId);

            Faculty faculty = null;

            try(ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    faculty = new Faculty();

                    faculty.setId(rs.getLong("id"));
                    faculty.setName(rs.getString("name"));
                    faculty.setUniversityId(rs.getLong("university_id"));
                    faculty.setOfficePhone(rs.getString("office_phone"));
                    faculty.setOfficeEmail(rs.getString("office_email"));
                    faculty.setOfficeAddress(rs.getString("address"));

                    faculties.add(faculty);
                }
            }

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
        return faculties;
    }

    @Override
    public List<Faculty> getAll() throws DAOException {
        List<Faculty> faculties = new ArrayList<>();
        PreparedStatement statement = null;
        Connection connection = null;

        try {
            connection = PoolHelper.getDataSource().getPool().getConnection();
            statement = connection.prepareStatement(SQL_GET_ALL);

            Faculty faculty = null;

            try(ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    faculty = new Faculty();

                    faculty.setId(rs.getLong("id"));
                    faculty.setName(rs.getString("name"));
                    faculty.setUniversityId(rs.getLong("university_id"));
                    faculty.setOfficePhone(rs.getString("office_phone"));
                    faculty.setOfficeEmail(rs.getString("office_email"));
                    faculty.setOfficeAddress(rs.getString("address"));

                    faculties.add(faculty);
                }
            }

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
        return faculties;
    }

    @Override
    public void add(Faculty faculty) throws DAOException {
        PreparedStatement statement = null;
        Connection connection = null;

        try {
            connection = PoolHelper.getDataSource().getPool().getConnection();
            statement = connection.prepareStatement(SQL_ADD, Statement.RETURN_GENERATED_KEYS);

            statement.setString(1, faculty.getName());
            statement.setLong(2, faculty.getUniversityId());
            statement.setString(3, faculty.getOfficePhone());
            statement.setString(4, faculty.getOfficeEmail());
            statement.setString(5, faculty.getOfficeAddress());

            int affectedRows = statement.executeUpdate();
            if(affectedRows == 0) {
                throw new DAOException("Failed to save faculty.");
            }

            try (ResultSet rs = statement.getGeneratedKeys()) {
                if (rs.next()) {
                    faculty.setId(rs.getLong(1));
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
            connection = PoolHelper.getDataSource().getPool().getConnection();
            statement = connection.prepareStatement(SQL_UPDATE);

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
    public void delete(Faculty entity) throws DAOException {
        delete(entity.getId());
    }

    @Override
    public void delete(Long id) throws DAOException {
        PreparedStatement statement = null;
        Connection connection = null;

        try {
            connection = PoolHelper.getDataSource().getPool().getConnection();
            statement = connection.prepareStatement(SQL_DELETE);

            statement.setLong(1, id);

            int affectedRows = statement.executeUpdate();
            if(affectedRows == 0) {
                throw new DAOException("Failed to delete faculty.");
            }

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
