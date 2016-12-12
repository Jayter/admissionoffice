package com.jayton.admissionoffice.service;

import com.jayton.admissionoffice.model.university.Direction;
import com.jayton.admissionoffice.service.exception.ServiceException;

import java.math.BigDecimal;
import java.util.List;

public interface DirectionService extends Service<Direction> {
    Direction add(Direction direction) throws ServiceException;
    Direction update(Direction direction) throws ServiceException;
    List<Direction> getByFaculty(long facultyId) throws ServiceException;
    void addEntranceSubject(long directionId, long subjectId, BigDecimal coef) throws ServiceException;
    void deleteEntranceSubject(long directionId, long subjectId) throws ServiceException;
}