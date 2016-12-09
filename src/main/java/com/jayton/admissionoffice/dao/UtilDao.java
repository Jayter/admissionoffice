package com.jayton.admissionoffice.dao;

import com.jayton.admissionoffice.dao.exception.DAOException;
import com.jayton.admissionoffice.model.NamedEntity;

import java.time.LocalDateTime;
import java.util.List;

public interface UtilDao {
    List<NamedEntity> getAllSubjects() throws DAOException;
    List<LocalDateTime> getSessionDate(Integer currentYear) throws DAOException;
}
