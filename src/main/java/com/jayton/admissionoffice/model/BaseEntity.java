package com.jayton.admissionoffice.model;

import java.io.Serializable;

public class BaseEntity implements Serializable {
    private Long id;
    protected String name;

    public BaseEntity() {
    }

    public BaseEntity(String name) {
        this.name = name;
    }

    public BaseEntity(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isNew() {
        return this.id == null;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
