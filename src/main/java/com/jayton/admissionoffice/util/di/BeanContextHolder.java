package com.jayton.admissionoffice.util.di;

/**
 * Provides access to BeanContext of an application.
 */
public class BeanContextHolder {

    private static BeanContextHolder instance;

    private BeanContext actualContext;

    private BeanContextHolder() {
    }

    public BeanContext getActualContext() {
        return actualContext;
    }

    public static BeanContextHolder getInstance() {
        if(instance == null) {
            instance = new BeanContextHolder();
        }
        return instance;
    }

    /**
     * Initializes {@link #actualContext} - an actual context of an application.
     * <p/>
     * This method can be called only once. All other calls are ignored.
     * @param context - initialized context of the application
     */
    public void init(BeanContext context) {
        if(actualContext != null) {
            actualContext = context;
        }
    }
}