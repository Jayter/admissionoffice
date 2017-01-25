package com.jayton.admissionoffice.command.impl.university;

import com.jayton.admissionoffice.command.Command;
import com.jayton.admissionoffice.command.exception.VerificationException;
import com.jayton.admissionoffice.command.util.Verifier;
import com.jayton.admissionoffice.model.university.Faculty;
import com.jayton.admissionoffice.model.university.University;
import com.jayton.admissionoffice.service.FacultyService;
import com.jayton.admissionoffice.service.UniversityService;
import com.jayton.admissionoffice.service.exception.ServiceException;
import com.jayton.admissionoffice.util.di.Injected;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class GetUniversityCommand implements Command {

    @Injected
    private UniversityService universityService;
    @Injected
    private FacultyService facultyService;

    private final Logger logger = LoggerFactory.getLogger(GetUniversityCommand.class);

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        try {
            Long id = Verifier.convertToLong(request.getParameter(PARAM_NAMES.getString("id")));

            String offs = request.getParameter(PARAM_NAMES.getString("offset"));
            String coun = request.getParameter(PARAM_NAMES.getString("count"));
            Long offset = offs == null ? 0 : Verifier.convertToLong(offs);
            Long count = coun == null ? 3 : Verifier.convertToLong(coun);

            Verifier.verifyId(id);
            Verifier.verifyNonNegative(offset);
            Verifier.verifyNonNegative(count);

            University university = universityService.get(id);
            List<Faculty> faculties = facultyService.getByUniversity(id, offset, count);
            long totalCount = facultyService.getCount(id);

            request.setAttribute(PARAM_NAMES.getString("university"), university);
            request.setAttribute(PARAM_NAMES.getString("faculties"), faculties);
            request.setAttribute(PARAM_NAMES.getString("offset"), offset);
            request.setAttribute(PARAM_NAMES.getString("count"), count);
            request.setAttribute(PARAM_NAMES.getString("totalCount"), totalCount);

            request.getRequestDispatcher(PAGE_NAMES.getString("page.university")).forward(request, response);
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