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

public class CreateUserCommand implements Command {

    @Injected
    private UserService userService;

    private final Logger logger = LoggerFactory.getLogger(CreateUserCommand.class);

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        try {
            String name = request.getParameter(PARAM_NAMES.getString("name"));
            String lastName = request.getParameter(PARAM_NAMES.getString("lastName"));
            String address = request.getParameter(PARAM_NAMES.getString("address"));
            String login = request.getParameter(PARAM_NAMES.getString("login"));
            String password = request.getParameter(PARAM_NAMES.getString("password"));
            String phoneNumber = request.getParameter(PARAM_NAMES.getString("phone"));
            LocalDate date = LocalDate.parse(request.getParameter(PARAM_NAMES.getString("birthDate")));
            Byte averageMark = Byte.parseByte(request.getParameter(PARAM_NAMES.getString("mark")));
            verifyInput(name, lastName, address, login, password, phoneNumber, date, averageMark);

            long id = userService.add(new User(name, lastName, address, login, password, phoneNumber, date, averageMark));

            response.sendRedirect(PAGE_NAMES.getString("controller.get_user")+"&id="+id);
        } catch (VerificationException e) {
            logger.error("Incorrect data.", e);
            addAttributes(request, e.getMessage());
            request.getRequestDispatcher(PAGE_NAMES.getString("page.user.edit")).forward(request, response);
        } catch (ServiceException e) {
            logger.error("Exception is caught.", e);
            request.setAttribute(PARAM_NAMES.getString("exception"), e);
            request.getRequestDispatcher(PAGE_NAMES.getString("page.exception")).forward(request, response);
        }
    }

    private void addAttributes(HttpServletRequest request, String exceptionMessage) {
        request.setAttribute(PARAM_NAMES.getString("shownException"), new ShownException(exceptionMessage));
        request.setAttribute(PARAM_NAMES.getString("name"), request.getParameter(PARAM_NAMES.getString("name")));
        request.setAttribute(PARAM_NAMES.getString("lastName"), request.getParameter(PARAM_NAMES.getString("lastName")));
        request.setAttribute(PARAM_NAMES.getString("address"), request.getParameter(PARAM_NAMES.getString("address")));
        request.setAttribute(PARAM_NAMES.getString("login"), request.getParameter(PARAM_NAMES.getString("login")));
        request.setAttribute(PARAM_NAMES.getString("password"), request.getParameter(PARAM_NAMES.getString("password")));
        request.setAttribute(PARAM_NAMES.getString("phone"), request.getParameter(PARAM_NAMES.getString("phone")));
        request.setAttribute(PARAM_NAMES.getString("birthDate"), request.getParameter(PARAM_NAMES.getString("birthDate")));
        request.setAttribute(PARAM_NAMES.getString("mark"), request.getParameter(PARAM_NAMES.getString("mark")));
    }

    private void verifyInput(String name, String lastName, String address, String login, String password,
                             String phoneNumber, LocalDate date, Byte averageMark) throws VerificationException {
        Verifier.verifyStrings(name, lastName, address, phoneNumber);
        Verifier.verifyEmail(login);
        Verifier.verifyPassword(password);
        Verifier.verifyMark(averageMark);
        Verifier.verifyObject(date);
    }
}