package com.jayton.admissionoffice.command.impl.direction;

import com.jayton.admissionoffice.command.Command;
import com.jayton.admissionoffice.command.exception.ShownException;
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
import java.math.BigDecimal;

public class UpdateDirectionCommand implements Command {

    @Injected
    private DirectionService directionService;

    private final Logger logger = LoggerFactory.getLogger(UpdateDirectionCommand.class);

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        Long id = null;
        try {
            id = Verifier.convertToLong(request.getParameter(PARAM_NAMES.getString("facultyId")));
            Long facultyId = Verifier.convertToLong(request.getParameter(PARAM_NAMES.getString("id")));
            String name = request.getParameter(PARAM_NAMES.getString("name"));
            Integer countOfStuds = Verifier.convertToInt(request.getParameter(PARAM_NAMES.getString("countOfStudents")));
            BigDecimal averageCoef = Verifier.convertToBigDecimal(
                    request.getParameter(PARAM_NAMES.getString("coefficient"))).setScale(2, BigDecimal.ROUND_HALF_DOWN);

            verifyInput(id, facultyId, name, averageCoef, countOfStuds);

            directionService.update(new Direction(id, name, averageCoef, countOfStuds, facultyId));

            response.sendRedirect(PAGE_NAMES.getString("controller.get_direction"+"&id="+id));
        } catch (VerificationException e) {
            logger.error("Incorrect data.", e);
            request.setAttribute(PARAM_NAMES.getString("shownException"), new ShownException(e.getMessage()));
            request.getRequestDispatcher(PAGE_NAMES.getString("controller.edit_direction")+"&id="+id)
                    .forward(request, response);
        } catch (ServiceException e) {
            logger.error("Exception is caught.", e);
            request.setAttribute(PARAM_NAMES.getString("exception"), e);
            request.getRequestDispatcher(PAGE_NAMES.getString("page.exception")).forward(request, response);
        }
    }

    private void verifyInput(Long id, Long facultyId, String name, BigDecimal coef, Integer count)
            throws VerificationException {
        Verifier.verifyIds(id, facultyId);
        Verifier.verifyString(name);
        Verifier.verifyCoef(coef);
        if(count < 0) {
            throw new VerificationException("Count must be a positive number.");
        }
    }
}