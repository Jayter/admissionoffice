package com.jayton.admissionoffice.dao;

import com.jayton.admissionoffice.dao.exception.DAOException;
import com.jayton.admissionoffice.model.Subject;
import com.jayton.admissionoffice.model.to.SessionTerms;

import java.util.List;

public interface UtilDao {
    List<Subject> getAllSubjects() throws DAOException;
    SessionTerms getSessionTerms(short currentYear) throws DAOException;
    void updateSessionTerms(SessionTerms terms) throws DAOException;
    void createSessionTerms(SessionTerms terms) throws DAOException;
}