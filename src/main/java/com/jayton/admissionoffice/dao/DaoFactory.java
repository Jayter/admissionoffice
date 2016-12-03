package com.jayton.admissionoffice.dao;

/**
 * Created by Jayton on 03.12.2016.
 */
public interface DaoFactory {
    ApplicationDao getApplicationDao();
    DirectionDao getDirectionDao();
    FacultyDao getFacultyDao();
    UniversityDao getUniversityDao();
    SubjectDao getSubjectDao();
    UserDao getUserDao();
}
