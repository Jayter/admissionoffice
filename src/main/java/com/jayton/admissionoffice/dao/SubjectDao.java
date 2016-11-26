package com.jayton.admissionoffice.dao;

import com.jayton.admissionoffice.dao.exception.DAOException;
import com.jayton.admissionoffice.model.Subject;

import java.util.List;

/**
 * Created by Jayton on 24.11.2016.
 */
public interface SubjectDao extends BaseDao<Subject> {
    List<Subject> getAll() throws DAOException;
    Subject getByName(String name) throws DAOException;
}
