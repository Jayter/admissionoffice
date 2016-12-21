package com.jayton.admissionoffice.command.impl.admin;

import com.jayton.admissionoffice.command.Command;
import com.jayton.admissionoffice.command.exception.ShownException;
import com.jayton.admissionoffice.command.exception.VerificationException;
import com.jayton.admissionoffice.model.to.SessionTerms;
import com.jayton.admissionoffice.service.ServiceFactory;
import com.jayton.admissionoffice.service.UtilService;
import com.jayton.admissionoffice.service.exception.ServiceException;
import com.jayton.admissionoffice.util.proxy.HttpServletRequestProxy;
import com.jayton.admissionoffice.util.proxy.HttpSessionProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;

public class CreateSessionTermsCommand implements Command {

    private final Logger logger = LoggerFactory.getLogger(UpdateSessionTermsCommand.class);

    @Override
    public String execute(HttpServletRequestProxy request) {
        LocalDateTime createdSessionStart = LocalDateTime.parse(request.getParameter(PARAM_NAMES.getString("sessionStart")));
        LocalDateTime createdSessionEnd = LocalDateTime.parse(request.getParameter(PARAM_NAMES.getString("sessionEnd")));

        LocalDateTime current = LocalDateTime.now();
        short year = (short)current.getYear();

        try {
            UtilService utilService = ServiceFactory.getInstance().getUtilService();

            if(createdSessionEnd.isBefore(createdSessionStart) || createdSessionEnd.equals(createdSessionStart)
                    || createdSessionStart.isBefore(current)) {
                logger.error("Failed to update session terms.");
                throw new VerificationException("Incorrect dates.");
            }

            SessionTerms updatedSessionTerms = new SessionTerms(year, createdSessionStart, createdSessionEnd);
            utilService.createSessionTerms(updatedSessionTerms);

            HttpSessionProxy session = request.getSession();
            session.setAttribute(PARAM_NAMES.getString("sessionTerms"), updatedSessionTerms);

            return PAGE_NAMES.getString("page.admin");
        } catch (VerificationException e) {
            logger.error("Incorrect data.", e);

            request.setAttribute("isNew", true);
            request.setAttribute(PARAM_NAMES.getString("year"), year);
            request.setAttribute(PARAM_NAMES.getString("sessionStart"), createdSessionStart);
            request.setAttribute(PARAM_NAMES.getString("sessionEnd"), createdSessionEnd);
            request.setAttribute(PARAM_NAMES.getString("shownException"), new ShownException(e.getMessage()));
            return PAGE_NAMES.getString("page.session_terms.edit");
        } catch (ServiceException e) {
            logger.error("Exception is caught.", e);
            request.setAttribute(PARAM_NAMES.getString("exception"), e);
            return PAGE_NAMES.getString("page.exception");
        }
    }
}