package com.jayton.admissionoffice.service.impl;

import com.jayton.admissionoffice.dao.*;
import com.jayton.admissionoffice.dao.exception.DAOException;
import com.jayton.admissionoffice.model.to.Application;
import com.jayton.admissionoffice.model.to.SessionTerms;
import com.jayton.admissionoffice.model.to.Status;
import com.jayton.admissionoffice.model.university.Direction;
import com.jayton.admissionoffice.model.user.User;
import com.jayton.admissionoffice.service.ApplicationService;
import com.jayton.admissionoffice.service.exception.ServiceException;
import com.jayton.admissionoffice.service.exception.ServiceVerificationException;
import com.jayton.admissionoffice.util.di.Injected;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class ApplicationServiceImpl implements ApplicationService {

    @Injected
    private ApplicationDao applicationDao;
    @Injected
    private DirectionDao directionDao;
    @Injected
    private UtilDao utilDao;

    public ApplicationServiceImpl() {
    }

    @Override
    public synchronized Application add(User user, long directionId, LocalDateTime applied) throws ServiceException {
        verifyApplicationDate(applied);

        try {
            List<Application> retrieved = applicationDao.getByUser(user.getId());
            if(retrieved.size() >= 5) {
                throw new ServiceVerificationException("You can apply only for 5 directions.");
            }

            long countOfDuplicated = retrieved.stream().filter(app -> app.getDirectionId()== directionId).count();
            if(countOfDuplicated != 0) {
                throw new ServiceVerificationException("You have already applied for this direction.");
            }

            Direction direction = directionDao.get(directionId);
            BigDecimal mark = getMark(user, direction);

            Application application = new Application(user.getId(), directionId, applied, mark);

            return applicationDao.add(application);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public Application get(long id) throws ServiceException {
        try {
            return applicationDao.get(id);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public List<Application> getByUser(long userId) throws ServiceException {
        try {
            return applicationDao.getByUser(userId);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public List<Application> getByDirection(long directionId, long offset, long count) throws ServiceException {
        try {
            return applicationDao.getByDirection(directionId, offset, count);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public void update(long id, Status status) throws ServiceException {
        try {
            applicationDao.update(id, status);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public void delete(long id) throws ServiceException {
        try {
            applicationDao.delete(id);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public long getCount(long directionId) throws ServiceException {
        try {
            return applicationDao.getCount(directionId);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    private void verifyApplicationDate(LocalDateTime applied) throws ServiceException {
        SessionTerms terms;
        try {
            terms = utilDao.getSessionTerms((short)applied.getYear());
        } catch (DAOException e) {
            throw new ServiceException(e);
        }

        if(applied.isBefore(terms.getSessionStart()) || applied.isAfter(terms.getSessionEnd())) {
            throw new ServiceException("Application period has already expired!");
        }
    }

    private BigDecimal getMark(User user, Direction direction) throws ServiceException {
        BigDecimal averageMark = BigDecimal.valueOf(user.getAverageMark());
        BigDecimal result = direction.getAverageCoefficient().multiply(averageMark);

        for(Map.Entry<Long, BigDecimal> entrancePair: direction.getEntranceSubjects().entrySet()) {
            if(!user.getResults().containsKey(entrancePair.getKey())) {
                throw new ServiceVerificationException("Inappropriate direction. Exam result is not found");
            }
            BigDecimal examResult = BigDecimal.valueOf(user.getResults().get(entrancePair.getKey()));
            BigDecimal examCoefficient = entrancePair.getValue();
            result = result.add(examCoefficient.multiply(examResult));
        }
        return result.setScale(2, BigDecimal.ROUND_HALF_DOWN);
    }
}