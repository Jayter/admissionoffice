package com.jayton.admissionoffice.command.impl.user;

import com.jayton.admissionoffice.command.Command;
import com.jayton.admissionoffice.command.exception.VerificationException;
import com.jayton.admissionoffice.command.util.Verifier;
import com.jayton.admissionoffice.model.to.Application;
import com.jayton.admissionoffice.model.user.User;
import com.jayton.admissionoffice.service.*;
import com.jayton.admissionoffice.service.exception.ServiceException;
import com.jayton.admissionoffice.util.proxy.HttpServletRequestProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

public class GetUserCommand implements Command {

    private final Logger logger = LoggerFactory.getLogger(GetUserCommand.class);

    @Override
    public String execute(HttpServletRequestProxy request) {
        try {
            Long id = Long.parseLong(request.getParameter(PARAM_NAMES.getString("id")));
            Verifier.verifyId(id);

            UserService userService = ServiceFactory.getInstance().getUserService();
            ApplicationService applicationService = ServiceFactory.getInstance().getApplicationService();

            User user = userService.get(id);
            List<Application> applications = applicationService.getByUser(id);
            Map<Long, String> directionNames = userService.getDirectionNames(id);

            request.setAttribute(PARAM_NAMES.getString("user"), user);
            request.setAttribute(PARAM_NAMES.getString("applications"), applications);
            request.setAttribute(PARAM_NAMES.getString("directionNames"), directionNames);

            return PAGE_NAMES.getString("page.user");
        } catch (ServiceException | VerificationException | NumberFormatException e) {
            logger.error("Exception is caught.", e);
            request.setAttribute(PARAM_NAMES.getString("exception"), e);
            return PAGE_NAMES.getString("page.exception");
        }
    }
}