package com.jayton.admissionoffice.command.impl.user;

import com.jayton.admissionoffice.command.Command;
import com.jayton.admissionoffice.command.exception.VerificationException;
import com.jayton.admissionoffice.command.util.Verifier;
import com.jayton.admissionoffice.model.user.User;
import com.jayton.admissionoffice.service.UserService;
import com.jayton.admissionoffice.service.exception.ServiceException;
import com.jayton.admissionoffice.util.di.Injected;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class EditUserCommand implements Command {

    @Injected
    private UserService userService;

    private final Logger logger = LoggerFactory.getLogger(EditUserCommand.class);

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        try {
            String id = request.getParameter(PARAM_NAMES.getString("id"));

            if(id != null && !id.isEmpty()) {
                Long userId = Verifier.convertToLong(id);
                Verifier.verifyId(userId);

                User user = userService.get(userId);

                request.setAttribute(PARAM_NAMES.getString("id"), user.getId());
                request.setAttribute(PARAM_NAMES.getString("name"), user.getName());
                request.setAttribute(PARAM_NAMES.getString("lastName"), user.getLastName());
                request.setAttribute(PARAM_NAMES.getString("address"), user.getAddress());
                request.setAttribute(PARAM_NAMES.getString("email"), user.getEmail());
                request.setAttribute(PARAM_NAMES.getString("phone"), user.getPhoneNumber());
                request.setAttribute(PARAM_NAMES.getString("birthDate"), user.getBirthDate());
                request.setAttribute(PARAM_NAMES.getString("mark"), user.getAverageMark());
            }

            request.getRequestDispatcher(PAGE_NAMES.getString("page.user.edit")).forward(request, response);
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