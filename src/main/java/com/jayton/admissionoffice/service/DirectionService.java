package com.jayton.admissionoffice.service;

import com.jayton.admissionoffice.model.university.Direction;
import com.jayton.admissionoffice.service.exception.ServiceException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Created by Jayton on 01.12.2016.
 */
public interface DirectionService extends Service<Direction> {
    Long add(String name, BigDecimal averageCoef, int countOfStudents, Long facultyId, Map<Long, BigDecimal> subjects)
            throws ServiceException;
    void update(Long id, String name, BigDecimal averageCoef, int countOfStudents, Long facultyId,
                Map<Long, BigDecimal> subjects) throws ServiceException;
    List<Direction> getByFaculty(Long facultyId) throws ServiceException;
    void addEntranceSubject(Direction direction, Long subjectId, BigDecimal coef) throws ServiceException;
    boolean deleteEntranceSubject(Long directionId, Long subjectId) throws ServiceException;
}
