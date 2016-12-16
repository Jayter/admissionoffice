package com.jayton.admissionoffice.util.proxy;

import javax.servlet.http.HttpSession;

public class HttpSessionProxy {

    private final HttpSession session;

    public HttpSessionProxy(HttpSession session) {
        this.session = session;
    }

    public void setAttribute(String name, Object object) {
        session.setAttribute(name, object);
    }

    public Object getAttribute(String name) {
        return session.getAttribute(name);
    }

    public void invalidate() {
        session.invalidate();
    }
}