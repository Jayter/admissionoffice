package com.jayton.admissionoffice.command.impl;

import com.jayton.admissionoffice.command.Command;
import com.jayton.admissionoffice.model.to.AuthorizationResult;
import com.jayton.admissionoffice.model.user.User;
import com.jayton.admissionoffice.service.ServiceFactory;
import com.jayton.admissionoffice.service.UserService;
import com.jayton.admissionoffice.command.exception.ShownException;
import com.jayton.admissionoffice.service.exception.ServiceException;
import com.jayton.admissionoffice.util.proxy.HttpServletRequestProxy;
import com.jayton.admissionoffice.util.proxy.HttpSessionProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AuthorizeCommand implements Command {

    private final Logger logger = LoggerFactory.getLogger(AuthorizeCommand.class);

    @Override
    public String execute(HttpServletRequestProxy request) {
        String login = request.getParameter(PARAM_NAMES.getString("login"));
        String password = request.getParameter(PARAM_NAMES.getString("password"));

        UserService service = ServiceFactory.getInstance().getUserService();
        HttpSessionProxy session = request.getSession();

        try {
            AuthorizationResult result = service.authorize(login, password);
            boolean isUser = result == AuthorizationResult.USER;
            boolean isAdmin = result == AuthorizationResult.ADMIN;

            session.setAttribute(PARAM_NAMES.getString("isAdmin"), result == AuthorizationResult.ADMIN);
            session.setAttribute(PARAM_NAMES.getString("isUser"), isUser);

            if(isUser) {
                User user = service.getByEmail(login);
                session.setAttribute(PARAM_NAMES.getString("user"), user);
            }

            if(!(isUser || isAdmin)) {
                logger.error("Incorrect data.");
                request.setAttribute(PARAM_NAMES.getString("shownException"),
                        new ShownException("Incorrect login or password."));
            }
            return PAGE_NAMES.getString("controller.load_main");

        } catch (ServiceException e) {
            logger.error("Exception is caught.", e);
            request.setAttribute(PARAM_NAMES.getString("exception"), e);
            return PAGE_NAMES.getString("page.exception");
        }
    }
}