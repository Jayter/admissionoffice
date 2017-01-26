package com.jayton.admissionoffice.dao;

import com.jayton.admissionoffice.dao.exception.DAOException;
import com.jayton.admissionoffice.model.to.AuthorizationResult;
import com.jayton.admissionoffice.model.user.User;

import java.util.List;
import java.util.Map;

public interface UserDao extends Dao<User> {
    void addResult(long userId, long subjectId, short mark) throws DAOException;
    void deleteResult(long userId, long subjectId) throws DAOException;
    User getByEmail(String email) throws DAOException;
    Map<Long, Short> getResultsOfUser(long userId) throws DAOException;
    AuthorizationResult authorize(String login, String password) throws DAOException;
    int checkEmail(String email) throws DAOException;
    List<User> getAll(long offset, long count) throws DAOException;
    Map<Long, String> getDirectionNames(long userId) throws DAOException;
}