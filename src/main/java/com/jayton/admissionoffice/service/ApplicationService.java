package com.jayton.admissionoffice.service;

import com.jayton.admissionoffice.model.to.Application;
import com.jayton.admissionoffice.model.to.Status;
import com.jayton.admissionoffice.model.user.User;
import com.jayton.admissionoffice.service.exception.ServiceException;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by Jayton on 06.12.2016.
 */
public interface ApplicationService {
    Long add(User user, Long directionId, LocalDateTime applied) throws ServiceException;
    Application get(Long id) throws ServiceException;
    List<Application> getByUser(Long userId) throws ServiceException;
    List<Application> getByDirection(Long directionId) throws ServiceException;
    void update(Long id, Status status) throws ServiceException;
    boolean delete(Long id) throws ServiceException;
}
