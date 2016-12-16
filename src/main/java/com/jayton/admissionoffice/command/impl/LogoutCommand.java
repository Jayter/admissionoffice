package com.jayton.admissionoffice.command.impl;

import com.jayton.admissionoffice.command.Command;
import com.jayton.admissionoffice.util.proxy.HttpServletRequestProxy;
import com.jayton.admissionoffice.util.proxy.HttpSessionProxy;

public class LogoutCommand implements Command {

    @Override
    public String execute(HttpServletRequestProxy request){
        HttpSessionProxy session = request.getSession();
        session.invalidate();

        return PAGE_NAMES.getString("controller.load_main");
    }
}