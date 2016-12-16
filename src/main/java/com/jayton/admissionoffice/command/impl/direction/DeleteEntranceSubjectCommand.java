package com.jayton.admissionoffice.command.impl.direction;

import com.jayton.admissionoffice.command.Command;
import com.jayton.admissionoffice.command.exception.VerificationException;
import com.jayton.admissionoffice.command.util.Verifier;
import com.jayton.admissionoffice.service.DirectionService;
import com.jayton.admissionoffice.service.ServiceFactory;
import com.jayton.admissionoffice.service.exception.ServiceException;
import com.jayton.admissionoffice.util.proxy.HttpServletRequestProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DeleteEntranceSubjectCommand implements Command {

    private final Logger logger = LoggerFactory.getLogger(DeleteEntranceSubjectCommand.class);

    @Override
    public String execute(HttpServletRequestProxy request) {
        try {
            Long directionId = Long.parseLong(request.getParameter(PARAM_NAMES.getString("directionId")));
            Long subjectId = Long.parseLong(request.getParameter(PARAM_NAMES.getString("subjectId")));
            Verifier.verifyIds(directionId, subjectId);

            DirectionService directionService = ServiceFactory.getInstance().getDirectionService();
            directionService.deleteEntranceSubject(directionId, subjectId);

            return PAGE_NAMES.getString("controller.get_direction")+"&id="+directionId;
        } catch (ServiceException | VerificationException | NumberFormatException e) {
            logger.error("Exception is caught.", e);
            request.setAttribute(PARAM_NAMES.getString("exception"), e);
            return PAGE_NAMES.getString("page.exception");
        }
    }
}