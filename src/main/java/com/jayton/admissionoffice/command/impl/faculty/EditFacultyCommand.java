package com.jayton.admissionoffice.command.impl.faculty;

import com.jayton.admissionoffice.command.Command;
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

public class EditFacultyCommand implements Command {

    @Injected
    private FacultyService facultyService;

    private final Logger logger = LoggerFactory.getLogger(EditFacultyCommand.class);

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        try {
            String id = request.getParameter(PARAM_NAMES.getString("id"));

            if(id != null && !id.isEmpty()) {
                Long facultyId = Verifier.convertToLong(id);
                Verifier.verifyId(facultyId);

                Faculty faculty = facultyService.get(facultyId);

                request.setAttribute(PARAM_NAMES.getString("id"), faculty.getId());
                request.setAttribute(PARAM_NAMES.getString("universityId"), faculty.getUniversityId());
                request.setAttribute(PARAM_NAMES.getString("name"), faculty.getName());
                request.setAttribute(PARAM_NAMES.getString("phone"), faculty.getOfficePhone());
                request.setAttribute(PARAM_NAMES.getString("email"), faculty.getOfficeEmail());
                request.setAttribute(PARAM_NAMES.getString("address"), faculty.getOfficeAddress());
            } else {
                Long universityId = Verifier.convertToLong(request.getParameter(PARAM_NAMES.getString("universityId")));
                Verifier.verifyId(universityId);
                request.setAttribute(PARAM_NAMES.getString("universityId"), universityId);
            }

            request.getRequestDispatcher(PAGE_NAMES.getString("page.faculty.edit")).forward(request, response);
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