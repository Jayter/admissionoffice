package com.jayton.admissionoffice.service.impl;

import com.jayton.admissionoffice.dao.UserDao;
import com.jayton.admissionoffice.dao.exception.DAOException;
import com.jayton.admissionoffice.model.to.AuthorizationResult;
import com.jayton.admissionoffice.model.user.User;
import com.jayton.admissionoffice.service.UserService;
import com.jayton.admissionoffice.service.exception.ServiceException;
import com.jayton.admissionoffice.service.exception.ServiceVerificationException;
import com.jayton.admissionoffice.util.di.Injected;

import java.util.List;
import java.util.Map;

public class UserServiceImpl implements UserService {

    @Injected
    private UserDao userDao;

    public UserServiceImpl() {
    }

    @Override
    public synchronized User add(User user) throws ServiceException {
        try {
            return userDao.add(user);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public User get(long id) throws ServiceException {
        try {
            return userDao.get(id);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public User getByEmail(String email) throws ServiceException {
        try {
            return userDao.getByEmail(email);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public User update(User user) throws ServiceException {
        try {
            return userDao.update(user);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public void delete(long id) throws ServiceException {
        try {
            userDao.delete(id);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public List<User> getAll(long offset, long count) throws ServiceException {
        try {
            return userDao.getAll(offset, count);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public synchronized void addResult(long userId, long subjectId, short mark) throws ServiceException {
        Map<Long, Short> results;
        try {
             results = userDao.getResultsOfUser(userId);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }

        if(results.size() >= 4) {
            throw new ServiceVerificationException("Cannot add more than 4 results.");
        }

        if(results.containsKey(subjectId)) {
            throw new ServiceVerificationException("Exam result on this subject already exists.");
        }

        try {
            userDao.addResult(userId, subjectId, mark);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public void deleteResult(long userId, long subjectId) throws ServiceException {
        try {
            userDao.deleteResult(userId, subjectId);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public AuthorizationResult authorize(String login, String password) throws ServiceException {
        try {
            return userDao.authorize(login, password);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public Map<Long, String> getDirectionNames(long userId) throws ServiceException {
        try {
            return userDao.getDirectionNames(userId);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }
}