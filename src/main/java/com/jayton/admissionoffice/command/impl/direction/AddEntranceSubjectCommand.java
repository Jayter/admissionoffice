package com.jayton.admissionoffice.command.impl.direction;

import com.jayton.admissionoffice.command.Command;
import com.jayton.admissionoffice.command.exception.ShownException;
import com.jayton.admissionoffice.command.exception.VerificationException;
import com.jayton.admissionoffice.command.util.Verifier;
import com.jayton.admissionoffice.service.DirectionService;
import com.jayton.admissionoffice.service.ServiceFactory;
import com.jayton.admissionoffice.service.exception.ServiceException;
import com.jayton.admissionoffice.service.exception.ServiceVerificationException;
import com.jayton.admissionoffice.util.proxy.HttpServletRequestProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;

public class AddEntranceSubjectCommand implements Command {

    private final Logger logger = LoggerFactory.getLogger(AddEntranceSubjectCommand.class);

    @Override
    public String execute(HttpServletRequestProxy request) {
        Long directionId = null;
        Long subjectId = null;
        try {
            directionId = Long.parseLong(request.getParameter(PARAM_NAMES.getString("directionId")));
            subjectId = Long.parseLong(request.getParameter(PARAM_NAMES.getString("subjectId")));
            Verifier.verifyIds(directionId, subjectId);

            BigDecimal coef = null;
            try {
                coef = new BigDecimal(request.getParameter(PARAM_NAMES.getString("coefficient")))
                        .setScale(2, BigDecimal.ROUND_HALF_UP);
            } catch (NumberFormatException e) {
                logger.error("Incorrect number.", e);
                request.setAttribute(PARAM_NAMES.getString("shownException"), new ShownException("Incorrect coefficient."));
                return PAGE_NAMES.getString("controller.edit_direction")+"&id="+directionId;
            }
            Verifier.verifyCoef(coef);

            DirectionService directionService = ServiceFactory.getInstance().getDirectionService();
            directionService.addEntranceSubject(directionId, subjectId, coef);

            return PAGE_NAMES.getString("controller.get_direction")+"&id="+directionId;
        } catch (VerificationException | ServiceVerificationException e) {
            logger.error("Incorrect data.", e);
            request.setAttribute(PARAM_NAMES.getString("shownException"), new ShownException(e.getMessage()));
            return PAGE_NAMES.getString("controller.edit_direction")+"&id="+directionId;
        }
        catch (ServiceException | NumberFormatException e) {
            logger.error("Exception is caught.", e);
            request.setAttribute(PARAM_NAMES.getString("exception"), e);
            return PAGE_NAMES.getString("page.exception");
        }
    }
}