package com.jayton.admissionoffice.util.di;

/**
 * Container for components(beans) of the application.
 *
 * Initializes beans and provides access to them.
 */
public interface BeanContext {
    Object getBean(String name);
}