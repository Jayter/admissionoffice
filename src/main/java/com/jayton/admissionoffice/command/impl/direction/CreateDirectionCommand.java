package com.jayton.admissionoffice.command.impl.direction;

import com.jayton.admissionoffice.command.Command;
import com.jayton.admissionoffice.command.exception.ShownException;
import com.jayton.admissionoffice.command.exception.VerificationException;
import com.jayton.admissionoffice.command.util.Verifier;
import com.jayton.admissionoffice.model.university.Direction;
import com.jayton.admissionoffice.service.DirectionService;
import com.jayton.admissionoffice.service.ServiceFactory;
import com.jayton.admissionoffice.service.exception.ServiceException;
import com.jayton.admissionoffice.util.proxy.HttpServletRequestProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class CreateDirectionCommand implements Command {

    private final Logger logger = LoggerFactory.getLogger(CreateDirectionCommand.class);

    @Override
    public String execute(HttpServletRequestProxy request) {
        try {
            String name = request.getParameter(PARAM_NAMES.getString("name"));
            int countOfStuds = Integer.parseInt(request.getParameter(PARAM_NAMES.getString("countOfStudents")));
            Long facultyId = Long.parseLong(request.getParameter(PARAM_NAMES.getString("facultyId")));
            BigDecimal averageCoef = new BigDecimal(request.getParameter(PARAM_NAMES.getString("coefficient")))
                    .setScale(2, BigDecimal.ROUND_HALF_DOWN);
            verifyInput(name, averageCoef, countOfStuds, facultyId);

            Long subject1Id = Long.parseLong(request.getParameter(PARAM_NAMES.getString("subject1Id")));
            Long subject2Id = Long.parseLong(request.getParameter(PARAM_NAMES.getString("subject2Id")));
            Long subject3Id = Long.parseLong(request.getParameter(PARAM_NAMES.getString("subject3Id")));
            BigDecimal coef1 = new BigDecimal(request.getParameter(PARAM_NAMES.getString("subject1Coefficient")));
            BigDecimal coef2 = new BigDecimal(request.getParameter(PARAM_NAMES.getString("subject2Coefficient")));
            BigDecimal coef3 = new BigDecimal(request.getParameter(PARAM_NAMES.getString("subject3Coefficient")));

            Verifier.verifyIds(subject1Id, subject2Id, subject3Id);
            Verifier.verifyCoefs(coef1, coef2, coef3);

            Map<Long, BigDecimal> subjects = new HashMap<Long, BigDecimal>(){{put(subject1Id, coef1);
                put(subject2Id, coef2); put(subject3Id, coef3);}};

            DirectionService directionService = ServiceFactory.getInstance().getDirectionService();
            Direction direction = directionService.add(new Direction(name, averageCoef, countOfStuds, facultyId, subjects));

            request.setAttribute(PARAM_NAMES.getString("direction"), direction);

            return PAGE_NAMES.getString("page.direction");
        } catch (VerificationException e) {
            logger.error("Incorrect data.", e);
            request.setAttribute(PARAM_NAMES.getString("shownException"), new ShownException(e.getMessage()));
            return PAGE_NAMES.getString("controller.edit_direction");
        } catch (NumberFormatException e) {
            logger.error("Incorrect number.", e);
            request.setAttribute(PARAM_NAMES.getString("shownException"), new ShownException("Incorrect number."));
            return PAGE_NAMES.getString("controller.edit_direction");
        } catch (ServiceException e) {
            logger.error("Exception is caught.", e);
            request.setAttribute(PARAM_NAMES.getString("exception"), e);
            return PAGE_NAMES.getString("page.exception");
        }
    }

    private void verifyInput(String name, BigDecimal coef, int count, Long facultyId) throws VerificationException {
        Verifier.verifyString(name);
        Verifier.verifyCoef(coef);
        Verifier.verifyId(facultyId);
        if(count < 0) {
            throw new VerificationException("Count must be a positive number.");
        }
    }
}