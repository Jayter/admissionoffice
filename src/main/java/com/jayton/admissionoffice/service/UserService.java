package com.jayton.admissionoffice.service;

import com.jayton.admissionoffice.model.user.User;
import com.jayton.admissionoffice.service.exception.ServiceException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

/**
 * Created by Jayton on 02.12.2016.
 */
public interface UserService extends BaseService<User> {
    Long add(String name, String lastName, String address, String email, String phoneNumber, LocalDate birthDate,
             BigDecimal averageCoef, String password, Map<Long, BigDecimal> results) throws ServiceException;
    void update(Long id, String name, String lastName, String address, String phoneNumber, LocalDate birthDate,
             BigDecimal averageCoef, Map<Long, BigDecimal> results) throws ServiceException;
    void addResult(Long userId, Long subjectId, BigDecimal mark) throws ServiceException;
    boolean deleteResult(Long userId, Long subjectId) throws ServiceException;
    boolean authorize(String login, String password) throws ServiceException;
}
