package com.jayton.admissionoffice.dao;

import com.jayton.admissionoffice.dao.exception.DAOException;
import com.jayton.admissionoffice.model.to.EntriesWithAssociatedPairsDto;
import com.jayton.admissionoffice.model.to.PaginationDTO;
import com.jayton.admissionoffice.model.university.Direction;

import java.math.BigDecimal;
import java.util.Map;

public interface DirectionDao extends Dao<Direction> {
    PaginationDTO<EntriesWithAssociatedPairsDto<Direction, Long, Long, BigDecimal>> getWithCountByFaculty(long facultyId, long offset, long count) throws DAOException;
    boolean deleteSubject(long directionId, long subjectId) throws DAOException;
    boolean addSubject(long directionId, long subjectId, BigDecimal coef) throws DAOException;
    Map<Long, BigDecimal> getEntranceSubjects(long directionId) throws DAOException;
    EntriesWithAssociatedPairsDto<Direction, Long, Long, BigDecimal> getAll() throws DAOException;
}