package com.jayton.admissionoffice.model.to;

public enum Status {
    CREATED,
    APPROVED;

    public static Status getByOrdinal(byte key) {
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
