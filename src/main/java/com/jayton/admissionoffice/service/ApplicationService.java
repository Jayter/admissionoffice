package com.jayton.admissionoffice.service;

import com.jayton.admissionoffice.model.to.Application;
import com.jayton.admissionoffice.model.to.ApplicationDTO;
import com.jayton.admissionoffice.model.to.Status;
import com.jayton.admissionoffice.model.user.User;
import com.jayton.admissionoffice.service.exception.ServiceException;

import java.time.LocalDateTime;
import java.util.List;

public interface ApplicationService {
    Application add(User user, long directionId, LocalDateTime applied) throws ServiceException;
    Application get(long id) throws ServiceException;
    List<Application> getByUser(long userId) throws ServiceException;
    ApplicationDTO getByDirection(long directionId, long offset, long count) throws ServiceException;
    void update(long id, Status status) throws ServiceException;
    void delete(long id) throws ServiceException;
}