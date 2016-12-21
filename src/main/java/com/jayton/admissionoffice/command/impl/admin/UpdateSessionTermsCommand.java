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

public class UpdateSessionTermsCommand implements Command {

    private final Logger logger = LoggerFactory.getLogger(UpdateSessionTermsCommand.class);

    @Override
    public String execute(HttpServletRequestProxy request) {
        LocalDateTime updatedSessionStart = LocalDateTime.parse(request.getParameter(PARAM_NAMES.getString("sessionStart")));
        LocalDateTime updatedSessionEnd = LocalDateTime.parse(request.getParameter(PARAM_NAMES.getString("sessionEnd")));
        LocalDateTime current = LocalDateTime.now();
        short year = (short)current.getYear();

        try {
            UtilService utilService = ServiceFactory.getInstance().getUtilService();

            SessionTerms currentTerms = utilService.getSessionTerms(year);
            LocalDateTime sessionStart = currentTerms.getSessionStart();

            if(current.isAfter(sessionStart)) {
                logger.error("Failed to update session terms.");
                request.setAttribute(PARAM_NAMES.getString("shownException"),
                        new ShownException("Can not change session terms after start of admission session."));
                return PAGE_NAMES.getString("page.admin");
            }

            if(updatedSessionEnd.isBefore(updatedSessionStart) || updatedSessionEnd.equals(updatedSessionStart)
                    || updatedSessionStart.isBefore(current)) {
                logger.error("Failed to update session terms.");
                throw new VerificationException("Incorrect dates.");
            }

            SessionTerms updatedSessionTerms = new SessionTerms(year, updatedSessionStart, updatedSessionEnd);
            utilService.updateSessionTerms(updatedSessionTerms);

            HttpSessionProxy session = request.getSession();
            session.setAttribute(PARAM_NAMES.getString("sessionTerms"), updatedSessionTerms);

            return PAGE_NAMES.getString("page.admin");
        } catch (VerificationException e) {
            logger.error("Incorrect data.", e);

            request.setAttribute("isNew", false);
            request.setAttribute(PARAM_NAMES.getString("year"), year);
            request.setAttribute(PARAM_NAMES.getString("sessionStart"), updatedSessionStart);
            request.setAttribute(PARAM_NAMES.getString("sessionEnd"), updatedSessionEnd);
            request.setAttribute(PARAM_NAMES.getString("shownException"), new ShownException(e.getMessage()));
            return PAGE_NAMES.getString("page.session_terms.edit");
        } catch (ServiceException e) {
            logger.error("Exception is caught.", e);
            request.setAttribute(PARAM_NAMES.getString("exception"), e);
            return PAGE_NAMES.getString("page.exception");
        }
    }
}