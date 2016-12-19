package com.jayton.admissionoffice.command.impl.user;

import com.jayton.admissionoffice.command.Command;
import com.jayton.admissionoffice.command.exception.ShownException;
import com.jayton.admissionoffice.model.user.User;
import com.jayton.admissionoffice.service.ApplicationService;
import com.jayton.admissionoffice.service.ServiceFactory;
import com.jayton.admissionoffice.service.exception.ServiceException;
import com.jayton.admissionoffice.service.exception.ServiceVerificationException;
import com.jayton.admissionoffice.util.proxy.HttpServletRequestProxy;
import com.jayton.admissionoffice.util.proxy.HttpSessionProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;

public class UserApplyCommand implements Command {

    private final Logger logger = LoggerFactory.getLogger(UserApplyCommand.class);

    @Override
    public String execute(HttpServletRequestProxy request) {
        HttpSessionProxy session = request.getSession();
        User user = (User) session.getAttribute(PARAM_NAMES.getString("user"));

        try {
            LocalDateTime applied = LocalDateTime.now();

            Long directionId = Long.parseLong(request.getParameter(PARAM_NAMES.getString("directionId")));

            ApplicationService applicationService = ServiceFactory.getInstance().getApplicationService();
            applicationService.add(user, directionId, applied);

            return PAGE_NAMES.getString("controller.get_direction")+"&id="+directionId;
        }  catch (ServiceVerificationException e) {
            logger.error("Incorrect data.", e);
            request.setAttribute(PARAM_NAMES.getString("shownException"), new ShownException(e.getMessage()));
            return PAGE_NAMES.getString("controller.get_user")+"&id="+user.getId();
        }
        catch (ServiceException | NumberFormatException e) {
            logger.error("Exception is caught.", e);
            request.setAttribute(PARAM_NAMES.getString("exception"), e);
            return PAGE_NAMES.getString("page.exception");
        }
    }
}