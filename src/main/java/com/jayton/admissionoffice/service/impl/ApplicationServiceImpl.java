package com.jayton.admissionoffice.service.impl;

import com.jayton.admissionoffice.dao.*;
import com.jayton.admissionoffice.dao.exception.DAOException;
import com.jayton.admissionoffice.model.to.Application;
import com.jayton.admissionoffice.model.to.ApplicationDto;
import com.jayton.admissionoffice.model.to.Status;
import com.jayton.admissionoffice.model.university.Direction;
import com.jayton.admissionoffice.model.user.User;
import com.jayton.admissionoffice.service.ApplicationService;
import com.jayton.admissionoffice.service.exception.ServiceException;
import com.jayton.admissionoffice.service.exception.ServiceVerificationException;
import com.jayton.admissionoffice.util.di.Injected;
import com.jayton.admissionoffice.util.exception.ApplicationException;
import com.jayton.admissionoffice.util.lock.Synchronizer;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ApplicationServiceImpl implements ApplicationService {

    @Injected
    private ApplicationDao applicationDao;
    @Injected
    private DirectionDao directionDao;
    @Injected
    private UtilDao utilDao;
    @Injected
    private Synchronizer synchronizer;

    public ApplicationServiceImpl() {
    }

    @Override
    public long add(User user, long directionId, LocalDateTime applied) throws ServiceException {
        try {
            Objects.requireNonNull(user);
            Objects.requireNonNull(applied);

            return synchronizer.executeSynchronously(user.getId(), () -> {
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
            });
        } catch (DAOException e) {
            throw new ServiceException(e);
        } catch (NullPointerException e) {
            throw new ServiceVerificationException("Nullable input parameters.");
        } catch (ServiceException e) {
            throw e;
        } catch (ApplicationException e) {
            throw new ServiceException("Internal server error.");
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
    public ApplicationDto getByDirection(long directionId, long offset, long count) throws ServiceException {
        try {
            return applicationDao.getByDirection(directionId, offset, count);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public boolean update(long id, Status status) throws ServiceException {
        try {
            Objects.requireNonNull(status);
            return applicationDao.update(id, status);
        } catch (DAOException e) {
            throw new ServiceException(e);
        } catch (NullPointerException e) {
            throw new ServiceVerificationException("Nullable input parameter.");
        }
    }

    @Override
    public boolean delete(long id) throws ServiceException {
        try {
            return applicationDao.delete(id);
        } catch (DAOException e) {
            throw new ServiceException(e);
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