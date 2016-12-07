package com.jayton.admissionoffice.model;

import java.io.Serializable;

public class NamedEntity implements Serializable {
    private Long id;
    protected String name;

    public NamedEntity() {
        //used only in commands
    }
    public NamedEntity(String name) {
        this.name = name;
    }

    public NamedEntity(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public boolean isNew() {
        return this.id == null;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NamedEntity that = (NamedEntity) o;

        return name != null ? name.equals(that.name) : that.name == null;

    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }
}
