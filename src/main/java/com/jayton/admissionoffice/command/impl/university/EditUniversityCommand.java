package com.jayton.admissionoffice.command.impl.university;

import com.jayton.admissionoffice.command.Command;
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

public class EditUniversityCommand implements Command {

    @Injected
    private UniversityService universityService;

    private final Logger logger = LoggerFactory.getLogger(EditUniversityCommand.class);

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        try {
            String id = request.getParameter(PARAM_NAMES.getString("id"));

            if(id != null && !id.isEmpty()) {
                Long universityId = Verifier.convertToLong(id);
                Verifier.verifyId(universityId);

                University university = universityService.get(universityId);
                request.setAttribute(PARAM_NAMES.getString("id"), university.getId());
                request.setAttribute(PARAM_NAMES.getString("name"), university.getName());
                request.setAttribute(PARAM_NAMES.getString("city"), university.getCity());
                request.setAttribute(PARAM_NAMES.getString("address"), university.getAddress());
            }

            request.getRequestDispatcher(PAGE_NAMES.getString("page.university.edit")).forward(request, response);
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