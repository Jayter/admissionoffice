package com.jayton.admissionoffice.command.impl.direction;

import com.jayton.admissionoffice.command.Command;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetDirectionCommand implements Command {

    private final Logger logger = LoggerFactory.getLogger(GetDirectionCommand.class);

    @Override
    public String execute(HttpServletRequestProxy request) {
        try {
            Long id = Long.parseLong(request.getParameter(PARAM_NAMES.getString("id")));

            String offs = request.getParameter(PARAM_NAMES.getString("offset"));
            String coun = request.getParameter(PARAM_NAMES.getString("count"));
            Long offset = offs == null ? 0 : Long.parseLong(offs);
            Long count = coun == null ? 3 : Long.parseLong(coun);

            Verifier.verifyId(id);
            Verifier.verifyNonNegative(offset);
            Verifier.verifyNonNegative(count);

            DirectionService directionService = ServiceFactory.getInstance().getDirectionService();
            ApplicationService applicationService = ServiceFactory.getInstance().getApplicationService();

            Direction direction = directionService.get(id);
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
        } catch (ServiceException | VerificationException | NumberFormatException e) {
            logger.error("Exception is caught.", e);
            request.setAttribute(PARAM_NAMES.getString("exception"), e);
            return PAGE_NAMES.getString("page.exception");
        }
    }
}