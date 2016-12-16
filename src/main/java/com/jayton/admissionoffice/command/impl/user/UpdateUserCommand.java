package com.jayton.admissionoffice.command.impl.user;

import com.jayton.admissionoffice.command.Command;
import com.jayton.admissionoffice.command.exception.ShownException;
import com.jayton.admissionoffice.command.exception.VerificationException;
import com.jayton.admissionoffice.command.util.Verifier;
import com.jayton.admissionoffice.model.to.Application;
import com.jayton.admissionoffice.model.user.User;
import com.jayton.admissionoffice.service.ApplicationService;
import com.jayton.admissionoffice.service.ServiceFactory;
import com.jayton.admissionoffice.service.UserService;
import com.jayton.admissionoffice.service.exception.ServiceException;
import com.jayton.admissionoffice.util.proxy.HttpServletRequestProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class UpdateUserCommand implements Command {

    private final Logger logger = LoggerFactory.getLogger(UpdateUserCommand.class);

    @Override
    public String execute(HttpServletRequestProxy request) {
        try {
            Long id = Long.parseLong(request.getParameter(PARAM_NAMES.getString("id")));
            String name = request.getParameter(PARAM_NAMES.getString("name"));
            String lastName = request.getParameter(PARAM_NAMES.getString("lastName"));
            String address = request.getParameter(PARAM_NAMES.getString("address"));
            String phoneNumber = request.getParameter(PARAM_NAMES.getString("phone"));
            LocalDate date = LocalDate.parse(request.getParameter(PARAM_NAMES.getString("birthDate")));
            Byte mark = Byte.parseByte(request.getParameter(PARAM_NAMES.getString("mark")));

            verifyInput(id, name, lastName, address, phoneNumber, date, mark);

            UserService userService = ServiceFactory.getInstance().getUserService();
            User user = userService.update(new User(id, name, lastName, address, phoneNumber, date, mark));

            ApplicationService applicationService = ServiceFactory.getInstance().getApplicationService();
            List<Application> applications = applicationService.getByUser(id);
            Map<Long, String> directionNames = userService.getDirectionNames(id);

            request.setAttribute(PARAM_NAMES.getString("user"), user);
            request.setAttribute(PARAM_NAMES.getString("applications"), applications);
            request.setAttribute(PARAM_NAMES.getString("directionNames"), directionNames);

            return PAGE_NAMES.getString("page.user");
        } catch (VerificationException e) {
            logger.error("Incorrect data.", e);
            request.setAttribute(PARAM_NAMES.getString("shownException"), new ShownException(e.getMessage()));
            return PAGE_NAMES.getString("controller.edit_user");
        }
        catch (ServiceException | NumberFormatException e) {
            logger.error("Exception is caught.", e);
            request.setAttribute(PARAM_NAMES.getString("exception"), e);
            return PAGE_NAMES.getString("page.exception");
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