package com.jayton.admissionoffice.service;

import com.jayton.admissionoffice.model.to.Application;
import com.jayton.admissionoffice.model.to.Status;
import com.jayton.admissionoffice.model.user.User;
import com.jayton.admissionoffice.service.exception.ServiceException;

import java.time.LocalDateTime;
import java.util.List;

public interface ApplicationService {
    Application add(User user, Long directionId, LocalDateTime applied) throws ServiceException;
    Application get(Long id) throws ServiceException;
    List<Application> getByUser(Long userId) throws ServiceException;
    List<Application> getByDirection(Long directionId) throws ServiceException;
    void update(Long id, Status status) throws ServiceException;
    void delete(Long id) throws ServiceException;
}