package com.jayton.admissionoffice.command.impl.university;

import com.jayton.admissionoffice.command.Command;
import com.jayton.admissionoffice.command.exception.VerificationException;
import com.jayton.admissionoffice.command.util.Verifier;
import com.jayton.admissionoffice.service.UniversityService;
import com.jayton.admissionoffice.service.exception.ServiceException;
import com.jayton.admissionoffice.util.di.Injected;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class DeleteUniversityCommand implements Command {

    @Injected
    private UniversityService universityService;

    private final Logger logger = LoggerFactory.getLogger(DeleteUniversityCommand.class);

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        try {
            Long id = Verifier.convertToLong(request.getParameter(PARAM_NAMES.getString("id")));
            Verifier.verifyId(id);

            if(!universityService.delete(id)) {
                request.setAttribute(PARAM_NAMES.getString("exception"),
                        new VerificationException("Failed to delete university."));
                request.getRequestDispatcher(PAGE_NAMES.getString("page.exception")).forward(request, response);
            }

            response.sendRedirect(PAGE_NAMES.getString("controller.load_main"));
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