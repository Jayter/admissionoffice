package com.jayton.admissionoffice.dao;

import com.jayton.admissionoffice.dao.exception.DAOException;
import com.jayton.admissionoffice.model.NamedEntity;
import com.jayton.admissionoffice.model.to.SessionTerms;

import java.util.List;

public interface UtilDao {
    List<NamedEntity> getAllSubjects() throws DAOException;
    SessionTerms getSessionTerms(Short currentYear) throws DAOException;
}
