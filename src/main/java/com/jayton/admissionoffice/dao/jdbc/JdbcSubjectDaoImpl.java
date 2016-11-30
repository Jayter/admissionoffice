package com.jayton.admissionoffice.dao.jdbc;

import com.jayton.admissionoffice.dao.SubjectDao;
import com.jayton.admissionoffice.dao.exception.DAOException;
import com.jayton.admissionoffice.dao.jdbc.pool.PoolHelper;
import com.jayton.admissionoffice.model.Subject;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jayton on 26.11.2016.
 */
public class JdbcSubjectDaoImpl implements SubjectDao {

    public static final String SQL_GET = "SELECT * FROM subjects WHERE id=?";
    public static final String SQL_GET_BY_NAME = "SELECT * FROM subjects WHERE name=?";
    public static final String SQL_GET_ALL = "SELECT * FROM subjects";
    public static final String SQL_ADD = "INSERT INTO subjects (name) VALUES (?)";
    public static final String SQL_UPDATE = "UPDATE subjects SET name=? WHERE id=?";
    public static final String SQL_DELETE = "DELETE FROM subjects WHERE id=?";

    private static JdbcSubjectDaoImpl instance;

    private JdbcSubjectDaoImpl() {
    }

    public static synchronized JdbcSubjectDaoImpl getInstance() {
        if(instance == null) {
            instance = new JdbcSubjectDaoImpl();
        }

        return instance;
    }

    @Override
    public Subject get(Long id) throws DAOException {
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

                return new Subject(id, name);
            }
        } catch (SQLException e) {
            throw new DAOException("Failed to load subject.", e);
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
    public List<Subject> getAll() throws DAOException {
        PreparedStatement statement = null;
        Connection connection = null;

        try {
            connection = PoolHelper.getInstance().getDataSource().getPool().getConnection();
            statement = connection.prepareStatement(SQL_GET_ALL);

            List<Subject> subjects = new ArrayList<>();

            try(ResultSet rs = statement.executeQuery()) {
                while(rs.next()) {
                    Long id = rs.getLong("id");
                    String name = rs.getString("name");

                    subjects.add(new Subject(id, name));
                }
            }
            return subjects;

        } catch (SQLException e) {
            throw new DAOException("Failed to load subjects.", e);
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
    public Subject getByName(String name) throws DAOException {
        PreparedStatement statement = null;
        Connection connection = null;

        try {
            connection = PoolHelper.getInstance().getDataSource().getPool().getConnection();
            statement = connection.prepareStatement(SQL_GET_BY_NAME);
            statement.setString(1, name);

            try(ResultSet rs = statement.executeQuery()) {
                if(!rs.next()) {
                    return null;
                }

                Long id = rs.getLong("id");
                return new Subject(id, name);
            }
        } catch (SQLException e) {
            throw new DAOException("Failed to load subject.", e);
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
    public Long add(Subject subject) throws DAOException {
        PreparedStatement statement = null;
        Connection connection = null;

        try {
            connection = PoolHelper.getInstance().getDataSource().getPool().getConnection();
            statement = connection.prepareStatement(SQL_ADD, Statement.RETURN_GENERATED_KEYS);

            statement.setString(1, subject.getName());

            int affectedRows = statement.executeUpdate();
            if(affectedRows == 0) {
                throw new DAOException("Failed to save subject.");
            }

            try (ResultSet rs = statement.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getLong(1);
                } else {
                    throw new DAOException("Failed to get subject`s id.");
                }
            }

        } catch (SQLException e) {
            throw new DAOException("Failed to save subject.", e);
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
    public void update(Subject subject) throws DAOException {
        PreparedStatement statement = null;
        Connection connection = null;

        try {
            connection = PoolHelper.getInstance().getDataSource().getPool().getConnection();
            statement = connection.prepareStatement(SQL_UPDATE);

            statement.setString(1, subject.getName());
            statement.setLong(2, subject.getId());

            int affectedRows = statement.executeUpdate();
            if(affectedRows == 0) {
                throw new DAOException("Failed to update subject.");
            }

        } catch (SQLException | NullPointerException e) {
            throw new DAOException("Failed to update subject.", e);
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
            throw new DAOException("Failed to delete subject.", e);
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
