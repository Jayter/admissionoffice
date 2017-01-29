package com.jayton.admissionoffice.service.impl;

import com.jayton.admissionoffice.dao.DirectionDao;
import com.jayton.admissionoffice.dao.exception.DAOException;
import com.jayton.admissionoffice.model.to.AssociatedPairDto;
import com.jayton.admissionoffice.model.to.EntriesWithAssociatedPairsDto;
import com.jayton.admissionoffice.model.to.PaginationDTO;
import com.jayton.admissionoffice.model.university.Direction;
import com.jayton.admissionoffice.service.DirectionService;
import com.jayton.admissionoffice.service.exception.ServiceException;
import com.jayton.admissionoffice.service.exception.ServiceVerificationException;
import com.jayton.admissionoffice.util.di.Injected;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class DirectionServiceImpl implements DirectionService {

    @Injected
    private DirectionDao directionDao;

    public DirectionServiceImpl() {
    }

    @Override
    public long add(Direction direction) throws ServiceException {
        try {
            Objects.requireNonNull(direction);
            verifyEntranceSubjects(direction.getEntranceSubjects(), direction.getAverageCoefficient());
            return directionDao.add(direction);
        } catch (DAOException e) {
            throw new ServiceException(e);
        } catch (NullPointerException e) {
            throw new ServiceVerificationException("Nullable input parameter.");
        }
    }

    @Override
    public Direction get(long id) throws ServiceException {
        try {
            return directionDao.get(id);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public boolean update(Direction direction) throws ServiceException {
        try {
            Objects.requireNonNull(direction);
            return directionDao.update(direction);
        } catch (DAOException e) {
            throw new ServiceException(e);
        } catch (NullPointerException e) {
            throw new ServiceVerificationException("Nullable input parameter.");
        }
    }

    @Override
    public boolean delete(long id) throws ServiceException {
        try {
            return directionDao.delete(id);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public PaginationDTO<Direction> getWithCountByFaculty(long facultyId, long offset, long count) throws ServiceException {
        try {
            PaginationDTO<EntriesWithAssociatedPairsDto<Direction, Long, Long, BigDecimal>> dto
                    = directionDao.getWithCountByFaculty(facultyId, offset, count);
            long totalCount = dto.getCount();

            EntriesWithAssociatedPairsDto<Direction, Long, Long, BigDecimal> entries = dto.getEntries().get(0);

            List<Direction> directions = getDirections(entries);
            return new PaginationDTO<>(directions, totalCount);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public List<Direction> getAll() throws ServiceException {
        try {
            EntriesWithAssociatedPairsDto<Direction, Long, Long, BigDecimal> entries = directionDao.getAll();

            return getDirections(entries);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public synchronized boolean addEntranceSubject(long directionId, long subjectId, BigDecimal coef) throws ServiceException {
        try {
            Objects.requireNonNull(coef);
            Direction direction = directionDao.get(directionId);

            Map<Long, BigDecimal> entranceSubjects = direction.getEntranceSubjects();
            if(entranceSubjects.containsKey(subjectId)) {
                throw new ServiceVerificationException("Can not add duplicated subject.");
            }
            entranceSubjects.put(subjectId, coef);
            verifyEntranceSubjects(entranceSubjects, direction.getAverageCoefficient());

            return directionDao.addSubject(directionId, subjectId, coef);
        } catch (DAOException e) {
            throw new ServiceException(e);
        } catch (NullPointerException e) {
            throw new ServiceVerificationException("Nullable input parameter.");
        }
    }

    @Override
    public boolean deleteEntranceSubject(long directionId, long subjectId) throws ServiceException {
        try {
            return directionDao.deleteSubject(directionId, subjectId);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    private List<Direction> getDirections(EntriesWithAssociatedPairsDto<Direction, Long, Long, BigDecimal> entries) {
        List<Direction> retrievedDirections = entries.getEntries();
        List<AssociatedPairDto<Long, Long, BigDecimal>> retrievedSubjects = entries.getPairs();

        List<Direction> directions = new ArrayList<>();

        for(Direction direction : retrievedDirections) {
            Map<Long, BigDecimal> subjects = retrievedSubjects.stream()
                    .filter(pair -> pair.getOwnerId().equals(direction.getId()))
                    .collect(Collectors.toMap(AssociatedPairDto::getKey,
                            p -> p.getValue().setScale(2, BigDecimal.ROUND_HALF_UP)));

            directions.add(new Direction(direction.getId(), direction.getName(), direction.getAverageCoefficient(),
                    direction.getCountOfStudents(), direction.getFacultyId(), subjects));
        }
        return directions;
    }

    public void verifyEntranceSubjects(Map<Long, BigDecimal> subjects, BigDecimal coefficient) throws ServiceVerificationException {
        if(subjects.size() > 3) {
            throw new ServiceVerificationException("Cannot add more than 3 entrance subjects.");
        }

        BigDecimal resultingCoef = subjects.values().stream().reduce(coefficient, BigDecimal::add)
                .setScale(2, BigDecimal.ROUND_HALF_UP);
        if(subjects.size() >= 2 && resultingCoef.compareTo(BigDecimal.ONE) != 0) {
            throw new ServiceVerificationException("Total coefficient must be strictly equal to 1.");
        }
    }
}