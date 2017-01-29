package com.jayton.admissionoffice.command.impl.direction;

import com.jayton.admissionoffice.command.Command;
import com.jayton.admissionoffice.command.exception.ShownException;
import com.jayton.admissionoffice.command.exception.VerificationException;
import com.jayton.admissionoffice.command.util.Verifier;
import com.jayton.admissionoffice.model.university.Direction;
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
import java.util.*;

public class CreateDirectionCommand implements Command {

    @Injected
    private DirectionService directionService;

    private final Logger logger = LoggerFactory.getLogger(CreateDirectionCommand.class);

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        try {
            Long facultyId = Verifier.convertToLong(request.getParameter(PARAM_NAMES.getString("facultyId")));
            String name = request.getParameter(PARAM_NAMES.getString("name"));
            Integer countOfStuds = Verifier.convertToInt(request.getParameter(PARAM_NAMES.getString("countOfStudents")));
            BigDecimal averageCoef = Verifier.convertToBigDecimal(
                    request.getParameter(PARAM_NAMES.getString("coefficient"))).setScale(2, BigDecimal.ROUND_HALF_DOWN);

            verifyInput(name, averageCoef, countOfStuds, facultyId);

            Map<Long, BigDecimal> subjects = new HashMap<>();
            for(int i = 1; i <= 3; i++) {
                String idParamName = String.format(PARAM_NAMES.getString("subject%dId"), i);
                String coefParamName = String.format(PARAM_NAMES.getString("subject%dCoefficient"), i);
                Long subjectId = Verifier.convertToLong(request.getParameter(idParamName));
                BigDecimal coef  = Verifier.convertToBigDecimal(request.getParameter(coefParamName));

                Verifier.verifyId(subjectId);
                Verifier.verifyCoef(coef);
                if(!subjects.containsKey(subjectId)) {
                    subjects.put(subjectId, coef);
                } else {
                    throw new VerificationException("Can not add duplicated subjects.");
                }
            }

            long id = directionService.add(new Direction(name, averageCoef, countOfStuds, facultyId, subjects));

            response.sendRedirect(PAGE_NAMES.getString("controller.get_direction")+"&id="+id);
        } catch (VerificationException | ServiceVerificationException e) {
            logger.error("Incorrect data.", e);
            convertParamsToAttributes(request, e.getMessage());
            request.getRequestDispatcher(PAGE_NAMES.getString("page.direction.create")).forward(request, response);
        } catch (ServiceException e) {
            logger.error("Exception is caught.", e);
            request.setAttribute(PARAM_NAMES.getString("exception"), e);
            request.getRequestDispatcher(PAGE_NAMES.getString("page.exception")).forward(request, response);
        }
    }

    private void convertParamsToAttributes(HttpServletRequest request, String exceptionMessage) {
        request.setAttribute(PARAM_NAMES.getString("shownException"), new ShownException(exceptionMessage));
        request.setAttribute(PARAM_NAMES.getString("facultyId"), request.getParameter(PARAM_NAMES.getString("facultyId")));
        request.setAttribute(PARAM_NAMES.getString("name"), request.getParameter(PARAM_NAMES.getString("name")));
        request.setAttribute(PARAM_NAMES.getString("countOfStudents"), request.getParameter(PARAM_NAMES.getString("countOfStudents")));
        request.setAttribute(PARAM_NAMES.getString("coefficient"), request.getParameter(PARAM_NAMES.getString("coefficient")));
    }

    private void verifyInput(String name, BigDecimal coef, Integer count, Long facultyId) throws VerificationException {
        Verifier.verifyString(name);
        Verifier.verifyCoef(coef);
        Verifier.verifyId(facultyId);
        if(count < 0) {
            throw new VerificationException("Count must be a positive number.");
        }
    }
}