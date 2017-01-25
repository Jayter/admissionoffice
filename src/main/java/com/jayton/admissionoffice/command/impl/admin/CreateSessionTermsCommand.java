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

public class CreateSessionTermsCommand implements Command {

    @Injected
    private UtilService utilService;

    private final Logger logger = LoggerFactory.getLogger(UpdateSessionTermsCommand.class);

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        LocalDateTime current = LocalDateTime.now();
        short year = (short)current.getYear();
        try {
            LocalDateTime createdSessionStart = Verifier.convertToLocalDateTime(request.getParameter(PARAM_NAMES.getString("sessionStart")));
            LocalDateTime createdSessionEnd = Verifier.convertToLocalDateTime(request.getParameter(PARAM_NAMES.getString("sessionEnd")));

            if(createdSessionEnd.isBefore(createdSessionStart) || createdSessionEnd.equals(createdSessionStart)
                    || createdSessionStart.isBefore(current)) {
                logger.error("Failed to update session terms.");
                throw new VerificationException("Incorrect dates.");
            }

            SessionTerms updatedSessionTerms = new SessionTerms(year, createdSessionStart, createdSessionEnd);
            utilService.createSessionTerms(updatedSessionTerms);

            HttpSession session = request.getSession();
            session.setAttribute(PARAM_NAMES.getString("sessionTerms"), updatedSessionTerms);

            response.sendRedirect(PAGE_NAMES.getString("controller.admin_page"));
        } catch (VerificationException e) {
            logger.error("Incorrect data.", e);
            addAttributes(request, e.getMessage(), year);
            request.getRequestDispatcher(PAGE_NAMES.getString("page.session_terms.edit")).forward(request, response);
        } catch (ServiceException e) {
            logger.error("Exception is caught.", e);
            request.setAttribute(PARAM_NAMES.getString("exception"), e);
            request.getRequestDispatcher(PAGE_NAMES.getString("page.exception")).forward(request, response);
        }
    }

    private void addAttributes(HttpServletRequest request, String exceptionMessage, short year) {
        request.setAttribute(PARAM_NAMES.getString("isNew"), true);
        request.setAttribute(PARAM_NAMES.getString("year"), year);
        request.setAttribute(PARAM_NAMES.getString("sessionStart"), request.getParameter(PARAM_NAMES.getString("sessionStart")));
        request.setAttribute(PARAM_NAMES.getString("sessionEnd"), request.getParameter(PARAM_NAMES.getString("sessionEnd")));
        request.setAttribute(PARAM_NAMES.getString("shownException"), new ShownException(exceptionMessage));
    }
}