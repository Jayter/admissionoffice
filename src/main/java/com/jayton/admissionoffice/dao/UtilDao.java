package com.jayton.admissionoffice.dao;

import com.jayton.admissionoffice.dao.exception.DAOException;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by Jayton on 06.12.2016.
 */
public interface UtilDao {
    List<LocalDateTime> getSessionDate(Integer currentYear) throws DAOException;
}
