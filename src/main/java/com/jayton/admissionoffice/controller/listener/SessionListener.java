package com.jayton.admissionoffice.controller.listener;

import com.jayton.admissionoffice.model.user.User;
import com.jayton.admissionoffice.util.di.BeanContext;
import com.jayton.admissionoffice.util.lock.Synchronizer;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

public class SessionListener implements HttpSessionListener {

    @Override
    public void sessionCreated(HttpSessionEvent se) {
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        HttpSession session = se.getSession();
        Boolean isAuthorizedUser = (Boolean) session.getAttribute("isAuthorizedUser");
        if(isAuthorizedUser != null && isAuthorizedUser) {
            User user = (User) session.getAttribute("user");
            Long id = user.getId();

            ServletContext servletContext = session.getServletContext();
            BeanContext beanContext = (BeanContext) servletContext.getAttribute("beanContext");
            Synchronizer synchronizer = (Synchronizer) beanContext.getBean("synchronizer");

            synchronizer.invalidateLock(id);
        }
    }
}