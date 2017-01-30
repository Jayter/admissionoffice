package com.jayton.admissionoffice.command.impl.user;

import com.jayton.admissionoffice.command.Command;
import com.jayton.admissionoffice.command.exception.ShownException;
import com.jayton.admissionoffice.command.exception.VerificationException;
import com.jayton.admissionoffice.command.util.Verifier;
import com.jayton.admissionoffice.service.UserService;
import com.jayton.admissionoffice.service.exception.ServiceException;
import com.jayton.admissionoffice.util.di.Injected;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AddUserResultCommand implements Command {

    @Injected
    private UserService userService;

    private final Logger logger = LoggerFactory.getLogger(AddUserResultCommand.class);

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        Long userId = null;
        try {
            userId = Verifier.convertToLong(request.getParameter(PARAM_NAMES.getString("id")));
            Long subjectId = Verifier.convertToLong(request.getParameter(PARAM_NAMES.getString("subjectId")));
            Short mark = Verifier.convertToShort(request.getParameter(PARAM_NAMES.getString("mark")));
            Verifier.verifyIds(userId, subjectId);
            Verifier.verifyResult(mark);

            userService.addResult(userId, subjectId, mark);

            response.sendRedirect(PAGE_NAMES.getString("controller.get_user")+"&id="+userId);
        } catch (VerificationException e) {
            logger.error("Incorrect data.", e);
            request.setAttribute(PARAM_NAMES.getString("shownException"), new ShownException(e.getMessage()));
            request.getRequestDispatcher(PAGE_NAMES.getString("controller.get_user")+"&id="+userId).forward(request, response);
        }
        catch (ServiceException e) {
            logger.error("Exception is caught.", e);
            request.setAttribute(PARAM_NAMES.getString("exception"), e);
            request.getRequestDispatcher(PAGE_NAMES.getString("page.exception")).forward(request, response);
        }
    }
}