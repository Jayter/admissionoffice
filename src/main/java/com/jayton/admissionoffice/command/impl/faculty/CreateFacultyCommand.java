package com.jayton.admissionoffice.command.impl.faculty;

import com.jayton.admissionoffice.command.Command;
import com.jayton.admissionoffice.command.exception.ShownException;
import com.jayton.admissionoffice.command.exception.VerificationException;
import com.jayton.admissionoffice.command.util.Verifier;
import com.jayton.admissionoffice.model.university.Faculty;
import com.jayton.admissionoffice.service.FacultyService;
import com.jayton.admissionoffice.service.exception.ServiceException;
import com.jayton.admissionoffice.util.di.Injected;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CreateFacultyCommand implements Command {

    @Injected
    private FacultyService facultyService;

    private final Logger logger = LoggerFactory.getLogger(CreateFacultyCommand.class);

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        try {
            String name = request.getParameter(PARAM_NAMES.getString("name"));
            String phone = request.getParameter(PARAM_NAMES.getString("phone"));
            String email = request.getParameter(PARAM_NAMES.getString("email"));
            String address = request.getParameter(PARAM_NAMES.getString("address"));
            Long universityId = Verifier.convertToLong(request.getParameter(PARAM_NAMES.getString("universityId")));

            verifyInput(name, phone, email, address, universityId);

            Faculty faculty = facultyService.add(new Faculty(name, phone, email, address, universityId));

            response.sendRedirect(PAGE_NAMES.getString("controller.get_faculty")+"&id="+faculty.getId());
        } catch (VerificationException e) {
            logger.error("Incorrect data.", e);
            convertParamsToAttributes(request, e.getMessage());
            request.getRequestDispatcher(PAGE_NAMES.getString("page.faculty.edit")).forward(request, response);
        } catch (ServiceException e) {
            logger.error("Exception is caught.", e);
            request.setAttribute(PARAM_NAMES.getString("exception"), e);
            request.getRequestDispatcher(PAGE_NAMES.getString("page.exception")).forward(request, response);
        }
    }

    private void convertParamsToAttributes(HttpServletRequest request, String exceptionMessage) {
        request.setAttribute(PARAM_NAMES.getString("shownException"), new ShownException(exceptionMessage));
        request.setAttribute(PARAM_NAMES.getString("name"), request.getParameter(PARAM_NAMES.getString("name")));
        request.setAttribute(PARAM_NAMES.getString("phone"), request.getParameter(PARAM_NAMES.getString("phone")));
        request.setAttribute(PARAM_NAMES.getString("email"), request.getParameter(PARAM_NAMES.getString("email")));
        request.setAttribute(PARAM_NAMES.getString("address"), request.getParameter(PARAM_NAMES.getString("address")));
        request.setAttribute(PARAM_NAMES.getString("universityId"), request.getParameter(PARAM_NAMES.getString("universityId")));
    }

    private void verifyInput(String name, String phone, String email, String address, Long universityId)
            throws VerificationException {
        Verifier.verifyStrings(name, phone, address);
        Verifier.verifyEmail(email);
        Verifier.verifyId(universityId);
    }
}