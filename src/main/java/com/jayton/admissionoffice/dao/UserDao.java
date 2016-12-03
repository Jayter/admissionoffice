package com.jayton.admissionoffice.dao;

import com.jayton.admissionoffice.dao.exception.DAOException;
import com.jayton.admissionoffice.model.to.AuthorizationResult;
import com.jayton.admissionoffice.model.user.User;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Created by Jayton on 24.11.2016.
 */
public interface UserDao extends BaseDao<User> {
    void addResult(Long userId, Long subjectId, BigDecimal mark) throws DAOException;
    boolean deleteResult(Long userId, Long subjectId) throws DAOException;
    Map<Long, BigDecimal> getByUser(Long userId) throws DAOException;
    AuthorizationResult authorize(String login, String password) throws DAOException;
    int checkEmail(String email) throws DAOException;
}
