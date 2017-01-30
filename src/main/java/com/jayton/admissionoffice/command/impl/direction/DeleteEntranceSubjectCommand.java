package com.jayton.admissionoffice.command.impl.direction;

import com.jayton.admissionoffice.command.Command;
import com.jayton.admissionoffice.command.exception.VerificationException;
import com.jayton.admissionoffice.command.util.Verifier;
import com.jayton.admissionoffice.service.DirectionService;
import com.jayton.admissionoffice.service.exception.ServiceException;
import com.jayton.admissionoffice.util.di.Injected;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class DeleteEntranceSubjectCommand implements Command {

    @Injected
    private DirectionService directionService;

    private final Logger logger = LoggerFactory.getLogger(DeleteEntranceSubjectCommand.class);

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        try {
            Long directionId = Verifier.convertToLong(request.getParameter(PARAM_NAMES.getString("directionId")));
            Long subjectId = Verifier.convertToLong(request.getParameter(PARAM_NAMES.getString("subjectId")));
            Verifier.verifyIds(directionId, subjectId);

            if(!directionService.deleteEntranceSubject(directionId, subjectId)) {
                request.setAttribute(PARAM_NAMES.getString("exception"),
                        new VerificationException("Failed to delete entrance subject."));
                request.getRequestDispatcher(PAGE_NAMES.getString("page.exception")).forward(request, response);
            }

            request.getRequestDispatcher(PAGE_NAMES.getString("controller.get_direction")+"&id="+directionId).forward(request, response);
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