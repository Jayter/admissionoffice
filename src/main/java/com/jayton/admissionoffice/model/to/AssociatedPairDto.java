package com.jayton.admissionoffice.model.to;

/**
 * Stores info about associated pairs of entries retrieved from db.
 * <p/>
 * Used to transfer retrieved maps from dao to service layer. Helps
 * to avoid data processing in dao layer.
 * @param <T> - type of the id of entry
 * @param <K> - key of the map
 * @param <V> - value of the map
 */
public class AssociatedPairDto<T, K, V> {
    private T ownerId;
    private K key;
    private V value;

    public AssociatedPairDto(T ownerId, K key, V value) {
        this.ownerId = ownerId;
        this.key = key;
        this.value = value;
    }

    public T getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(T ownerId) {
        this.ownerId = ownerId;
    }

    public K getKey() {
        return key;
    }

    public void setKey(K key) {
        this.key = key;
    }

    public V getValue() {
        return value;
    }

    public void setValue(V value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AssociatedPairDto<?, ?, ?> that = (AssociatedPairDto<?, ?, ?>) o;

        if (ownerId != null ? !ownerId.equals(that.ownerId) : that.ownerId != null) return false;
        if (key != null ? !key.equals(that.key) : that.key != null) return false;
        return value != null ? value.equals(that.value) : that.value == null;

    }

    @Override
    public int hashCode() {
        int result = ownerId != null ? ownerId.hashCode() : 0;
        result = 31 * result + (key != null ? key.hashCode() : 0);
        result = 31 * result + (value != null ? value.hashCode() : 0);
        return result;
    }
}