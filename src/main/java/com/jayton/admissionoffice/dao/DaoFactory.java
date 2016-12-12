package com.jayton.admissionoffice.dao;

public interface DaoFactory {
    ApplicationDao getApplicationDao();
    DirectionDao getDirectionDao();
    FacultyDao getFacultyDao();
    UniversityDao getUniversityDao();
    UserDao getUserDao();
    UtilDao getUtilDao();
}