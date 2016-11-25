package com.jayton.admissionoffice.dao;

import com.jayton.admissionoffice.model.Subject;

/**
 * Created by Jayton on 24.11.2016.
 */
public interface SubjectDao extends BaseDao<Subject> {
    Subject getByName(String name);
}
