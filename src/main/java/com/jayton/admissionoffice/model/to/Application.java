package com.jayton.admissionoffice.model.to;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Created by Jayton on 30.11.2016.
 */
public class Application {
    private Long id;
    private Long userId;
    private Long directionId;
    private LocalDateTime creationTime;
    private BigDecimal mark;
    private Status status;

    public Application(Long userId, Long directionId, BigDecimal mark) {
        this.userId = userId;
        this.directionId = directionId;
        this.mark = mark;
    }

    public Application(Long id, Long userId, Long directionId, LocalDateTime created, Status status, BigDecimal mark) {
        this.id = id;
        this.userId = userId;
        this.directionId = directionId;
        this.creationTime = created;
        this.status = status;
        this.mark = mark;
    }

    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public Long getDirectionId() {
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

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (userId != null ? !userId.equals(that.userId) : that.userId != null) return false;
        if (directionId != null ? !directionId.equals(that.directionId) : that.directionId != null) return false;
        if (creationTime != null ? !creationTime.equals(that.creationTime) : that.creationTime != null) return false;
        if (mark != null ? !(mark.compareTo(that.mark) == 0) : that.mark != null) return false;
        return status == that.status;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (userId != null ? userId.hashCode() : 0);
        result = 31 * result + (directionId != null ? directionId.hashCode() : 0);
        result = 31 * result + (creationTime != null ? creationTime.hashCode() : 0);
        result = 31 * result + (mark != null ? mark.intValue() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        return result;
    }
}
