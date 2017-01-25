package com.jayton.admissionoffice.command.impl.direction;

import com.jayton.admissionoffice.command.Command;
import com.jayton.admissionoffice.command.exception.VerificationException;
import com.jayton.admissionoffice.command.util.Verifier;
import com.jayton.admissionoffice.model.Subject;
import com.jayton.admissionoffice.model.to.Application;
import com.jayton.admissionoffice.model.university.Direction;
import com.jayton.admissionoffice.service.ApplicationService;
import com.jayton.admissionoffice.service.DirectionService;
import com.jayton.admissionoffice.service.UtilService;
import com.jayton.admissionoffice.service.exception.ServiceException;
import com.jayton.admissionoffice.util.di.Injected;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetDirectionCommand implements Command {

    @Injected
    private DirectionService directionService;
    @Injected
    private ApplicationService applicationService;
    @Injected
    private UtilService utilService;

    private final Logger logger = LoggerFactory.getLogger(GetDirectionCommand.class);

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        try {
            Long id = Verifier.convertToLong(request.getParameter(PARAM_NAMES.getString("id")));

            String offs = request.getParameter(PARAM_NAMES.getString("offset"));
            String coun = request.getParameter(PARAM_NAMES.getString("count"));
            Long offset = offs == null ? 0 : Verifier.convertToLong(offs);
            Long count = coun == null ? 3 : Verifier.convertToLong(coun);

            Verifier.verifyId(id);
            Verifier.verifyNonNegative(offset);
            Verifier.verifyNonNegative(count);

            Direction direction = directionService.get(id);
            Map<Long, String> userNames = directionService.getUserNames(id);
            List<Application> applications = applicationService.getByDirection(id, offset, count);
            Long totalCount = applicationService.getCount(id);

            request.setAttribute(PARAM_NAMES.getString("direction"), direction);
            request.setAttribute(PARAM_NAMES.getString("userNames"), userNames);
            request.setAttribute(PARAM_NAMES.getString("applications"), applications);
            request.setAttribute(PARAM_NAMES.getString("offset"), offset);
            request.setAttribute(PARAM_NAMES.getString("count"), count);
            request.setAttribute(PARAM_NAMES.getString("totalCount"), totalCount);

            List<Subject> allSubjects = utilService.getAllSubjects();
            Map<Long, Subject> subjectsMap = new HashMap<>();
            allSubjects.forEach(subject -> subjectsMap.put(subject.getId(), subject));

            request.setAttribute(PARAM_NAMES.getString("subjects"), subjectsMap);

            request.getRequestDispatcher(PAGE_NAMES.getString("page.direction")).forward(request, response);
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