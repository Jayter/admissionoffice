package com.jayton.admissionoffice.util.lock;

import com.jayton.admissionoffice.util.exception.ApplicationException;

@FunctionalInterface
public interface NullaryFunction<T> {
    T apply() throws ApplicationException;
}
