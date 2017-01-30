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

public class UpdateFacultyCommand implements Command {

    @Injected
    private FacultyService facultyService;

    private final Logger logger = LoggerFactory.getLogger(UpdateFacultyCommand.class);

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        Long id = null;
        try {
            id = Verifier.convertToLong(request.getParameter(PARAM_NAMES.getString("id")));
            Verifier.verifyId(id);

            String name = request.getParameter(PARAM_NAMES.getString("name"));
            String phone = request.getParameter(PARAM_NAMES.getString("phone"));
            String email = request.getParameter(PARAM_NAMES.getString("email"));
            String address = request.getParameter(PARAM_NAMES.getString("address"));
            Long universityId = Verifier.convertToLong(request.getParameter(PARAM_NAMES.getString("universityId")));

            verifyInput(name, phone, email, address, universityId);

            if(!facultyService.update(new Faculty(id, name, phone, email, address, universityId))) {
                request.setAttribute(PARAM_NAMES.getString("exception"),
                        new VerificationException("Failed to update faculty."));
                request.getRequestDispatcher(PAGE_NAMES.getString("page.exception")).forward(request, response);
            }

            response.sendRedirect(PAGE_NAMES.getString("controller.get_faculty")+"&id="+id);
        } catch (VerificationException e) {
            logger.error("Incorrect data.", e);
            request.setAttribute(PARAM_NAMES.getString("shownException"), new ShownException(e.getMessage()));
            request.getRequestDispatcher(PAGE_NAMES.getString("controller.edit_faculty")+"&id="+id).forward(request, response);
        } catch (ServiceException e) {
            logger.error("Exception is caught.", e);
            request.setAttribute(PARAM_NAMES.getString("exception"), e);
            request.getRequestDispatcher(PAGE_NAMES.getString("page.exception")).forward(request, response);
        }
    }

    private void verifyInput(String name, String phone, String email, String address, Long universityId)
            throws VerificationException {
        Verifier.verifyId(universityId);
        Verifier.verifyStrings(name, phone, address);
        Verifier.verifyEmail(email);
    }
}