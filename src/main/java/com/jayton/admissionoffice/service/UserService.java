package com.jayton.admissionoffice.service;

import com.jayton.admissionoffice.model.to.AuthorizationResult;
import com.jayton.admissionoffice.model.user.User;
import com.jayton.admissionoffice.service.exception.ServiceException;

public interface UserService extends Service<User> {
    User add(User user) throws ServiceException;
    User update(User user) throws ServiceException;
    void addResult(long userId, long subjectId, short mark) throws ServiceException;
    void deleteResult(long userId, long subjectId) throws ServiceException;
    AuthorizationResult authorize(String login, String password) throws ServiceException;
    User getByEmail(String email) throws ServiceException;
}