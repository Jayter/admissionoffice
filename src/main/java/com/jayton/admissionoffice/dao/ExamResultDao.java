package com.jayton.admissionoffice.dao;

import com.jayton.admissionoffice.dao.exception.DAOException;
import com.jayton.admissionoffice.model.to.ExamResult;

import java.util.List;

/**
 * Created by Jayton on 29.11.2016.
 */
public interface ExamResultDao {
    void add(List<ExamResult> results) throws DAOException;
    void add(ExamResult result) throws DAOException;
    boolean delete(ExamResult result) throws DAOException;
    void update(ExamResult result) throws DAOException;
    List<ExamResult> getByUser(Long userId) throws DAOException;
}
