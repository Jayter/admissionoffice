package com.jayton.admissionoffice.command.impl.direction;

import com.jayton.admissionoffice.command.Command;
import com.jayton.admissionoffice.command.exception.ShownException;
import com.jayton.admissionoffice.command.exception.VerificationException;
import com.jayton.admissionoffice.command.util.Verifier;
import com.jayton.admissionoffice.service.DirectionService;
import com.jayton.admissionoffice.service.exception.ServiceException;
import com.jayton.admissionoffice.service.exception.ServiceVerificationException;
import com.jayton.admissionoffice.util.di.Injected;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;

public class AddEntranceSubjectCommand implements Command {

    @Injected
    private DirectionService directionService;

    private final Logger logger = LoggerFactory.getLogger(AddEntranceSubjectCommand.class);

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        Long id = null;
        try {
            id = Verifier.convertToLong(request.getParameter(PARAM_NAMES.getString("directionId")));
            Long subjectId = Verifier.convertToLong(request.getParameter(PARAM_NAMES.getString("subjectId")));
            BigDecimal coef = Verifier.convertToBigDecimal(request.getParameter(PARAM_NAMES.getString("coefficient"))).
                    setScale(2, BigDecimal.ROUND_HALF_UP);
            verifyInput(id, subjectId, coef);

            directionService.addEntranceSubject(id, subjectId, coef);

            response.sendRedirect(PAGE_NAMES.getString("controller.get_direction")+"&id="+id);
        } catch (VerificationException | ServiceVerificationException e) {
            logger.error("Incorrect data.", e);
            request.setAttribute(PARAM_NAMES.getString("shownException"), new ShownException(e.getMessage()));
            request.getRequestDispatcher(PAGE_NAMES.getString("controller.edit_direction")+"&id="+id).forward(request, response);
        }
        catch (ServiceException e) {
            logger.error("Exception is caught.", e);
            request.setAttribute(PARAM_NAMES.getString("exception"), e);
            request.getRequestDispatcher(PAGE_NAMES.getString("page.exception")).forward(request, response);
        }
    }

    private void verifyInput(long id, long subjectId, BigDecimal coef) throws VerificationException {
        Verifier.verifyIds(id, subjectId);
        Verifier.verifyCoef(coef);
    }
}