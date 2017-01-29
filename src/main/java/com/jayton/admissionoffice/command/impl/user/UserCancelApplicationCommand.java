package com.jayton.admissionoffice.command.impl.user;

import com.jayton.admissionoffice.command.Command;
import com.jayton.admissionoffice.command.exception.ShownException;
import com.jayton.admissionoffice.command.exception.VerificationException;
import com.jayton.admissionoffice.command.util.Verifier;
import com.jayton.admissionoffice.model.to.Application;
import com.jayton.admissionoffice.model.to.SessionTerms;
import com.jayton.admissionoffice.model.user.User;
import com.jayton.admissionoffice.service.ApplicationService;
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

public class UserCancelApplicationCommand implements Command {

    @Injected
    private UtilService utilService;
    @Injected
    private ApplicationService applicationService;

    private final Logger logger = LoggerFactory.getLogger(UserCancelApplicationCommand.class);

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        try {
            HttpSession session = request.getSession();
            User logged = (User) session.getAttribute(PARAM_NAMES.getString("user"));

            Long id = Verifier.convertToLong(request.getParameter(PARAM_NAMES.getString("id")));
            Verifier.verifyId(id);

            Application removed = applicationService.get(id);
            if(removed.getUserId() != logged.getId()) {
                throw new VerificationException("Access denied");
            }
            applicationService.delete(id);

            response.sendRedirect(PAGE_NAMES.getString("controller.get_user")+"&id="+logged.getId());
        } catch (ServiceException e) {
            logger.error("Exception is caught.", e);
            request.setAttribute(PARAM_NAMES.getString("exception"), e);
            request.getRequestDispatcher(PAGE_NAMES.getString("page.exception")).forward(request, response);
        } catch (VerificationException e) {
            logger.error("Requested resource does not exist.", e);
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }
}