package com.jayton.admissionoffice.util.proxy;

import javax.servlet.http.HttpServletRequest;

public class HttpServletRequestProxy {

    private final HttpServletRequest request;

    public HttpServletRequestProxy(HttpServletRequest request) {
        this.request = request;
    }

    public String getParameter(String name) {
        return request.getParameter(name);
    }

    public Object getAttribute(String name) {
        return request.getAttribute(name);
    }

    public void setAttribute(String name, Object object) {
        request.setAttribute(name, object);
    }

    public HttpSessionProxy getSession() {
        return new HttpSessionProxy(request.getSession());
    }
}