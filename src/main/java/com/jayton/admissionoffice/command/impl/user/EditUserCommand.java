package com.jayton.admissionoffice.command.impl.user;

import com.jayton.admissionoffice.command.Command;
import com.jayton.admissionoffice.command.exception.VerificationException;
import com.jayton.admissionoffice.command.util.Verifier;
import com.jayton.admissionoffice.model.user.User;
import com.jayton.admissionoffice.service.ServiceFactory;
import com.jayton.admissionoffice.service.UserService;
import com.jayton.admissionoffice.service.exception.ServiceException;
import com.jayton.admissionoffice.util.proxy.HttpServletRequestProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EditUserCommand implements Command {

    private final Logger logger = LoggerFactory.getLogger(EditUserCommand.class);

    @Override
    public String execute(HttpServletRequestProxy request) {
        try {
            String id = request.getParameter(PARAM_NAMES.getString("id"));

            if(id != null && !id.isEmpty()) {
                Long userId = Long.parseLong(id);
                Verifier.verifyId(userId);

                UserService service = ServiceFactory.getInstance().getUserService();
                User user = service.get(userId);

                request.setAttribute(PARAM_NAMES.getString("id"), user.getId());
                request.setAttribute(PARAM_NAMES.getString("name"), user.getName());
                request.setAttribute(PARAM_NAMES.getString("lastName"), user.getLastName());
                request.setAttribute(PARAM_NAMES.getString("address"), user.getAddress());
                request.setAttribute(PARAM_NAMES.getString("email"), user.getEmail());
                request.setAttribute(PARAM_NAMES.getString("phone"), user.getPhoneNumber());
                request.setAttribute(PARAM_NAMES.getString("birthDate"), user.getBirthDate());
                request.setAttribute(PARAM_NAMES.getString("mark"), user.getAverageMark());
            }

            return PAGE_NAMES.getString("page.user.edit");
        } catch (ServiceException | VerificationException | NumberFormatException e) {
            logger.error("Exception is caught.", e);
            request.setAttribute(PARAM_NAMES.getString("exception"), e);
            return PAGE_NAMES.getString("page.exception");
        }
    }
}