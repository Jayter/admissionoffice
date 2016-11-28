package com.jayton.admissionoffice.dao;

import com.jayton.admissionoffice.dao.exception.DAOException;
import com.jayton.admissionoffice.model.user.Enrollee;
import com.jayton.admissionoffice.model.to.ExamResult;

import java.util.List;

/**
 * Created by Jayton on 24.11.2016.
 */
public interface EnrolleDao extends BaseDao<Enrollee> {
    void addResults(List<ExamResult> results) throws DAOException;
    void deleteResults(ExamResult result) throws DAOException;
    void updateResults(ExamResult result) throws DAOException;
    List<ExamResult> getResults(Long userId) throws DAOException;
}
