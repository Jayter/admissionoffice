package com.jayton.admissionoffice.service;

import com.jayton.admissionoffice.model.to.AuthorizationResult;
import com.jayton.admissionoffice.model.user.User;
import com.jayton.admissionoffice.service.exception.ServiceException;

import java.util.List;
import java.util.Map;

public interface UserService extends Service<User> {
    User add(User user) throws ServiceException;
    User update(User user) throws ServiceException;
    void addResult(long userId, long subjectId, short mark) throws ServiceException;
    void deleteResult(long userId, long subjectId) throws ServiceException;
    AuthorizationResult authorize(String login, String password) throws ServiceException;
    List<User> getAll(long offset, long count) throws ServiceException;
    User getByEmail(String email) throws ServiceException;
    long getTotalCount() throws ServiceException;
    Map<Long, String> getDirectionNames(long userId) throws ServiceException;
}