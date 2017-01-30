package com.jayton.admissionoffice.command.impl.admin;

import com.jayton.admissionoffice.command.Command;
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

public class EditSessionTermsCommand implements Command {

    @Injected
    private UtilService utilService;

    private final Logger logger = LoggerFactory.getLogger(EditSessionTermsCommand.class);

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        try {
            LocalDateTime current = LocalDateTime.now();
            short currentYear = (short)current.getYear();
            request.setAttribute(PARAM_NAMES.getString("year"), currentYear);

            SessionTerms currentTerms = utilService.getSessionTerms(currentYear);
            if(currentTerms != null) {
                request.setAttribute(PARAM_NAMES.getString("isNew"), false);
                request.setAttribute(PARAM_NAMES.getString("sessionStart"), currentTerms.getSessionStart());
                request.setAttribute(PARAM_NAMES.getString("sessionEnd"), currentTerms.getSessionEnd());
            } else {
                request.setAttribute(PARAM_NAMES.getString("isNew"), true);
            }

            request.getRequestDispatcher(PAGE_NAMES.getString("page.session_terms.edit")).forward(request, response);
        } catch (ServiceException e) {
            logger.error("Exception is caught.", e);
            request.setAttribute(PARAM_NAMES.getString("exception"), e);
            request.getRequestDispatcher(PAGE_NAMES.getString("page.exception")).forward(request, response);
        }
    }
}