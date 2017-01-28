package com.jayton.admissionoffice.service.util;

import com.jayton.admissionoffice.dao.ApplicationDao;
import com.jayton.admissionoffice.dao.DirectionDao;
import com.jayton.admissionoffice.dao.exception.DAOException;
import com.jayton.admissionoffice.model.to.Application;
import com.jayton.admissionoffice.model.to.Status;
import com.jayton.admissionoffice.model.university.Direction;
import com.jayton.admissionoffice.service.DirectionService;
import com.jayton.admissionoffice.service.exception.ServiceException;
import com.jayton.admissionoffice.util.di.Injected;

import java.util.*;
import java.util.stream.Collectors;

public class ApplicationHandler {

    @Injected
    private DirectionService directionService;
    @Injected
    private ApplicationDao applicationDao;

    private final Comparator<Application> dateComparator =
            (app1, app2) -> app1.getCreationTime().compareTo(app2.getCreationTime());
    private final Comparator<Application> markComparator =
            (app1, app2) -> app1.getMark().compareTo(app2.getMark());

    private Map<Long, Direction> directions;

    public ApplicationHandler() {
    }

    public void handleApplications() throws ServiceException {
        List<Application> applications;
        try {
            directions = directionService.getAll().stream().collect(Collectors.toMap(Direction::getId, direction -> direction));
            applications = applicationDao.getAll();
        } catch (DAOException e) {
            throw new ServiceException("Failed to handle applications.", e);
        }

        Map<Long, PriorityQueue<Application>> userApplications = convertToUserQueue(applications);
        Map<Long, PriorityQueue<Application>> acceptedApplications = distributeInDirections(userApplications);

        List<Application> applicationsToUpdate = new ArrayList<>();
        acceptedApplications.forEach((id, queue) -> applicationsToUpdate.addAll(queue));

        try {
            applicationDao.updateAll(applicationsToUpdate, Status.APPROVED);
        } catch (DAOException e) {
            throw new ServiceException("Failed to save results.", e);
        }
    }

    public Map<Long, PriorityQueue<Application>> distributeInDirections(Map<Long, PriorityQueue<Application>> apps) {
        Map<Long, PriorityQueue<Application>> destinationApplications = new HashMap<>();
        for(Map.Entry<Long, PriorityQueue<Application>> pair: apps.entrySet()) {
            addFirstApplication(pair.getKey(), apps, destinationApplications);
        }
        return destinationApplications;
    }

    private void addFirstApplication(Long userId, Map<Long, PriorityQueue<Application>> sourceApplications,
                                     Map<Long, PriorityQueue<Application>> destinationApplications) {
        PriorityQueue<Application> userQueue = sourceApplications.get(userId);
        if(userQueue != null && !userQueue.isEmpty()) {
            Application firstApplication = userQueue.poll();
            PriorityQueue<Application> directionQueue = destinationApplications.get(firstApplication.getDirectionId());
            if(directionQueue == null) {
                directionQueue = new PriorityQueue<>(directions.get(firstApplication.getDirectionId())
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

    private Map<Long, PriorityQueue<Application>> convertToUserQueue(List<Application> applications) {
        Map<Long, PriorityQueue<Application>> result = new HashMap<>();

        for(ListIterator<Application> iterator = applications.listIterator(); iterator.hasNext(); ) {
            Application first = iterator.next();
            PriorityQueue<Application> queue = new PriorityQueue<>(5, dateComparator);
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