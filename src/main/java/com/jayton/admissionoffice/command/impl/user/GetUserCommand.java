package com.jayton.admissionoffice.command.impl.user;

import com.jayton.admissionoffice.command.Command;
import com.jayton.admissionoffice.command.exception.VerificationException;
import com.jayton.admissionoffice.command.util.Verifier;
import com.jayton.admissionoffice.model.Subject;
import com.jayton.admissionoffice.model.to.Application;
import com.jayton.admissionoffice.model.user.User;
import com.jayton.admissionoffice.service.*;
import com.jayton.admissionoffice.service.exception.ServiceException;
import com.jayton.admissionoffice.util.di.Injected;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetUserCommand implements Command {

    @Injected
    private UserService userService;
    @Injected
    private ApplicationService applicationService;
    @Injected
    private UtilService utilService;

    private final Logger logger = LoggerFactory.getLogger(GetUserCommand.class);

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        try {
            Long id = Verifier.convertToLong(request.getParameter(PARAM_NAMES.getString("id")));
            Verifier.verifyId(id);

            User user = userService.get(id);
            List<Application> applications = applicationService.getByUser(id);
            Map<Long, String> directionNames = userService.getDirectionNames(id);

            request.setAttribute(PARAM_NAMES.getString("user"), user);
            request.setAttribute(PARAM_NAMES.getString("applications"), applications);
            request.setAttribute(PARAM_NAMES.getString("directionNames"), directionNames);

            List<Subject> allSubjects = utilService.getAllSubjects();
            Map<Long, Subject> subjectsMap = new HashMap<>();
            allSubjects.forEach(subject -> subjectsMap.put(subject.getId(), subject));

            request.setAttribute(PARAM_NAMES.getString("subjects"), subjectsMap);

            request.getRequestDispatcher(PAGE_NAMES.getString("page.user")).forward(request, response);
        } catch (ServiceException  e) {
            logger.error("Exception is caught.", e);
            request.setAttribute(PARAM_NAMES.getString("exception"), e);
            request.getRequestDispatcher(PAGE_NAMES.getString("page.exception")).forward(request, response);
        } catch (VerificationException e) {
            logger.error("Requested resource does not exist.", e);
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }
}