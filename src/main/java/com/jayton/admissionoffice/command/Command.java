package com.jayton.admissionoffice.command;

import com.jayton.admissionoffice.util.proxy.HttpServletRequestProxy;

import java.util.ResourceBundle;

@FunctionalInterface
public interface Command {

    ResourceBundle PARAM_NAMES = ResourceBundle.getBundle("web.parameterAndAttributeNames");
    ResourceBundle PAGE_NAMES = ResourceBundle.getBundle("web.pageNames");

    String execute(HttpServletRequestProxy request);
}