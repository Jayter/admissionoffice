package com.jayton.admissionoffice.dao.jdbc;

import com.jayton.admissionoffice.dao.*;

/**
 * Created by Jayton on 03.12.2016.
 */
public class PostgresDaoFactory implements DaoFactory {
    private final JdbcApplicationDaoImpl jdbcApplicationDao = new JdbcApplicationDaoImpl();
    private final JdbcDirectionDaoImpl jdbcDirectionDao = new JdbcDirectionDaoImpl();
    private final JdbcFacultyDaoImpl jdbcFacultyDao = new JdbcFacultyDaoImpl();
    private final JdbcUniversityDaoImpl jdbcUniversityDao = new JdbcUniversityDaoImpl();
    private final JdbcSubjectDaoImpl jdbcSubjectDao = new JdbcSubjectDaoImpl();
    private final JdbcUserDaoImpl jdbcUserDao = new JdbcUserDaoImpl();
    private final JdbcUtilDaoImpl jdbcUtilDao = new JdbcUtilDaoImpl();

    @Override
    public ApplicationDao getApplicationDao() {
        return jdbcApplicationDao;
    }

    @Override
    public DirectionDao getDirectionDao() {
        return jdbcDirectionDao;
    }

    @Override
    public FacultyDao getFacultyDao() {
        return jdbcFacultyDao;
    }

    @Override
    public UniversityDao getUniversityDao() {
        return jdbcUniversityDao;
    }

    @Override
    public SubjectDao getSubjectDao() {
        return jdbcSubjectDao;
    }

    @Override
    public UserDao getUserDao() {
        return jdbcUserDao;
    }

    @Override
    public UtilDao getUtilDao() {
        return jdbcUtilDao;
    }
}
