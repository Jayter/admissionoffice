package com.jayton.admissionoffice.dao.jdbc;

import com.jayton.admissionoffice.dao.FacultyDao;
import com.jayton.admissionoffice.dao.exception.DAOException;
import com.jayton.admissionoffice.model.university.Faculty;

import java.util.List;

/**
 * Created by Jayton on 27.11.2016.
 */
public class JdbcFacultyDaoImpl implements FacultyDao {

    public static final String SQL_GET = "SELECT * FROM faculties WHERE id=?";
    public static final String SQL_GET_BY_NAME = "SELECT * FROM subjects WHERE name=?";
    public static final String SQL_GET_ALL = "SELECT * FROM faculties";
    public static final String SQL_ADD = "INSERT INTO faculties (name) VALUES (?)";
    public static final String SQL_UPDATE = "UPDATE faculties SET name=? WHERE id=?";
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
        return null;
    }

    @Override
    public void add(Faculty entity) throws DAOException {

    }

    @Override
    public List<Faculty> getByUniversity(Long universityID) throws DAOException {
        return null;
    }

    @Override
    public void update(Faculty entity) throws DAOException {

    }

    @Override
    public void delete(Faculty entity) throws DAOException {

    }

    @Override
    public void delete(Long id) throws DAOException {

    }
}
