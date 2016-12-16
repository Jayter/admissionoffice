package com.jayton.admissionoffice.service.util;

import com.jayton.admissionoffice.dao.ApplicationDao;
import com.jayton.admissionoffice.dao.DirectionDao;
import com.jayton.admissionoffice.dao.FactoryProducer;
import com.jayton.admissionoffice.dao.exception.DAOException;
import com.jayton.admissionoffice.model.to.Application;
import com.jayton.admissionoffice.model.to.Status;
import com.jayton.admissionoffice.model.university.Direction;
import com.jayton.admissionoffice.service.exception.ServiceException;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.stream.Collectors;

public class ApplicationHandler {

    private static ApplicationHandler instance = new ApplicationHandler();

    private final Comparator<Application> dateComparator =
            (app1, app2) -> app1.getCreationTime().compareTo(app2.getCreationTime());
    private final Comparator<Application> markComparator =
            (app1, app2) -> app1.getMark().compareTo(app2.getMark());

    private Map<Long, Direction> directions;

    private ApplicationHandler() {
    }

    public static ApplicationHandler getInstance() {
        return instance;
    }

    public void handleApplications() throws ServiceException {
        DirectionDao directionDao = FactoryProducer.getInstance().getPostgresDaoFactory().getDirectionDao();
        ApplicationDao applicationDao = FactoryProducer.getInstance().getPostgresDaoFactory().getApplicationDao();

        List<Application> applications;
        try {
            directions = directionDao.getAll().stream().collect(Collectors.toMap(Direction::getId, direction -> direction));
            applications = applicationDao.getAll();
        } catch (DAOException e) {
            throw new ServiceException("Failed to handle applications.", e);
        }

        Map<Long, PriorityBlockingQueue<Application>> userApplications = convertToUserQueue(applications);
        Map<Long, PriorityBlockingQueue<Application>> acceptedApplications = distributeInDirections(userApplications);

        List<Application> applicationsToUpdate = new ArrayList<>();
        acceptedApplications.forEach((id, queue) -> applicationsToUpdate.addAll(queue));

        try {
            applicationDao.updateAll(applicationsToUpdate, Status.APPROVED);
        } catch (DAOException e) {
            throw new ServiceException("Failed to save results.", e);
        }
    }

    public Map<Long, PriorityBlockingQueue<Application>> distributeInDirections(Map<Long, PriorityBlockingQueue<Application>> apps) {
        Map<Long, PriorityBlockingQueue<Application>> destinationApplications = new ConcurrentHashMap<>();
        for(Map.Entry<Long, PriorityBlockingQueue<Application>> pair: apps.entrySet()) {
            addFirstApplication(pair.getKey(), apps, destinationApplications);
        }
        return destinationApplications;
    }

    private void addFirstApplication(Long userId, Map<Long, PriorityBlockingQueue<Application>> sourceApplications,
                                     Map<Long, PriorityBlockingQueue<Application>> destinationApplications) {
        PriorityBlockingQueue<Application> userQueue = sourceApplications.get(userId);
        if(userQueue != null && !userQueue.isEmpty()) {
            Application firstApplication = userQueue.poll();
            PriorityBlockingQueue<Application> directionQueue = destinationApplications.get(firstApplication.getDirectionId());
            if(directionQueue == null) {
                directionQueue = new PriorityBlockingQueue<>(directions.get(firstApplication.getDirectionId())
                        .getCountOfStudents(), markComparator);
                destinationApplications.put(firstApplication.getDirectionId(), directionQueue);
            }

            directionQueue.add(firstApplication);
            if(directionQueue.size() > directions.get(firstApplication.getDirectionId()).getCountOfStudents()) {
                Application application = directionQueue.poll();
                addFirstApplication(application.getUserId(), sourceApplications, destinationApplications);
            }
        }
    }

    private Map<Long, PriorityBlockingQueue<Application>> convertToUserQueue(List<Application> applications) {
        Map<Long, PriorityBlockingQueue<Application>> result = new ConcurrentHashMap<>();

        for(ListIterator<Application> iterator = applications.listIterator(); iterator.hasNext(); ) {
            Application first = iterator.next();
            PriorityBlockingQueue<Application> queue = new PriorityBlockingQueue<>(5, dateComparator);
            queue.add(first);

            while (iterator.hasNext()) {
                Application next = iterator.next();
                if(first.getUserId() != next.getUserId()) {
                    iterator.previous();
                    break;
                }
                queue.add(next);
            }
            result.put(first.getUserId(), queue);
        }
        return result;
    }
}