package com.jayton.admissionoffice.service;

import com.jayton.admissionoffice.service.impl.*;

public class ServiceFactory {

    private static ServiceFactory instance = new ServiceFactory();

    private DirectionService directionService = new DirectionServiceImpl();
    private FacultyService facultyService = new FacultyServiceImpl();
    private UniversityService universityService = new UniversityServiceImpl();
    private UserService userService = new UserServiceImpl();
    private ApplicationService applicationService = new ApplicationServiceImpl();
    private UtilService utilService = new UtilServiceImpl();

    private ServiceFactory() {
    }

    public static ServiceFactory getInstance() {
        return instance;
    }

    public DirectionService getDirectionService() {
        return directionService;
    }

    public FacultyService getFacultyService() {
        return facultyService;
    }

    public UniversityService getUniversityService() {
        return universityService;
    }

    public UserService getUserService() {
        return userService;
    }

    public ApplicationService getApplicationService() {
        return applicationService;
    }

    public UtilService getUtilService() {
        return utilService;
    }
}