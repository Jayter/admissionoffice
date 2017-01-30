package com.jayton.admissionoffice.command.impl.direction;

import com.jayton.admissionoffice.command.Command;
import com.jayton.admissionoffice.command.exception.VerificationException;
import com.jayton.admissionoffice.command.util.Verifier;
import com.jayton.admissionoffice.model.university.Direction;
import com.jayton.admissionoffice.service.DirectionService;
import com.jayton.admissionoffice.service.exception.ServiceException;
import com.jayton.admissionoffice.util.di.Injected;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class EditDirectionCommand implements Command {

    @Injected
    private DirectionService directionService;

    private final Logger logger = LoggerFactory.getLogger(EditDirectionCommand.class);

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        try {
            String id = request.getParameter(PARAM_NAMES.getString("id"));

            if(id != null && !id.isEmpty()) {
                Long directionId = Verifier.convertToLong(id);
                Verifier.verifyId(directionId);

                Direction direction = directionService.get(directionId);
                Verifier.verifyObject(direction);

                request.setAttribute(PARAM_NAMES.getString("direction"), direction);
                request.getRequestDispatcher(PAGE_NAMES.getString("page.direction.edit")).forward(request, response);
            } else {
                Long facultyId = Verifier.convertToLong(request.getParameter(PARAM_NAMES.getString("facultyId")));
                Verifier.verifyId(facultyId);

                request.setAttribute(PARAM_NAMES.getString("facultyId"), facultyId);
                request.getRequestDispatcher(PAGE_NAMES.getString("page.direction.create")).forward(request, response);
            }

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