package com.jayton.admissionoffice.command.impl.user;

import com.jayton.admissionoffice.command.Command;
import com.jayton.admissionoffice.command.exception.ShownException;
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
import java.time.LocalDate;

public class UpdateUserCommand implements Command {

    @Injected
    private UserService userService;

    private final Logger logger = LoggerFactory.getLogger(UpdateUserCommand.class);

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        Long id = null;
        try {
            id = Verifier.convertToLong(request.getParameter(PARAM_NAMES.getString("id")));
            String name = request.getParameter(PARAM_NAMES.getString("name"));
            String lastName = request.getParameter(PARAM_NAMES.getString("lastName"));
            String address = request.getParameter(PARAM_NAMES.getString("address"));
            String phoneNumber = request.getParameter(PARAM_NAMES.getString("phone"));
            LocalDate date = Verifier.convertToLocalDate(request.getParameter(PARAM_NAMES.getString("birthDate")));
            Byte mark = Verifier.convertToByte(request.getParameter(PARAM_NAMES.getString("mark")));

            verifyInput(id, name, lastName, address, phoneNumber, date, mark);

            userService.update(new User(id, name, lastName, address, phoneNumber, date, mark));

            response.sendRedirect(PAGE_NAMES.getString("controller.get_user")+"id="+id);
        } catch (VerificationException e) {
            logger.error("Incorrect data.", e);
            request.setAttribute(PARAM_NAMES.getString("shownException"), new ShownException(e.getMessage()));
            request.getRequestDispatcher(PAGE_NAMES.getString("controller.edit_user")+"&id="+id).forward(request, response);
        } catch (ServiceException e) {
            logger.error("Exception is caught.", e);
            request.setAttribute(PARAM_NAMES.getString("exception"), e);
            request.getRequestDispatcher(PAGE_NAMES.getString("page.exception")).forward(request, response);
        }
    }

    private void verifyInput(Long id, String name, String lastName, String address, String phoneNumber,
                             LocalDate date, Byte averageMark) throws VerificationException {
        Verifier.verifyId(id);
        Verifier.verifyStrings(name, lastName, address, phoneNumber);
        Verifier.verifyMark(averageMark);
        Verifier.verifyObject(date);
    }
}