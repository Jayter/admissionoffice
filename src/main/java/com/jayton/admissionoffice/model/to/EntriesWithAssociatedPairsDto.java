package com.jayton.admissionoffice.model.to;

import java.util.List;

/**
 * Stores complex data structures retrieved from db.
 * <p/>
 * Used to transfer retrieved list of entities with inner map from dao to service.
 * Helps to avoid data processing in dao layer.
 * @param <T> - type of entities
 * @param <E> - type of entity`s id
 * @param <K> - type of the key of the map
 * @param <V> - type of the value of the map
 */
public class EntriesWithAssociatedPairsDto<T, E, K, V> {
    private List<T> entries;
    private List<AssociatedPairDto<E, K, V>> pairs;

    public EntriesWithAssociatedPairsDto(List<T> entries, List<AssociatedPairDto<E, K, V>> pairs) {
        this.entries = entries;
        this.pairs = pairs;
    }

    public List<T> getEntries() {
        return entries;
    }

    public void setEntries(List<T> entries) {
        this.entries = entries;
    }

    public List<AssociatedPairDto<E, K, V>> getPairs() {
        return pairs;
    }

    public void setPairs(List<AssociatedPairDto<E, K, V>> pairs) {
        this.pairs = pairs;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EntriesWithAssociatedPairsDto<?, ?, ?, ?> that = (EntriesWithAssociatedPairsDto<?, ?, ?, ?>) o;

        if (entries != null ? !entries.equals(that.entries) : that.entries != null) return false;
        return pairs != null ? pairs.equals(that.pairs) : that.pairs == null;

    }

    @Override
    public int hashCode() {
        int result = entries != null ? entries.hashCode() : 0;
        result = 31 * result + (pairs != null ? pairs.hashCode() : 0);
        return result;
    }
}