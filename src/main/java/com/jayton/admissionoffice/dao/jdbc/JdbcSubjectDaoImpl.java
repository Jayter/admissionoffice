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
            connection = PoolHelper.getDataSource().getPool().getConnection();
            statement = connection.prepareStatement(SQL_GET);
            statement.setLong(1, id);

            Subject subject = new Subject();

            try(ResultSet rs = statement.executeQuery()) {
                if(!rs.next()) {
                    return null;
                }

                subject.setId(rs.getLong("id"));
                subject.setName(rs.getString("name"));
            }

            return subject;
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
        List<Subject> subjects = new ArrayList<>();
        PreparedStatement statement = null;
        Connection connection = null;

        try {
            connection = PoolHelper.getDataSource().getPool().getConnection();
            statement = connection.prepareStatement(SQL_GET_ALL);

            Subject subject;

            try(ResultSet rs = statement.executeQuery()) {
                while(rs.next()) {
                    subject = new Subject();

                    subject.setId(rs.getLong("id"));
                    subject.setName(rs.getString("name"));

                    subjects.add(subject);
                }
            }

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
        return subjects;
    }

    @Override
    public Subject getByName(String name) throws DAOException {
        PreparedStatement statement = null;
        Connection connection = null;

        try {
            connection = PoolHelper.getDataSource().getPool().getConnection();
            statement = connection.prepareStatement(SQL_GET_BY_NAME);
            statement.setString(1, name);

            Subject subject = new Subject();

            try(ResultSet rs = statement.executeQuery()) {
                if(!rs.next()) {
                    return null;
                }

                subject.setId(rs.getLong("id"));
                subject.setName(rs.getString("name"));
            }

            return subject;
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
    public void add(Subject subject) throws DAOException {
        PreparedStatement statement = null;
        Connection connection = null;

        try {
            connection = PoolHelper.getDataSource().getPool().getConnection();
            statement = connection.prepareStatement(SQL_ADD);

            statement.setString(1, subject.getName());

            int row = statement.executeUpdate();
            if(row == 0) {
                throw new DAOException("Failed to save subject.");
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
            connection = PoolHelper.getDataSource().getPool().getConnection();
            statement = connection.prepareStatement(SQL_UPDATE);

            statement.setString(1, subject.getName());
            statement.setLong(2, subject.getId());

            int row = statement.executeUpdate();
            if(row == 0) {
                throw new DAOException("Failed to update subject.");
            }

        } catch (SQLException e) {
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
    public void delete(Subject subject) throws DAOException {
        delete(subject.getId());
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
                throw new DAOException("Failed to delete subject.");
            }

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
