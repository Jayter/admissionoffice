package com.jayton.admissionoffice.command.impl.direction;

import com.jayton.admissionoffice.command.Command;
import com.jayton.admissionoffice.command.exception.ShownException;
import com.jayton.admissionoffice.command.exception.VerificationException;
import com.jayton.admissionoffice.command.util.Verifier;
import com.jayton.admissionoffice.model.to.Application;
import com.jayton.admissionoffice.model.university.Direction;
import com.jayton.admissionoffice.service.ApplicationService;
import com.jayton.admissionoffice.service.DirectionService;
import com.jayton.admissionoffice.service.ServiceFactory;
import com.jayton.admissionoffice.service.exception.ServiceException;
import com.jayton.admissionoffice.util.proxy.HttpServletRequestProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class UpdateDirectionCommand implements Command {

    private final Logger logger = LoggerFactory.getLogger(UpdateDirectionCommand.class);

    @Override
    public String execute(HttpServletRequestProxy request)  {
        Long facultyId = null;
        try {
            Long id = Long.parseLong(request.getParameter(PARAM_NAMES.getString("id")));
            facultyId = Long.parseLong(request.getParameter(PARAM_NAMES.getString("facultyId")));
            Verifier.verifyIds(id, facultyId);

            String name = request.getParameter(PARAM_NAMES.getString("name"));
            int countOfStuds = Integer.parseInt(request.getParameter(PARAM_NAMES.getString("countOfStudents")));
            Verifier.verifyId(facultyId);

            BigDecimal averageCoef = null;
            try {
                averageCoef = new BigDecimal(request.getParameter(PARAM_NAMES.getString("coefficient")))
                        .setScale(2, BigDecimal.ROUND_HALF_DOWN);
            } catch (NumberFormatException e) {
                logger.error("Incorrect number.", e);
                request.setAttribute(PARAM_NAMES.getString("shownException"), new ShownException("Incorrect number."));
                return PAGE_NAMES.getString("controller.edit_direction");
            }

            verifyInput(name, averageCoef, countOfStuds);

            DirectionService directionService = ServiceFactory.getInstance().getDirectionService();
            ApplicationService applicationService = ServiceFactory.getInstance().getApplicationService();
            Direction direction = directionService.update(new Direction(id, name, averageCoef, countOfStuds, facultyId));

            long offset = 0;
            long count = 3;

            Map<Long, String> userNames = directionService.getUserNames(id);
            List<Application> applications = applicationService.getByDirection(id, offset, count);
            long totalCount = applicationService.getCount(id);

            request.setAttribute(PARAM_NAMES.getString("direction"), direction);
            request.setAttribute(PARAM_NAMES.getString("userNames"), userNames);
            request.setAttribute(PARAM_NAMES.getString("applications"), applications);
            request.setAttribute(PARAM_NAMES.getString("offset"), offset);
            request.setAttribute(PARAM_NAMES.getString("count"), count);
            request.setAttribute(PARAM_NAMES.getString("totalCount"), totalCount);

            return PAGE_NAMES.getString("page.direction");
        } catch (VerificationException e) {
            logger.error("Incorrect data.", e);
            request.setAttribute(PARAM_NAMES.getString("shownException"), new ShownException(e.getMessage()));
            return PAGE_NAMES.getString("controller.edit_direction")+"&facultyId="+facultyId;
        } catch (ServiceException | NumberFormatException e) {
            logger.error("Exception is caught.", e);
            request.setAttribute(PARAM_NAMES.getString("exception"), e);
            return PAGE_NAMES.getString("page.exception");
        }
    }

    private void verifyInput(String name, BigDecimal coef, int count)
            throws VerificationException {
        Verifier.verifyString(name);
        Verifier.verifyCoef(coef);
        if(count < 0) {
            throw new VerificationException("Count must be a positive number.");
        }
    }
}