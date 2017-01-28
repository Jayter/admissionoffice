package com.jayton.admissionoffice.dao;

import com.jayton.admissionoffice.dao.exception.DAOException;
import com.jayton.admissionoffice.model.to.Application;
import com.jayton.admissionoffice.model.to.ApplicationDto;
import com.jayton.admissionoffice.model.to.Status;

import java.util.List;

public interface ApplicationDao {
    Application get(long id) throws DAOException;
    long add(Application application) throws DAOException;
    boolean update(long id, Status status) throws DAOException;
    boolean delete(long id) throws DAOException;
    boolean updateAll(List<Application> applications, Status status) throws DAOException;
    List<Application> getByUser(long userId) throws DAOException;
    ApplicationDto getByDirection(long directionId, long offset, long count) throws DAOException;
    List<Application> getAll() throws DAOException;
}