package com.jayton.admissionoffice.service;

import com.jayton.admissionoffice.service.impl.*;

public class ServiceFactory {

    private static ServiceFactory instance = new ServiceFactory();

    private final DirectionService directionService = new DirectionServiceImpl();
    private final FacultyService facultyService = new FacultyServiceImpl();
    private final UniversityService universityService = new UniversityServiceImpl();
    private final UserService userService = new UserServiceImpl();
    private final ApplicationService applicationService = new ApplicationServiceImpl();
    private final UtilService utilService = new UtilServiceImpl();

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