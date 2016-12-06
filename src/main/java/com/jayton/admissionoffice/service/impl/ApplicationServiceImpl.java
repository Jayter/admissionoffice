package com.jayton.admissionoffice.service.impl;

import com.jayton.admissionoffice.dao.*;
import com.jayton.admissionoffice.dao.exception.DAOException;
import com.jayton.admissionoffice.model.to.Application;
import com.jayton.admissionoffice.model.to.Status;
import com.jayton.admissionoffice.model.university.Direction;
import com.jayton.admissionoffice.model.user.User;
import com.jayton.admissionoffice.service.ApplicationService;
import com.jayton.admissionoffice.service.exception.ServiceException;
import com.jayton.admissionoffice.service.exception.ServiceVerificationException;
import com.jayton.admissionoffice.service.util.ServiceVerifier;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Created by Jayton on 06.12.2016.
 */
public class ApplicationServiceImpl implements ApplicationService {
    @Override
    public Long add(User user, Long directionId, LocalDateTime applied) throws ServiceException {
        ServiceVerifier.verifyId(directionId);
        verifyApplicationDate(applied);

        DirectionDao directionDao = FactoryProducer.getInstance().getPostgresDaoFactory().getDirectionDao();
        ApplicationDao applicationDao = FactoryProducer.getInstance().getPostgresDaoFactory().getApplicationDao();

        try {
            Direction direction = directionDao.get(directionId);
            BigDecimal mark = getMark(user, direction);
            Application application = new Application(user.getId(), directionId, applied, mark);

            return applicationDao.add(application);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public Application get(Long id) throws ServiceException {
        ServiceVerifier.verifyId(id);

        ApplicationDao applicationDao = FactoryProducer.getInstance().getPostgresDaoFactory().getApplicationDao();
        try {
            return applicationDao.get(id);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public List<Application> getByUser(Long userId) throws ServiceException {
        ServiceVerifier.verifyId(userId);

        ApplicationDao applicationDao = FactoryProducer.getInstance().getPostgresDaoFactory().getApplicationDao();
        try {
            return applicationDao.getByDirection(userId);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public List<Application> getByDirection(Long directionId) throws ServiceException {
        ServiceVerifier.verifyId(directionId);

        ApplicationDao applicationDao = FactoryProducer.getInstance().getPostgresDaoFactory().getApplicationDao();
        try {
            return applicationDao.getByDirection(directionId);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public void update(Long id, Status status) throws ServiceException {
        ServiceVerifier.verifyId(id);
        ServiceVerifier.verifyObject(status);

        ApplicationDao applicationDao = FactoryProducer.getInstance().getPostgresDaoFactory().getApplicationDao();
        try {
            applicationDao.update(id, status);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public boolean delete(Long id) throws ServiceException {
        ServiceVerifier.verifyId(id);

        ApplicationDao applicationDao = FactoryProducer.getInstance().getPostgresDaoFactory().getApplicationDao();
        try {
            return applicationDao.delete(id);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    private void verifyApplicationDate(LocalDateTime applied) throws ServiceException {
        UtilDao utilDao = FactoryProducer.getInstance().getPostgresDaoFactory().getUtilDao();

        List<LocalDateTime> terms;
        try {
            terms = utilDao.getSessionDate(applied.getYear());
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
        if(terms.get(0).isAfter(applied) || terms.get(1).isBefore(applied)) {
            throw new ServiceException("Application period has already expired!");
        }
    }

    private BigDecimal getMark(User user, Direction direction) throws ServiceException {
        BigDecimal result = user.getAverageMark().multiply(direction.getAverageCoefficient());
        for(Map.Entry<Long, BigDecimal> entrancePair: direction.getEntranceSubjects().entrySet()) {
            if(!user.getResults().containsKey(entrancePair.getKey())) {
                throw new ServiceVerificationException("Inappropriate direction. Exam result is not found");
            }
            result = result.add(user.getResults().get(entrancePair.getKey()).multiply(entrancePair.getValue()));
        }
        return result.setScale(2, BigDecimal.ROUND_HALF_DOWN);
    }
}
