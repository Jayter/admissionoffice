package com.jayton.admissionoffice.service;

import com.jayton.admissionoffice.service.impl.*;

/**
 * Created by Jayton on 03.12.2016.
 */
public class ServiceFactory {

    private static ServiceFactory instance = new ServiceFactory();
    private final DirectionService directionService = new DirectionServiceImpl();
    private final FacultyService facultyService = new FacultyServiceImpl();
    private final SubjectService subjectService = new SubjectServiceImpl();
    private final UniversityService universityService = new UniversityServiceImpl();
    private final UserService userService = new UserServiceImpl();

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

    public SubjectService getSubjectService() {
        return subjectService;
    }

    public UniversityService getUniversityService() {
        return universityService;
    }

    public UserService getUserService() {
        return userService;
    }
}
