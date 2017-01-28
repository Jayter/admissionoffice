package com.jayton.admissionoffice.service;

import com.jayton.admissionoffice.model.to.PaginationDTO;
import com.jayton.admissionoffice.model.university.Direction;
import com.jayton.admissionoffice.service.exception.ServiceException;

import java.math.BigDecimal;
import java.util.List;

public interface DirectionService extends Service<Direction> {
    long add(Direction direction) throws ServiceException;
    boolean update(Direction direction) throws ServiceException;
    PaginationDTO<Direction> getWithCountByFaculty(long facultyId, long offset, long count) throws ServiceException;
    boolean addEntranceSubject(long directionId, long subjectId, BigDecimal coef) throws ServiceException;
    boolean deleteEntranceSubject(long directionId, long subjectId) throws ServiceException;
    List<Direction> getAll() throws ServiceException;
}