package com.jayton.admissionoffice.dao;

import com.jayton.admissionoffice.dao.exception.DAOException;
import com.jayton.admissionoffice.model.to.EntranceSubject;

import java.util.List;

/**
 * Created by Jayton on 29.11.2016.
 */
public interface EntranceSubjectDao {
    void addSubject(EntranceSubject subject) throws DAOException;
    void addSubjects(List<EntranceSubject> subject) throws DAOException;
    void updateSubjects(List<EntranceSubject> subjects) throws DAOException;
    boolean deleteSubject(Long directionId, Long subjectId) throws DAOException;
    List<EntranceSubject> getSubjects(Long directionId) throws DAOException;
}
