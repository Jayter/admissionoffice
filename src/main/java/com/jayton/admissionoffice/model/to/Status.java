package com.jayton.admissionoffice.model.to;

/**
 * Created by Jayton on 30.11.2016.
 */
public enum Status {
    CREATED("Created"),
    APPROVED("Approved"),
    REJECTED("Rejected");
    private String value;

    Status(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static Status getByValue(String s) {
        if("Created".equals(s)) {
            return CREATED;
        }
        if("Approved".equals(s)) {
            return APPROVED;
        }
        if("Rejected".equals(s)) {
            return REJECTED;
        }
        throw new IllegalArgumentException("There is no status available by key " + s);
    }
}
