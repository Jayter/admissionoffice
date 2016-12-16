package com.jayton.admissionoffice.service.impl;

import com.jayton.admissionoffice.dao.FactoryProducer;
import com.jayton.admissionoffice.dao.UserDao;
import com.jayton.admissionoffice.dao.exception.DAOException;
import com.jayton.admissionoffice.model.to.AuthorizationResult;
import com.jayton.admissionoffice.model.user.User;
import com.jayton.admissionoffice.service.UserService;
import com.jayton.admissionoffice.service.exception.ServiceException;
import com.jayton.admissionoffice.service.exception.ServiceVerificationException;

import java.util.List;
import java.util.Map;

public class UserServiceImpl implements UserService {

    @Override
    public synchronized User add(User user) throws ServiceException {
        UserDao userDao = FactoryProducer.getInstance().getPostgresDaoFactory().getUserDao();
        try {
            return userDao.add(user);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public User get(long id) throws ServiceException {
        UserDao userDao = FactoryProducer.getInstance().getPostgresDaoFactory().getUserDao();
        try {
            return userDao.get(id);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public User getByEmail(String email) throws ServiceException {
        UserDao userDao = FactoryProducer.getInstance().getPostgresDaoFactory().getUserDao();
        try {
            return userDao.getByEmail(email);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public User update(User user) throws ServiceException {
        UserDao userDao = FactoryProducer.getInstance().getPostgresDaoFactory().getUserDao();
        try {
            return userDao.update(user);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public void delete(long id) throws ServiceException {
        UserDao userDao = FactoryProducer.getInstance().getPostgresDaoFactory().getUserDao();
        try {
            userDao.delete(id);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public List<User> getAll(long offset, long count) throws ServiceException {
        UserDao userDao = FactoryProducer.getInstance().getPostgresDaoFactory().getUserDao();
        try {
            return userDao.getAll(offset, count);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public synchronized void addResult(long userId, long subjectId, short mark) throws ServiceException {
        Map<Long, Short> results;
        UserDao userDao = FactoryProducer.getInstance().getPostgresDaoFactory().getUserDao();
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
        UserDao userDao = FactoryProducer.getInstance().getPostgresDaoFactory().getUserDao();
        try {
            userDao.deleteResult(userId, subjectId);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public AuthorizationResult authorize(String login, String password) throws ServiceException {
        UserDao userDao = FactoryProducer.getInstance().getPostgresDaoFactory().getUserDao();
        try {
            return userDao.authorize(login, password);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public long getTotalCount() throws ServiceException {
        UserDao userDao = FactoryProducer.getInstance().getPostgresDaoFactory().getUserDao();
        try {
            return userDao.getTotalCount();
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public Map<Long, String> getDirectionNames(long userId) throws ServiceException {
        UserDao userDao = FactoryProducer.getInstance().getPostgresDaoFactory().getUserDao();
        try {
            return userDao.getDirectionNames(userId);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }
}