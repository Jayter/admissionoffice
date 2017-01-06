package com.jayton.admissionoffice.command.impl.user;

import com.jayton.admissionoffice.command.Command;
import com.jayton.admissionoffice.command.exception.ShownException;
import com.jayton.admissionoffice.command.exception.VerificationException;
import com.jayton.admissionoffice.command.util.Verifier;
import com.jayton.admissionoffice.model.Subject;
import com.jayton.admissionoffice.model.user.User;
import com.jayton.admissionoffice.service.ServiceFactory;
import com.jayton.admissionoffice.service.UserService;
import com.jayton.admissionoffice.service.UtilService;
import com.jayton.admissionoffice.service.exception.ServiceException;
import com.jayton.admissionoffice.util.proxy.HttpServletRequestProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CreateUserCommand implements Command {

    private final Logger logger = LoggerFactory.getLogger(CreateUserCommand.class);

    @Override
    public String execute(HttpServletRequestProxy request) {
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

            UserService userService = ServiceFactory.getInstance().getUserService();
            User user = userService.add(new User(name, lastName, address, login, password, phoneNumber, date, averageMark));

            request.setAttribute(PARAM_NAMES.getString("user"), user);

            UtilService utilService = ServiceFactory.getInstance().getUtilService();
            List<Subject> allSubjects = utilService.getAllSubjects();
            Map<Long, Subject> subjectsMap = new HashMap<>();
            allSubjects.forEach(subject -> subjectsMap.put(subject.getId(), subject));

            request.setAttribute(PARAM_NAMES.getString("subjects"), subjectsMap);

            return PAGE_NAMES.getString("page.user");
        } catch (VerificationException e) {
            logger.error("Incorrect data.", e);
            request.setAttribute(PARAM_NAMES.getString("shownException"), new ShownException(e.getMessage()));
            return PAGE_NAMES.getString("controller.edit_user");
        } catch (NumberFormatException e) {
            logger.error("Incorrect number.", e);
            request.setAttribute(PARAM_NAMES.getString("shownException"), new ShownException("Incorrect mark."));
            return PAGE_NAMES.getString("controller.edit_user");
        }
        catch (ServiceException e) {
            logger.error("Exception is caught.", e);
            request.setAttribute(PARAM_NAMES.getString("exception"), e);
            return PAGE_NAMES.getString("page.exception");
        }
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