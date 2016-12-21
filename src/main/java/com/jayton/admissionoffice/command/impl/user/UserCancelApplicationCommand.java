package com.jayton.admissionoffice.command.impl.user;

import com.jayton.admissionoffice.command.Command;
import com.jayton.admissionoffice.command.exception.ShownException;
import com.jayton.admissionoffice.command.exception.VerificationException;
import com.jayton.admissionoffice.command.util.Verifier;
import com.jayton.admissionoffice.model.to.SessionTerms;
import com.jayton.admissionoffice.model.user.User;
import com.jayton.admissionoffice.service.ApplicationService;
import com.jayton.admissionoffice.service.ServiceFactory;
import com.jayton.admissionoffice.service.UtilService;
import com.jayton.admissionoffice.service.exception.ServiceException;
import com.jayton.admissionoffice.util.proxy.HttpServletRequestProxy;
import com.jayton.admissionoffice.util.proxy.HttpSessionProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;

public class UserCancelApplicationCommand implements Command {

    private final Logger logger = LoggerFactory.getLogger(UserCancelApplicationCommand.class);

    @Override
    public String execute(HttpServletRequestProxy request) {
        try {
            HttpSessionProxy session = request.getSession();
            User logged = (User) session.getAttribute(PARAM_NAMES.getString("user"));

            LocalDateTime current = LocalDateTime.now();
            UtilService utilService = ServiceFactory.getInstance().getUtilService();

            SessionTerms sessionTerms = utilService.getSessionTerms((short)current.getYear());
            if(current.isAfter(sessionTerms.getSessionEnd()) || current.isBefore(sessionTerms.getSessionStart())) {
                logger.error("Failed to cancel application.");
                request.setAttribute(PARAM_NAMES.getString("shownException"),
                        new ShownException("Can not cancel application after session term expiration."));
                return PAGE_NAMES.getString("controller.get_user")+"&id="+logged.getId();
            }

            Long id = Long.parseLong(request.getParameter(PARAM_NAMES.getString("id")));
            Verifier.verifyId(id);

            ApplicationService service = ServiceFactory.getInstance().getApplicationService();

            service.delete(id);

            return PAGE_NAMES.getString("controller.get_user")+"&id="+logged.getId();
        } catch (ServiceException | VerificationException | NumberFormatException e) {
            logger.error("Exception is caught.", e);
            request.setAttribute(PARAM_NAMES.getString("exception"), e);
            return PAGE_NAMES.getString("page.exception");
        }
    }
}