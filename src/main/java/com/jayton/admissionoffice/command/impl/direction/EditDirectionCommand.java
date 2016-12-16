package com.jayton.admissionoffice.command.impl.direction;

import com.jayton.admissionoffice.command.Command;
import com.jayton.admissionoffice.command.exception.VerificationException;
import com.jayton.admissionoffice.command.util.Verifier;
import com.jayton.admissionoffice.model.university.Direction;
import com.jayton.admissionoffice.service.DirectionService;
import com.jayton.admissionoffice.service.ServiceFactory;
import com.jayton.admissionoffice.service.exception.ServiceException;
import com.jayton.admissionoffice.util.proxy.HttpServletRequestProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EditDirectionCommand implements Command {

    private final Logger logger = LoggerFactory.getLogger(EditDirectionCommand.class);

    @Override
    public String execute(HttpServletRequestProxy request) {
        try {
            String id = request.getParameter(PARAM_NAMES.getString("id"));

            if(id != null && !id.isEmpty()) {
                Long directionId = Long.parseLong(id);
                Verifier.verifyId(directionId);

                DirectionService directionService = ServiceFactory.getInstance().getDirectionService();
                Direction direction = directionService.get(directionId);

                request.setAttribute(PARAM_NAMES.getString("direction"), direction);
                return PAGE_NAMES.getString("page.direction.edit");
            } else {
                Long facultyId = Long.parseLong(request.getParameter(PARAM_NAMES.getString("facultyId")));
                Verifier.verifyId(facultyId);

                request.setAttribute(PARAM_NAMES.getString("facultyId"), facultyId);
                return PAGE_NAMES.getString("page.direction.create");
            }

        } catch (ServiceException | VerificationException | NumberFormatException e) {
            logger.error("Exception is caught.", e);
            request.setAttribute(PARAM_NAMES.getString("exception"), e);
            return PAGE_NAMES.getString("page.exception");
        }
    }
}