package com.jayton.admissionoffice.command.impl.admin;

import com.jayton.admissionoffice.command.Command;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AdminPageCommand implements Command {

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        request.getRequestDispatcher(PAGE_NAMES.getString("page.admin")).forward(request, response);
    }
}