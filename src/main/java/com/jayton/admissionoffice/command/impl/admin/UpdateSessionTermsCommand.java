package com.jayton.admissionoffice.command.impl.admin;

import com.jayton.admissionoffice.command.Command;
import com.jayton.admissionoffice.command.exception.ShownException;
import com.jayton.admissionoffice.command.exception.VerificationException;
import com.jayton.admissionoffice.command.util.Verifier;
import com.jayton.admissionoffice.model.to.SessionTerms;
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
import java.time.LocalDateTime;

public class UpdateSessionTermsCommand implements Command {

    @Injected
    private UtilService utilService;

    private final Logger logger = LoggerFactory.getLogger(UpdateSessionTermsCommand.class);

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        LocalDateTime current = LocalDateTime.now();
        short year = (short)current.getYear();

        try {
            LocalDateTime updatedSessionStart = Verifier.convertToLocalDateTime(request.getParameter(PARAM_NAMES.getString("sessionStart")));
            LocalDateTime updatedSessionEnd = Verifier.convertToLocalDateTime(request.getParameter(PARAM_NAMES.getString("sessionEnd")));

            SessionTerms currentTerms = utilService.getSessionTerms(year);
            LocalDateTime sessionStart = currentTerms.getSessionStart();

            if(current.isAfter(sessionStart)) {
                logger.error("Failed to update session terms.");
                request.setAttribute(PARAM_NAMES.getString("shownException"),
                        new ShownException("Can not change session terms after start of admission session."));
                response.sendRedirect(PAGE_NAMES.getString("controller.admin_page"));
            }

            if(updatedSessionEnd.isBefore(updatedSessionStart) || updatedSessionEnd.equals(updatedSessionStart)
                    || updatedSessionStart.isBefore(current)) {
                logger.error("Failed to update session terms.");
                throw new VerificationException("Incorrect dates.");
            }

            SessionTerms updatedSessionTerms = new SessionTerms(year, updatedSessionStart, updatedSessionEnd);
            utilService.updateSessionTerms(updatedSessionTerms);

            HttpSession session = request.getSession();
            session.setAttribute(PARAM_NAMES.getString("sessionTerms"), updatedSessionTerms);

            response.sendRedirect(PAGE_NAMES.getString("controller.admin_page"));
        } catch (VerificationException e) {
            logger.error("Incorrect data.", e);
            request.setAttribute(PARAM_NAMES.getString("shownException"), new ShownException(e.getMessage()));
            request.getRequestDispatcher(PAGE_NAMES.getString("controller.edit_session_terms")).forward(request, response);
        } catch (ServiceException e) {
            logger.error("Exception is caught.", e);
            request.setAttribute(PARAM_NAMES.getString("exception"), e);
            request.getRequestDispatcher(PAGE_NAMES.getString("page.exception")).forward(request, response);
        }
    }
}