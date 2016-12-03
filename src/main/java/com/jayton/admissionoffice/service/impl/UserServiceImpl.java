package com.jayton.admissionoffice.service.impl;

import com.jayton.admissionoffice.dao.FactoryProducer;
import com.jayton.admissionoffice.dao.UserDao;
import com.jayton.admissionoffice.dao.exception.DAOException;
import com.jayton.admissionoffice.model.to.AuthorizationResult;
import com.jayton.admissionoffice.model.user.User;
import com.jayton.admissionoffice.service.UserService;
import com.jayton.admissionoffice.service.exception.ServiceException;
import com.jayton.admissionoffice.service.exception.ServiceVerificationException;
import com.jayton.admissionoffice.service.util.ServiceVerifier;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.jayton.admissionoffice.service.util.ServiceVerifier.NULLABLE;

/**
 * Created by Jayton on 02.12.2016.
 */
public class UserServiceImpl implements UserService {

    @Override
    public synchronized Long add(String name, String lastName, String address, String email, String phoneNumber, LocalDate birthDate,
                    BigDecimal averageCoef, String password, Map<Long, BigDecimal> results) throws ServiceException {
        ServiceVerifier.verifyEmail(email);
        checkEmail(email);
        ServiceVerifier.verifyPassword(email);
        verifyUser(name, lastName, address, phoneNumber, birthDate, averageCoef);
        verifyResults(results);

        User user = new User(name, lastName, address, email, password, phoneNumber, birthDate, averageCoef, results);

        UserDao userDao = FactoryProducer.getInstance().getPostgresDaoFactory().getUserDao();
        try {
            return userDao.add(user);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public User get(Long id) throws ServiceException {
        ServiceVerifier.verifyId(id);

        UserDao userDao = FactoryProducer.getInstance().getPostgresDaoFactory().getUserDao();
        try {
            return userDao.get(id);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public void update(Long id, String name, String lastName, String address, String phoneNumber, LocalDate birthDate,
                       BigDecimal averageCoef, Map<Long, BigDecimal> results) throws ServiceException {
        ServiceVerifier.verifyId(id);
        verifyUser(name, lastName, address, phoneNumber, birthDate, averageCoef);
        verifyResults(results);

        User user = new User(id, name, lastName, address, phoneNumber, birthDate, averageCoef, results);

        UserDao userDao = FactoryProducer.getInstance().getPostgresDaoFactory().getUserDao();
        try {
            userDao.update(user);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public boolean delete(Long id) throws ServiceException {
        ServiceVerifier.verifyId(id);

        UserDao userDao = FactoryProducer.getInstance().getPostgresDaoFactory().getUserDao();
        try {
            return userDao.delete(id);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public List<User> getAll() throws ServiceException {
        UserDao userDao = FactoryProducer.getInstance().getPostgresDaoFactory().getUserDao();
        try {
            return userDao.getAll();
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public synchronized void addResult(Long userId, Long subjectId, BigDecimal mark) throws ServiceException {
        ServiceVerifier.verifyIds(userId, subjectId);
        ServiceVerifier.verifyResult(mark);

        Map<Long, BigDecimal> results;
        UserDao userDao = FactoryProducer.getInstance().getPostgresDaoFactory().getUserDao();
        try {
             results = userDao.getByUser(userId);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }

        results.put(subjectId, mark);
        verifyResults(results);

        try {
            userDao.addResult(userId, subjectId, mark);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public boolean deleteResult(Long userId, Long subjectId) throws ServiceException {
        ServiceVerifier.verifyIds(userId, subjectId);

        UserDao userDao = FactoryProducer.getInstance().getPostgresDaoFactory().getUserDao();
        try {
            return userDao.deleteResult(userId, subjectId);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public boolean authorize(String login, String password) throws ServiceException {
        ServiceVerifier.verifyEmail(login);
        ServiceVerifier.verifyPassword(password);

        AuthorizationResult result;

        UserDao userDao = FactoryProducer.getInstance().getPostgresDaoFactory().getUserDao();
        try {
            result =  userDao.authorize(login, password);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
        if(result == AuthorizationResult.NULL) {
            throw new ServiceVerificationException("Incorrect login or(and) password.");
        }

        return result == AuthorizationResult.ADMIN;
    }



    private void checkEmail(String email) throws ServiceException {
        UserDao userDao = FactoryProducer.getInstance().getPostgresDaoFactory().getUserDao();
        try {
            int count = userDao.checkEmail(email);
            if(count != 0) {
                throw new ServiceVerificationException("Email already exists.");
            }
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    private void verifyUser(String name, String lastName, String address, String phoneNumber,
                            LocalDate birthDate, BigDecimal averageCoef) throws ServiceException {
        ServiceVerifier.verifyStrings(name, lastName, address, phoneNumber);
        if(Objects.isNull(birthDate)) {
            throw new ServiceVerificationException(String.format(NULLABLE, "Date"));
        }

        ServiceVerifier.verifyCoef(averageCoef);
    }

    private void verifyResults(Map<Long, BigDecimal> results) throws ServiceException {
        if(Objects.isNull(results)) {
            throw new ServiceVerificationException(String.format(NULLABLE, "Results"));
        }
        if(results.size() < 3 || results.size() > 4) {
            throw new ServiceVerificationException(String.format("Count of results must be either 3 or 4, but was: %d.",
                    results.size()));
        }
        if(results.keySet().size() != results.values().size()) {
            throw new ServiceVerificationException("Results must be of different subjects.");
        }
        for(Map.Entry<Long, BigDecimal> pair: results.entrySet()) {
            ServiceVerifier.verifyId(pair.getKey());
            ServiceVerifier.verifyResult(pair.getValue());
        }
    }
}
