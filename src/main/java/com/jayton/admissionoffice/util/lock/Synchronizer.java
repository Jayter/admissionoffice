package com.jayton.admissionoffice.util.lock;

import com.jayton.admissionoffice.util.exception.ApplicationException;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Provides synchronization by key.
 */
public class Synchronizer {

    private Map<Long, Lock> locks = new ConcurrentHashMap<>();

    public Synchronizer() {
    }

    public <T> T executeSynchronously(Long id, NullaryFunction<T> function) throws ApplicationException {
        Lock lock = getLock(id);
        try {
            lock.lock();
            return function.apply();
        } finally {
            lock.unlock();
        }
    }

    private Lock getLock(Long id) {
        Lock lock = locks.get(id);
        if(lock == null) {
            synchronized (this) {
                if(lock == null) {
                    lock = new ReentrantLock();
                    locks.put(id, lock);
                }
            }
        }
        return lock;
    }
}