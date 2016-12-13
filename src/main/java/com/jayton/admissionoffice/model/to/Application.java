package com.jayton.admissionoffice.model.to;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Application {
    private long id;
    private long userId;
    private long directionId;
    private LocalDateTime creationTime;
    private BigDecimal mark;
    private Status status;

    public Application(long userId, long directionId, LocalDateTime creationTime, BigDecimal mark) {
        this.userId = userId;
        this.directionId = directionId;
        this.creationTime = creationTime;
        this.mark = mark;
    }

    public Application(long id, long userId, long directionId, LocalDateTime created, Status status, BigDecimal mark) {
        this.id = id;
        this.userId = userId;
        this.directionId = directionId;
        this.creationTime = created;
        this.status = status;
        this.mark = mark;
    }

    public long getId() {
        return id;
    }

    public long getUserId() {
        return userId;
    }

    public long getDirectionId() {
        return directionId;
    }

    public LocalDateTime getCreationTime() {
        return creationTime;
    }

    public Status getStatus() {
        return status;
    }

    public BigDecimal getMark() {return mark;}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Application that = (Application) o;

        if (directionId != that.directionId) return false;
        if (creationTime != null ? !creationTime.equals(that.creationTime) : that.creationTime != null) return false;
        if (mark != null ? !(mark.compareTo(that.mark) == 0) : that.mark != null) return false;
        return status == that.status;
    }

    @Override
    public int hashCode() {
        int result = (int) (userId ^ (userId >>> 32));
        result = 31 * result + (int) (directionId ^ (directionId >>> 32));
        result = 31 * result + (creationTime != null ? creationTime.hashCode() : 0);
        result = 31 * result + (mark != null ? mark.intValue() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        return result;
    }
}