package com.jayton.admissionoffice.command.impl.admin;

import com.jayton.admissionoffice.command.Command;
import com.jayton.admissionoffice.command.exception.ShownException;
import com.jayton.admissionoffice.model.to.SessionTerms;
import com.jayton.admissionoffice.service.UtilService;
import com.jayton.admissionoffice.service.exception.ServiceException;
import com.jayton.admissionoffice.util.di.Injected;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;

public class HandleApplicationsCommand implements Command {

    @Injected
    private UtilService utilService;

    private final Logger logger = LoggerFactory.getLogger(HandleApplicationsCommand.class);

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        try {
            LocalDateTime current = LocalDateTime.now();
            SessionTerms terms = utilService.getSessionTerms((short)current.getYear());

            LocalDateTime sessionEnd = terms.getSessionEnd();

            if(current.isBefore(sessionEnd)) {
                logger.error("Failed to count handle results.");
                request.setAttribute(PARAM_NAMES.getString("shownException"),
                        new ShownException("Can not count results unless admission session expires."));
            } else {
                utilService.handleApplications();
            }

            response.sendRedirect(PAGE_NAMES.getString("controller.admin_page"));
        } catch (ServiceException e) {
            logger.error("Exception is caught.", e);
            request.setAttribute(PARAM_NAMES.getString("exception"), e);
            request.getRequestDispatcher(PAGE_NAMES.getString("page.exception")).forward(request, response);
        }
    }
}