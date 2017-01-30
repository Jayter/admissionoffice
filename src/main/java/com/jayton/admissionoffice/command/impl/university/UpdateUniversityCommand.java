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

public class UpdateUniversityCommand implements Command {

    @Injected
    private UniversityService universityService;

    private final Logger logger = LoggerFactory.getLogger(UpdateUniversityCommand.class);

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        Long id = null;
        try {
            id = Verifier.convertToLong(request.getParameter(PARAM_NAMES.getString("id")));
            Verifier.verifyId(id);

            String name = request.getParameter(PARAM_NAMES.getString("name"));
            String city = request.getParameter(PARAM_NAMES.getString("city"));
            String address = request.getParameter(PARAM_NAMES.getString("address"));

            Verifier.verifyStrings(name, city, address);

            if(!universityService.update(new University(id, name, city, address))) {
                request.setAttribute(PARAM_NAMES.getString("exception"),
                        new VerificationException("Failed to update university."));
                request.getRequestDispatcher(PAGE_NAMES.getString("page.exception")).forward(request, response);
            }

            response.sendRedirect(PAGE_NAMES.getString("controller.get_university")+"&id="+id);
        } catch (VerificationException e) {
            logger.error("Incorrect data.", e);
            request.setAttribute(PARAM_NAMES.getString("shownException"), new ShownException(e.getMessage()));
            request.getRequestDispatcher(PAGE_NAMES.getString("controller.edit_university")+"&id="+id).forward(request, response);
        } catch (ServiceException e) {
            logger.error("Exception is caught.", e);
            request.setAttribute(PARAM_NAMES.getString("exception"), e);
            request.getRequestDispatcher(PAGE_NAMES.getString("page.exception")).forward(request, response);
        }
    }
}