package com.jayton.admissionoffice.util.lock;

import java.util.HashMap;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Provides synchronization by key.
 */
public class Synchronizer {

    private HashMap<Long, ReentrantLock> locks = new HashMap<>();

    public Synchronizer() {
    }

    public synchronized void lock(Long key) {
        ReentrantLock lock = locks.get(key);
        if(lock == null) {
            lock = new ReentrantLock();
            locks.put(key, lock);
        }
        lock.lock();
    }

    public void unlock(Long key) {
        ReentrantLock lock = locks.get(key);
        if(lock != null) {
            lock.unlock();
        }
    }

    public synchronized void invalidateLock(Long key) {
        ReentrantLock lock = locks.remove(key);
        if(lock != null) {
            lock.lock();
            lock.unlock();
        }
    }
}