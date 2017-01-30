package com.jayton.admissionoffice.service.impl;

import com.jayton.admissionoffice.dao.UserDao;
import com.jayton.admissionoffice.dao.exception.DAOException;
import com.jayton.admissionoffice.model.to.AssociatedPairDto;
import com.jayton.admissionoffice.model.to.AuthorizationResult;
import com.jayton.admissionoffice.model.to.EntriesWithAssociatedPairsDto;
import com.jayton.admissionoffice.model.to.PaginationDto;
import com.jayton.admissionoffice.model.user.User;
import com.jayton.admissionoffice.service.UserService;
import com.jayton.admissionoffice.service.exception.ServiceException;
import com.jayton.admissionoffice.service.exception.ServiceVerificationException;
import com.jayton.admissionoffice.util.di.Injected;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class UserServiceImpl implements UserService {

    @Injected
    private UserDao userDao;

    public UserServiceImpl() {
    }

    @Override
    public synchronized long add(User user) throws ServiceException {
        try {
            Objects.requireNonNull(user);
            return userDao.add(user);
        } catch (DAOException e) {
            throw new ServiceException(e);
        } catch (NullPointerException e) {
            throw new ServiceVerificationException("Nullable input parameter.");
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
            Objects.requireNonNull(email);
            return userDao.getByEmail(email);
        } catch (DAOException e) {
            throw new ServiceException(e);
        } catch (NullPointerException e) {
            throw new ServiceVerificationException("Nullable input parameter.");
        }
    }

    @Override
    public boolean update(User user) throws ServiceException {
        try {
            Objects.requireNonNull(user);
            return userDao.update(user);
        } catch (DAOException e) {
            throw new ServiceException(e);
        } catch (NullPointerException e) {
            throw new ServiceVerificationException("Nullable input parameter.");
        }
    }

    @Override
    public boolean delete(long id) throws ServiceException {
        try {
            return userDao.delete(id);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public PaginationDto<User> getAllWithCount(long offset, long count) throws ServiceException {
        try {
            PaginationDto<EntriesWithAssociatedPairsDto<User, Long, Long, Short>> dto = userDao.getAllWithCount(offset, count);
            long totalCount = dto.getCount();

            EntriesWithAssociatedPairsDto<User, Long, Long, Short> entries = dto.getEntries().get(0);
            List<User> retrievedUsers = entries.getEntries();
            List<AssociatedPairDto<Long, Long, Short>> retrievedResults = entries.getPairs();

            List<User> users =  new ArrayList<>();

            for(User user : retrievedUsers) {
                Map<Long, Short> results = retrievedResults.stream()
                        .filter(pair -> pair.getOwnerId().equals(user.getId()))
                        .collect(Collectors.toMap(AssociatedPairDto::getKey, AssociatedPairDto::getValue));

                users.add(new User(user.getId(), user.getName(), user.getLastName(), user.getAddress(), user.getEmail(),
                        user.getPhoneNumber(), user.getBirthDate(), user.getAverageMark(), results));
            }

            return new PaginationDto<>(users, totalCount);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public synchronized boolean addResult(long userId, long subjectId, short mark) throws ServiceException {
        Map<Long, Short> results;
        try {
             results = userDao.getUserResults(userId);
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
            return userDao.addResult(userId, subjectId, mark);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public boolean deleteResult(long userId, long subjectId) throws ServiceException {
        try {
            return userDao.deleteResult(userId, subjectId);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public AuthorizationResult authorize(String login, String password) throws ServiceException {
        try {
            Objects.requireNonNull(login);
            Objects.requireNonNull(password);
            return userDao.authorize(login, password);
        } catch (DAOException e) {
            throw new ServiceException(e);
        } catch (NullPointerException e) {
            throw new ServiceVerificationException("Nullable input parameter.");
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