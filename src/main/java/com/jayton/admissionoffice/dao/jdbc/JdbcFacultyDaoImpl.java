package com.jayton.admissionoffice.dao.jdbc;

import com.jayton.admissionoffice.dao.FacultyDao;
import com.jayton.admissionoffice.dao.exception.DAOException;
import com.jayton.admissionoffice.dao.jdbc.util.DaoHelper;
import com.jayton.admissionoffice.model.to.PaginationDTO;
import com.jayton.admissionoffice.model.university.Faculty;
import com.jayton.admissionoffice.util.di.Injected;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class JdbcFacultyDaoImpl implements FacultyDao {

    private final ResourceBundle facultyQueries = ResourceBundle.getBundle("db.queries.facultyQueries");

    @Injected
    private DataSource dataSource;
    @Injected
    private DaoHelper daoHelper;

    public JdbcFacultyDaoImpl() {
    }

    @Override
    public Faculty add(Faculty faculty) throws DAOException {
        PreparedStatement statement = null;
        Connection connection = null;

        try {
            connection = dataSource.getConnection();
            statement = connection.prepareStatement(facultyQueries.getString("faculty.add"),
                    Statement.RETURN_GENERATED_KEYS);

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
                    return new Faculty(rs.getLong(1), faculty.getName(), faculty.getOfficePhone(),
                            faculty.getOfficeEmail(), faculty.getOfficeAddress(), faculty.getUniversityId());
                } else {
                    throw new DAOException("Failed to get faculty`s id.");
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Failed to save faculty.", e);
        } finally {
            daoHelper.closeResources(connection, statement);
        }
    }

    @Override
    public Faculty get(long id) throws DAOException {
        PreparedStatement statement = null;
        Connection connection = null;

        try {
            connection = dataSource.getConnection();
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
                long universityId = rs.getLong("university_id");

                return new Faculty(id, name, phone, email, address, universityId);
            }
        } catch (SQLException e) {
            throw new DAOException("Failed to load faculty.", e);
        } finally {
            daoHelper.closeResources(connection, statement);
        }
    }

    @Override
    public Faculty update(Faculty faculty) throws DAOException {
        PreparedStatement statement = null;
        Connection connection = null;

        try {
            connection = dataSource.getConnection();
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
            return faculty;

        } catch (SQLException e) {
            throw new DAOException("Failed to update faculty.", e);
        } finally {
            daoHelper.closeResources(connection, statement);
        }
    }

    @Override
    public void delete(long id) throws DAOException {
        daoHelper.delete(facultyQueries.getString("faculty.delete"), "Failed to delete faculty.", id);
    }

    @Override
    public List<Faculty> getAll() throws DAOException {
        PreparedStatement statement = null;
        Connection connection = null;

        try {
            connection = dataSource.getConnection();
            statement = connection.prepareStatement(facultyQueries.getString("faculty.get.all"));

            return getByStatement(statement);

        } catch (SQLException e) {
            throw new DAOException("Failed to load faculties.", e);
        } finally {
            daoHelper.closeResources(connection, statement);
        }
    }

    @Override
    public PaginationDTO<Faculty> getWithCountByUniversity(long universityId, long offset, long count) throws DAOException {
        Connection connection = null;
        PreparedStatement getFacultiesSt = null;
        PreparedStatement getTotalCountSt = null;

        try {
            connection = dataSource.getConnection();
            getFacultiesSt = connection.prepareStatement(facultyQueries.getString("faculty.get.all.by_university"));
            getTotalCountSt = connection.prepareStatement(facultyQueries.getString("faculty.count"));
            getFacultiesSt.setLong(1, universityId);
            getFacultiesSt.setLong(2, count);
            getFacultiesSt.setLong(3, offset);

            List<Faculty> faculties =  getByStatement(getFacultiesSt);
            long totalCount = daoHelper.getCount(getTotalCountSt, universityId);

            return new PaginationDTO<Faculty>(faculties, totalCount);
        } catch (SQLException e) {
            throw new DAOException("Failed to load faculties.", e);
        } finally {
            daoHelper.closeResources(connection, getFacultiesSt, getTotalCountSt);
        }
    }

    private List<Faculty> getByStatement(PreparedStatement statement) throws SQLException {
        List<Faculty> faculties = new ArrayList<>();

        try(ResultSet rs = statement.executeQuery()) {
            while (rs.next()) {
                long id = rs.getLong("id");
                String name = rs.getString("name");
                String phone = rs.getString("office_phone");
                String email = rs.getString("office_email");
                String address = rs.getString("address");
                long universityId = rs.getLong("university_id");

                faculties.add(new Faculty(id, name, phone, email, address, universityId));
            }
        }
        return faculties;
    }
}