package util;

import com.jayton.admissionoffice.util.di.BeanContextHolder;
import com.jayton.admissionoffice.util.di.XmlBeanContext;
import com.jayton.admissionoffice.util.di.exception.InjectionException;

/**
 * Initializes {@link com.jayton.admissionoffice.util.di.BeanContext}.
 */
public class ContextInitializationHelper {

    private static ContextInitializationHelper instance = new ContextInitializationHelper();

    private boolean isContextInitialized = false;

    private ContextInitializationHelper() {
    }

    public static ContextInitializationHelper getInstance() {
        return instance;
    }

    public void initContext(String path) throws InjectionException {
        if(!isContextInitialized) {
            XmlBeanContext xmlBeanContext = new XmlBeanContext(path);
            xmlBeanContext.init();
            BeanContextHolder.getInstance().init(xmlBeanContext);
            isContextInitialized = true;
        }
    }
}