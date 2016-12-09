package com.jayton.admissionoffice.dao;

import com.jayton.admissionoffice.dao.exception.DAOException;
import com.jayton.admissionoffice.model.to.AuthorizationResult;
import com.jayton.admissionoffice.model.user.User;

import java.util.Map;

public interface UserDao extends BaseDao<User> {
    void addResult(Long userId, Long subjectId, Short mark) throws DAOException;
    void deleteResult(Long userId, Long subjectId) throws DAOException;
    User getByEmail(String email) throws DAOException;
    Map<Long, Short> getResultsOfUser(Long userId) throws DAOException;
    AuthorizationResult authorize(String login, String password) throws DAOException;
    int checkEmail(String email) throws DAOException;
}
