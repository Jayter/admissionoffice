package com.jayton.admissionoffice.service.impl;

import com.jayton.admissionoffice.model.user.User;
import com.jayton.admissionoffice.service.UserService;
import com.jayton.admissionoffice.service.exception.ServiceException;

import java.util.List;

/**
 * Created by Jayton on 02.12.2016.
 */
public class UserServiceImpl implements UserService {

    @Override
    public User get(Long id) throws ServiceException {
        return null;
    }

    @Override
    public boolean delete(Long id) throws ServiceException {
        return false;
    }

    @Override
    public List<User> getAll() throws ServiceException {
        return null;
    }
}
