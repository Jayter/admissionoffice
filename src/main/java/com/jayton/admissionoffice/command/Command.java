package com.jayton.admissionoffice.command;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ResourceBundle;

@FunctionalInterface
public interface Command {

    ResourceBundle PARAM_NAMES = ResourceBundle.getBundle("web.parameterAndAttributeNames");
    ResourceBundle PAGE_NAMES = ResourceBundle.getBundle("web.pageNames");

    void execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException;

    default String getRelativeReferer(HttpServletRequest request) {
        String referer = getReferer(request);
        String context = request.getContextPath();
        String[] uriParts = referer.split(context);
        return uriParts[1];
    }

    default String getReferer(HttpServletRequest request) {
        return request.getHeader("Referer");
    }
}