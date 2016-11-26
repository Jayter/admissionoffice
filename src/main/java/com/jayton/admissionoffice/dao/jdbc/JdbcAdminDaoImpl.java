package com.jayton.admissionoffice.dao.jdbc;

import com.jayton.admissionoffice.dao.AdminDao;
import com.jayton.admissionoffice.dao.exception.DAOException;
import com.jayton.admissionoffice.dao.jdbc.pool.PoolHelper;
import com.jayton.admissionoffice.model.user.Admin;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jayton on 25.11.2016.
 */
public class JdbcAdminDaoImpl implements AdminDao {

    public static final String SQL_GET = "SELECT * FROM admins WHERE id=?";
    public static final String SQL_GET_ALL = "SELECT * FROM admins";
    public static final String SQL_ADD = "INSERT INTO admins (name, second_name, address, email, password," +
            " phone_number, birth_date) values (?, ?, ?, ?, ?, ?, ?)";
    public static final String SQL_UPDATE = "UPDATE admins SET name=?, second_name=?, address=?, email=?," +
            " password=?, phone_number=?, birth_date=? WHERE id=?";
    public static final String SQL_DELETE = "DELETE FROM admins WHERE id=?";

    private static JdbcAdminDaoImpl instance;

    private JdbcAdminDaoImpl() {
    }

    public static synchronized JdbcAdminDaoImpl getInstance() {
        if(instance == null) {
            instance = new JdbcAdminDaoImpl();
        }
        return instance;
    }

    @Override
    public Admin get(Long id) throws DAOException {
        PreparedStatement statement = null;
        Connection connection = null;

        try {
            connection = PoolHelper.getDataSource().getPool().getConnection();
            statement = connection.prepareStatement(SQL_GET);
            statement.setLong(1, id);

            Admin admin = new Admin();

            try(ResultSet rs = statement.executeQuery()) {
                if(!rs.next()) {
                    return null;
                }

                admin.setId(rs.getLong("id"));
                admin.setName(rs.getString("name"));
                admin.setSecondName(rs.getString("second_name"));
                admin.setAddress(rs.getString("address"));
                admin.setEmail(rs.getString("email"));
                admin.setPassword(rs.getString("password"));
                admin.setPhoneNumber(rs.getString("phone_number"));
                admin.setBirthDate(rs.getDate("birth_date").toLocalDate());
            }

            return admin;
        } catch (SQLException e) {
            throw new DAOException("Failed to load admin.", e);
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

    public List<Admin> getAll() throws DAOException {
        List<Admin> admins = new ArrayList<>();
        PreparedStatement statement = null;
        Connection connection = null;

        try {
            connection = PoolHelper.getDataSource().getPool().getConnection();
            statement = connection.prepareStatement(SQL_GET_ALL);

            Admin admin;

            try(ResultSet rs = statement.executeQuery()) {
                while(rs.next()) {
                    admin = new Admin();

                    admin.setId(rs.getLong("id"));
                    admin.setName(rs.getString("name"));
                    admin.setSecondName(rs.getString("second_name"));
                    admin.setAddress(rs.getString("address"));
                    admin.setEmail(rs.getString("email"));
                    admin.setPassword(rs.getString("password"));
                    admin.setPhoneNumber(rs.getString("phone_number"));
                    admin.setBirthDate(rs.getDate("birth_date").toLocalDate());

                    admins.add(admin);
                }
            }

        } catch (SQLException e) {
            throw new DAOException("Failed to load admins.", e);
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
        return admins;
    }

    @Override
    public void add(Admin admin) throws DAOException {
        PreparedStatement statement = null;
        Connection connection = null;

        try {
            connection = PoolHelper.getDataSource().getPool().getConnection();
            statement = connection.prepareStatement(SQL_ADD);

            statement.setString(1, admin.getName());
            statement.setString(2, admin.getSecondName());
            statement.setString(3, admin.getAddress());
            statement.setString(4, admin.getEmail());
            statement.setString(5, admin.getPassword());
            statement.setString(6, admin.getPhoneNumber());
            statement.setDate(7, Date.valueOf(admin.getBirthDate()));

            int row = statement.executeUpdate();
            if(row == 0) {
                throw new DAOException("Failed to save admin.");
            }

        } catch (SQLException e) {
            throw new DAOException("Failed to save admin.", e);
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
    public void update(Admin admin) throws DAOException {
        PreparedStatement statement = null;
        Connection connection = null;

        try {
            connection = PoolHelper.getDataSource().getPool().getConnection();
            statement = connection.prepareStatement(SQL_UPDATE);

            statement.setString(1, admin.getName());
            statement.setString(2, admin.getSecondName());
            statement.setString(3, admin.getAddress());
            statement.setString(4, admin.getEmail());
            statement.setString(5, admin.getPassword());
            statement.setString(6, admin.getPhoneNumber());
            statement.setDate(7, Date.valueOf(admin.getBirthDate()));

            int row = statement.executeUpdate();
            if(row == 0) {
                throw new DAOException("Failed to update admin.");
            }

        } catch (SQLException e) {
            throw new DAOException("Failed to update admin.", e);
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
    public void delete(Admin entity) throws DAOException {
        this.delete(entity.getId());
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
                throw new DAOException("Failed to delete admin.");
            }

        } catch (SQLException e) {
            throw new DAOException("Failed to delete admin.", e);
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
