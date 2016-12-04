package com.jayton.admissionoffice.controller;

import com.jayton.admissionoffice.command.Command;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Jayton on 28.11.2016.
 */
public class Controller extends HttpServlet {

    private static final String COMMAND = "command";
    private static final String ERROR_PAGE = "WEB_INF/jsp/404.jsp";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String commandName = req.getParameter(COMMAND);
        Command command = CommandHelper.getInstance().getCommand(commandName);
        if(command == null) {
            req.getRequestDispatcher(ERROR_PAGE).forward(req, resp);
        } else {
            command.execute(req, resp);
        }
    }
}
