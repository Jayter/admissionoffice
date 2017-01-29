package com.jayton.admissionoffice.command.impl.user;

import com.jayton.admissionoffice.command.Command;
import com.jayton.admissionoffice.command.exception.ShownException;
import com.jayton.admissionoffice.command.exception.VerificationException;
import com.jayton.admissionoffice.command.util.Verifier;
import com.jayton.admissionoffice.model.user.User;
import com.jayton.admissionoffice.service.ApplicationService;
import com.jayton.admissionoffice.service.UtilService;
import com.jayton.admissionoffice.service.exception.ServiceException;
import com.jayton.admissionoffice.service.exception.ServiceVerificationException;
import com.jayton.admissionoffice.util.di.Injected;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.time.LocalDateTime;

public class UserApplyCommand implements Command {

    @Injected
    private UtilService utilService;
    @Injected
    private ApplicationService applicationService;

    private final Logger logger = LoggerFactory.getLogger(UserApplyCommand.class);

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute(PARAM_NAMES.getString("user"));

        String referer = getReferer(request);
        try {
            LocalDateTime current = LocalDateTime.now();
            Long directionId = Verifier.convertToLong(request.getParameter(PARAM_NAMES.getString("directionId")));

            applicationService.add(user, directionId, current);

            response.sendRedirect(referer);
        }  catch (ServiceVerificationException e) {
            logger.error("User application failed.", e);
            request.setAttribute(PARAM_NAMES.getString("shownException"), new ShownException(e.getMessage()));
            request.setAttribute(PARAM_NAMES.getString("redirectPath"), referer);

            String relativeReferer = getRelativeReferer(request);
            request.getRequestDispatcher(relativeReferer).forward(request, response);
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