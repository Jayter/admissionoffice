package com.jayton.admissionoffice.model.to;

import java.util.List;
import java.util.Map;

/**
 * Stores info about users` applications.
 */
public class ApplicationDTO {
    private List<Application> applications;
    private Map<Long, String> userNames;
    private long count;

    public ApplicationDTO(List<Application> applications, Map<Long, String> userNames, long count) {
        this.applications = applications;
        this.userNames = userNames;
        this.count = count;
    }

    public List<Application> getApplications() {
        return applications;
    }

    public void setApplications(List<Application> applications) {
        this.applications = applications;
    }

    public Map<Long, String> getUserNames() {
        return userNames;
    }

    public void setUserNames(Map<Long, String> userNames) {
        this.userNames = userNames;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }
}