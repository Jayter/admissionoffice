package com.jayton.admissionoffice.controller;

import com.jayton.admissionoffice.command.Command;
import com.jayton.admissionoffice.util.di.BeanContext;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class Controller extends HttpServlet {

    private static final String COMMAND = "command";

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String commandName = request.getParameter(COMMAND);
        BeanContext context = (BeanContext) getServletContext().getAttribute("beanContext");
        Command command = (Command) context.getBean(commandName);
        if(command == null) {
            request.setAttribute("exception", new IllegalArgumentException("Not found."));
            request.getRequestDispatcher("error.jsp").forward(request, response);
        } else {
            try {
                command.execute(request, response);
            } catch (IOException e) {
                request.getRequestDispatcher("error.jsp").forward(request, response);
            }
        }
    }
}