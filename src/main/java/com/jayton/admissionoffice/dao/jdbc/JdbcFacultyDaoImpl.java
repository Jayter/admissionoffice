package com.jayton.admissionoffice.dao.jdbc;

import com.jayton.admissionoffice.dao.FacultyDao;
import com.jayton.admissionoffice.dao.exception.DAOException;
import com.jayton.admissionoffice.dao.jdbc.util.DaoHelper;
import com.jayton.admissionoffice.model.to.PaginationDTO;
import com.jayton.admissionoffice.model.university.Faculty;
import com.jayton.admissionoffice.util.di.Injected;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class JdbcFacultyDaoImpl implements FacultyDao {

    private final ResourceBundle facultyQueries = ResourceBundle.getBundle("db.queries.facultyQueries");
    private final Logger logger = LoggerFactory.getLogger(JdbcFacultyDaoImpl.class);

    @Injected
    private DataSource dataSource;
    @Injected
    private DaoHelper daoHelper;

    public JdbcFacultyDaoImpl() {
    }

    @Override
    public long add(Faculty faculty) throws DAOException {
        try(Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(facultyQueries.getString("faculty.add"),
                    Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, faculty.getName());
            statement.setString(2, faculty.getOfficePhone());
            statement.setString(3, faculty.getOfficeEmail());
            statement.setString(4, faculty.getOfficeAddress());
            statement.setLong(5, faculty.getUniversityId());

            statement.executeUpdate();
            try (ResultSet rs = statement.getGeneratedKeys()) {
                rs.next();
                return rs.getLong(1);
            }
        } catch (SQLException e) {
            logger.error("Failed to save faculty.", e);
            throw new DAOException("Failed to save faculty.", e);
        }
    }

    @Override
    public Faculty get(long id) throws DAOException {
        Faculty faculty = null;
        try(Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(facultyQueries.getString("faculty.get"))) {

            statement.setLong(1, id);

            try(ResultSet rs = statement.executeQuery()) {
                if(rs.next()) {
                    String name = rs.getString("name");
                    String phone = rs.getString("office_phone");
                    String email = rs.getString("office_email");
                    String address = rs.getString("address");
                    long universityId = rs.getLong("university_id");

                    faculty = new Faculty(id, name, phone, email, address, universityId);
                }
            }
        } catch (SQLException e) {
            logger.error("Failed to load faculty.", e);
            throw new DAOException("Failed to load faculty.", e);
        }
        return faculty;
    }

    @Override
    public boolean update(Faculty faculty) throws DAOException {
        try(Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(facultyQueries.getString("faculty.update"))) {

            statement.setString(1, faculty.getName());
            statement.setString(2, faculty.getOfficePhone());
            statement.setString(3, faculty.getOfficeEmail());
            statement.setString(4, faculty.getOfficeAddress());
            statement.setLong(5, faculty.getId());

            return statement.executeUpdate() != 0;
        } catch (SQLException e) {
            logger.error("Failed to update faculty.", e);
            throw new DAOException("Failed to update faculty.", e);
        }
    }

    @Override
    public boolean delete(long id) throws DAOException {
        return daoHelper.delete(facultyQueries.getString("faculty.delete"), "Failed to delete faculty.", id);
    }

    @Override
    public PaginationDTO<Faculty> getWithCountByUniversity(long universityId, long offset, long count) throws DAOException {
        try(Connection connection = dataSource.getConnection();
            PreparedStatement getFacultiesSt = connection.prepareStatement(facultyQueries.getString("faculty.get.all.by_university"));
            PreparedStatement getTotalCountSt = connection.prepareStatement(facultyQueries.getString("faculty.count"));) {

            getFacultiesSt.setLong(1, universityId);
            getFacultiesSt.setLong(2, count);
            getFacultiesSt.setLong(3, offset);

            List<Faculty> faculties =  getByStatement(getFacultiesSt);
            long totalCount = daoHelper.getCount(getTotalCountSt, universityId);

            return new PaginationDTO<>(faculties, totalCount);
        } catch (SQLException e) {
            logger.error("Failed to load faculties.", e);
            throw new DAOException("Failed to load faculties.", e);
        }
    }

    @Override
    public List<Faculty> getAll() throws DAOException {
        try(Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(facultyQueries.getString("faculty.get.all"))) {

            return getByStatement(statement);
        } catch (SQLException e) {
            logger.error("Failed to load faculties.", e);
            throw new DAOException("Failed to load faculties.", e);
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