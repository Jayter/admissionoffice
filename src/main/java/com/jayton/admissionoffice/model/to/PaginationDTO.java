package com.jayton.admissionoffice.model.to;

import java.util.List;

/**
 * Stores info about entries.
 * <p/>
 * Used to transfer data such as list of items and total count of them.
 * This data is mostly used for pagination.
 */
public class PaginationDTO<T> {
    private List<T> entries;
    private long count;

    public PaginationDTO(List<T> entries, long count) {
        this.entries = entries;
        this.count = count;
    }

    public List<T> getEntries() {
        return entries;
    }

    public void setEntries(List<T> entries) {
        this.entries = entries;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }
}