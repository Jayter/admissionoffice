package com.jayton.admissionoffice.command.impl;

import com.jayton.admissionoffice.command.Command;
import com.jayton.admissionoffice.model.Subject;
import com.jayton.admissionoffice.model.to.AuthorizationResult;
import com.jayton.admissionoffice.model.to.SessionTerms;
import com.jayton.admissionoffice.model.user.User;
import com.jayton.admissionoffice.service.ServiceFactory;
import com.jayton.admissionoffice.service.UserService;
import com.jayton.admissionoffice.command.exception.ShownException;
import com.jayton.admissionoffice.service.UtilService;
import com.jayton.admissionoffice.service.exception.ServiceException;
import com.jayton.admissionoffice.util.proxy.HttpServletRequestProxy;
import com.jayton.admissionoffice.util.proxy.HttpSessionProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AuthorizeCommand implements Command {

    private final Logger logger = LoggerFactory.getLogger(AuthorizeCommand.class);

    @Override
    public String execute(HttpServletRequestProxy request) {
        String login = request.getParameter(PARAM_NAMES.getString("login"));
        String password = request.getParameter(PARAM_NAMES.getString("password"));

        UserService service = ServiceFactory.getInstance().getUserService();
        UtilService utilService = ServiceFactory.getInstance().getUtilService();

        HttpSessionProxy session = request.getSession();

        try {
            AuthorizationResult result = service.authorize(login, password);
            boolean isUser = result == AuthorizationResult.USER;
            boolean isAdmin = result == AuthorizationResult.ADMIN;

            if(!(isUser || isAdmin)) {
                logger.error("Incorrect data.");
                request.setAttribute(PARAM_NAMES.getString("shownException"),
                        new ShownException("Incorrect login or password."));
            }

            if(isUser) {
                User user = service.getByEmail(login);
                session.setAttribute(PARAM_NAMES.getString("user"), user);
            }

            session.setAttribute(PARAM_NAMES.getString("isAdmin"), result == AuthorizationResult.ADMIN);
            session.setAttribute(PARAM_NAMES.getString("isUser"), isUser);

            List<Subject> subjects = utilService.getAllSubjects();
            SessionTerms sessionTerms = utilService.getSessionTerms((short) LocalDate.now().getYear());

            Map<Long, Subject> subjectsMap = new HashMap<>();
            subjects.forEach(subject -> subjectsMap.put(subject.getId(), subject));

            session.setAttribute(PARAM_NAMES.getString("subjects"), subjectsMap);
            session.setAttribute(PARAM_NAMES.getString("sessionTerms"), sessionTerms);

            return PAGE_NAMES.getString("controller.load_main");

        } catch (ServiceException e) {
            logger.error("Failed to load application data.", e);
            request.setAttribute(PARAM_NAMES.getString("exception"), e);
            return PAGE_NAMES.getString("page.exception");
        }
    }
}