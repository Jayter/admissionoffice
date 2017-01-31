package com.jayton.admissionoffice.controller;

import com.jayton.admissionoffice.command.Command;
import com.jayton.admissionoffice.util.di.BeanContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class Controller extends HttpServlet {

    private static final String COMMAND = "command";

    private final Logger logger = LoggerFactory.getLogger(Controller.class);

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logger.info("Handling request: {}?{}.", request.getRequestURI(), request.getQueryString());
        String commandName = request.getParameter(COMMAND);
        BeanContext context = (BeanContext) getServletContext().getAttribute("beanContext");
        Command command = (Command) context.getBean(commandName);
        if(command == null) {
            logger.error("Command is not found.");
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        } else {
            try {
                command.execute(request, response);
            } catch (IOException e) {
                logger.error("Exception during command execution.", e);
                request.getRequestDispatcher("error.jsp").forward(request, response);
            }
        }
    }
}