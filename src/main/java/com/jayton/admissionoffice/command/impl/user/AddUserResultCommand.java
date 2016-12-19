package com.jayton.admissionoffice.command.impl.user;

import com.jayton.admissionoffice.command.Command;
import com.jayton.admissionoffice.command.exception.ShownException;
import com.jayton.admissionoffice.command.exception.VerificationException;
import com.jayton.admissionoffice.command.util.Verifier;
import com.jayton.admissionoffice.service.ServiceFactory;
import com.jayton.admissionoffice.service.UserService;
import com.jayton.admissionoffice.service.exception.ServiceException;
import com.jayton.admissionoffice.util.proxy.HttpServletRequestProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AddUserResultCommand implements Command {

    private final Logger logger = LoggerFactory.getLogger(AddUserResultCommand.class);

    @Override
    public String execute(HttpServletRequestProxy request) {
        Long userId = null;
        try {
            userId = Long.parseLong(request.getParameter(PARAM_NAMES.getString("id")));
            Long subjectId = Long.parseLong(request.getParameter(PARAM_NAMES.getString("subjectId")));
            Verifier.verifyIds(userId, subjectId);

            Short mark;
            try {
                mark = Short.parseShort(request.getParameter(PARAM_NAMES.getString("mark")));
            } catch (NumberFormatException e ) {
                logger.error("Incorrect number.", e);
                request.setAttribute(PARAM_NAMES.getString("shownException"), new ShownException("Incorrect result."));
                return PAGE_NAMES.getString("controller.edit_user")+"&id="+userId;
            }
            Verifier.verifyResult(mark);

            UserService userService = ServiceFactory.getInstance().getUserService();
            userService.addResult(userId, subjectId, mark);

            return PAGE_NAMES.getString("controller.get_user")+"&id="+userId;
        } catch (VerificationException e) {
            logger.error("Incorrect data.", e);
            request.setAttribute(PARAM_NAMES.getString("shownException"), new ShownException(e.getMessage()));
            return PAGE_NAMES.getString("controller.edit_user")+"&id="+userId;
        }
        catch (ServiceException | NumberFormatException e) {
            logger.error("Exception is caught.", e);
            request.setAttribute(PARAM_NAMES.getString("exception"), e);
            return PAGE_NAMES.getString("page.exception");
        }
    }
}