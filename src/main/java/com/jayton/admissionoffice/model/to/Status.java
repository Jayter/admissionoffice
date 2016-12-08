package com.jayton.admissionoffice.model.to;

/**
 * Created by Jayton on 30.11.2016.
 */
public enum Status {
    CREATED,
    APPROVED;

    public static Status getByOrdinal(int key) {
        switch (key) {
            case 0:
                return CREATED;
            case 1:
                return APPROVED;
            default:
                throw new IllegalArgumentException("There is no status available by key " + key);
        }
    }
}
