package com.jayton.admissionoffice.dao;

import com.jayton.admissionoffice.dao.exception.DAOException;
import com.jayton.admissionoffice.model.to.AuthorizationResult;
import com.jayton.admissionoffice.model.to.EntriesWithAssociatedPairsDto;
import com.jayton.admissionoffice.model.to.PaginationDto;
import com.jayton.admissionoffice.model.user.User;

import java.util.Map;

public interface UserDao extends Dao<User> {
    boolean addResult(long userId, long subjectId, short mark) throws DAOException;
    boolean deleteResult(long userId, long subjectId) throws DAOException;
    User getByEmail(String email) throws DAOException;
    Map<Long, Short> getUserResults(long userId) throws DAOException;
    AuthorizationResult authorize(String login, String password) throws DAOException;
    int checkEmail(String email) throws DAOException;
    PaginationDto<EntriesWithAssociatedPairsDto<User, Long, Long, Short>> getAllWithCount(long offset, long count) throws DAOException;
    Map<Long, String> getDirectionNames(long userId) throws DAOException;
}