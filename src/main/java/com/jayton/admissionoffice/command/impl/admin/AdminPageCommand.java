package com.jayton.admissionoffice.command.impl.admin;

import com.jayton.admissionoffice.command.Command;
import com.jayton.admissionoffice.util.proxy.HttpServletRequestProxy;

public class AdminPageCommand implements Command {

    @Override
    public String execute(HttpServletRequestProxy request) {
        return PAGE_NAMES.getString("page.admin");
    }
}