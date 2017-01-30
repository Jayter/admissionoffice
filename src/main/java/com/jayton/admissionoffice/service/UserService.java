package com.jayton.admissionoffice.service;

import com.jayton.admissionoffice.model.to.AuthorizationResult;
import com.jayton.admissionoffice.model.to.PaginationDto;
import com.jayton.admissionoffice.model.user.User;
import com.jayton.admissionoffice.service.exception.ServiceException;

import java.util.Map;

public interface UserService extends Service<User> {
    long add(User user) throws ServiceException;
    boolean update(User user) throws ServiceException;
    boolean addResult(long userId, long subjectId, short mark) throws ServiceException;
    boolean deleteResult(long userId, long subjectId) throws ServiceException;
    AuthorizationResult authorize(String login, String password) throws ServiceException;
    PaginationDto<User> getAllWithCount(long offset, long count) throws ServiceException;
    User getByEmail(String email) throws ServiceException;
    Map<Long, String> getDirectionNames(long userId) throws ServiceException;
}