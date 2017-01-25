package com.jayton.admissionoffice.command.impl.university;

import com.jayton.admissionoffice.command.Command;
import com.jayton.admissionoffice.command.exception.ShownException;
import com.jayton.admissionoffice.command.exception.VerificationException;
import com.jayton.admissionoffice.command.util.Verifier;
import com.jayton.admissionoffice.model.university.University;
import com.jayton.admissionoffice.service.UniversityService;
import com.jayton.admissionoffice.service.exception.ServiceException;
import com.jayton.admissionoffice.util.di.Injected;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CreateUniversityCommand implements Command {

    @Injected
    private UniversityService universityService;

    private final Logger logger = LoggerFactory.getLogger(CreateUniversityCommand.class);

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        try {
            String name = request.getParameter(PARAM_NAMES.getString("name"));
            String city = request.getParameter(PARAM_NAMES.getString("city"));
            String address = request.getParameter(PARAM_NAMES.getString("address"));

            Verifier.verifyStrings(name, city, address);

            University university = universityService.add(new University(name, city, address));

            response.sendRedirect(PAGE_NAMES.getString("controller.get_university")+"&id="+university.getId());
        } catch (VerificationException e) {
            logger.error("Incorrect data.", e);
            convertParamsToAttributes(request, e.getMessage());
            request.getRequestDispatcher(PAGE_NAMES.getString("page.university.edit")).forward(request, response);
        } catch (ServiceException e) {
            logger.error("Exception is caught.", e);
            request.setAttribute(PARAM_NAMES.getString("exception"), e);
            request.getRequestDispatcher(PAGE_NAMES.getString("page.exception")).forward(request, response);
        }
    }

    private void convertParamsToAttributes(HttpServletRequest request, String exceptionMessage) {
        request.setAttribute(PARAM_NAMES.getString("shownException"), new ShownException(exceptionMessage));
        request.setAttribute(PARAM_NAMES.getString("name"), request.getParameter(PARAM_NAMES.getString("name")));
        request.setAttribute(PARAM_NAMES.getString("city"), request.getParameter(PARAM_NAMES.getString("city")));
        request.setAttribute(PARAM_NAMES.getString("address"), request.getParameter(PARAM_NAMES.getString("address")));
    }
}