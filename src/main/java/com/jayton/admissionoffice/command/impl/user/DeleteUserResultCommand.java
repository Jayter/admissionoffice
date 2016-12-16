package com.jayton.admissionoffice.command.impl.user;

import com.jayton.admissionoffice.command.Command;
import com.jayton.admissionoffice.command.exception.VerificationException;
import com.jayton.admissionoffice.command.util.Verifier;
import com.jayton.admissionoffice.service.ServiceFactory;
import com.jayton.admissionoffice.service.UserService;
import com.jayton.admissionoffice.service.exception.ServiceException;
import com.jayton.admissionoffice.util.proxy.HttpServletRequestProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DeleteUserResultCommand implements Command {

    private final Logger logger = LoggerFactory.getLogger(DeleteUserResultCommand.class);

    @Override
    public String execute(HttpServletRequestProxy request) {
        try {
            Long userId = Long.parseLong(request.getParameter(PARAM_NAMES.getString("id")));
            Long subjectId = Long.parseLong(request.getParameter(PARAM_NAMES.getString("subjectId")));
            Verifier.verifyIds(userId, subjectId);

            UserService userService = ServiceFactory.getInstance().getUserService();
            userService.deleteResult(userId, subjectId);

            return PAGE_NAMES.getString("controller.get_user")+"&id="+userId;
        } catch (ServiceException | VerificationException | NumberFormatException e) {
            logger.error("Exception is caught.", e);
            request.setAttribute(PARAM_NAMES.getString("exception"), e);
            return PAGE_NAMES.getString("page.exception");
        }
    }
}