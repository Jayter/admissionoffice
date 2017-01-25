package com.jayton.admissionoffice.util.di;

/**
 * Provides access to BeanContext of an application.
 */
public class BeanFactory {

    private static BeanFactory instance;

    private BeanContext actualContext;

    private BeanFactory() {
    }

    public BeanContext getActualContext() {
        return actualContext;
    }

    public static BeanFactory getInstance() {
        if(instance == null) {
            instance = new BeanFactory();
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