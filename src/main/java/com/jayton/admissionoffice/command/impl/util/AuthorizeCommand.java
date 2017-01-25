package com.jayton.admissionoffice.command.impl.util;

import com.jayton.admissionoffice.command.Command;
import com.jayton.admissionoffice.model.Subject;
import com.jayton.admissionoffice.model.to.AuthorizationResult;
import com.jayton.admissionoffice.model.to.SessionTerms;
import com.jayton.admissionoffice.model.user.User;
import com.jayton.admissionoffice.service.UserService;
import com.jayton.admissionoffice.command.exception.ShownException;
import com.jayton.admissionoffice.service.UtilService;
import com.jayton.admissionoffice.service.exception.ServiceException;
import com.jayton.admissionoffice.util.di.Injected;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AuthorizeCommand implements Command {

    @Injected
    private UserService userService;
    @Injected
    private UtilService utilService;

    private final Logger logger = LoggerFactory.getLogger(AuthorizeCommand.class);

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String login = request.getParameter(PARAM_NAMES.getString("login"));
        String password = request.getParameter(PARAM_NAMES.getString("password"));

        HttpSession session = request.getSession();

        try {
            AuthorizationResult result = userService.authorize(login, password);
            boolean isUser = result == AuthorizationResult.USER;
            boolean isAdmin = result == AuthorizationResult.ADMIN;

            if(!(isUser || isAdmin)) {
                logger.error("Incorrect data.");
                request.setAttribute(PARAM_NAMES.getString("shownException"),
                        new ShownException("Incorrect login or password."));
                request.setAttribute(PARAM_NAMES.getString("redirectPath"), getReferer(request));

                String relativeReferer = getRelativeReferer(request);
                request.getRequestDispatcher(relativeReferer).forward(request, response);
            }

            if(isUser) {
                User user = userService.getByEmail(login);
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

            String referer = getReferer(request);
            response.sendRedirect(referer);
        } catch (ServiceException e) {
            logger.error("Failed to load application data.", e);
            request.setAttribute(PARAM_NAMES.getString("exception"), e);
            request.getRequestDispatcher("page.exception").forward(request, response);
        }
    }
}