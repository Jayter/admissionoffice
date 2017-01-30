package com.jayton.admissionoffice.command.impl.direction;

import com.jayton.admissionoffice.command.Command;
import com.jayton.admissionoffice.command.exception.VerificationException;
import com.jayton.admissionoffice.command.util.CommandUtils;
import com.jayton.admissionoffice.command.util.Verifier;
import com.jayton.admissionoffice.model.Subject;
import com.jayton.admissionoffice.model.to.ApplicationDto;
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

            String pageParam = request.getParameter(PARAM_NAMES.getString("page"));
            String countParam = request.getParameter(PARAM_NAMES.getString("count"));
            Long page = pageParam == null ? 1 : Verifier.convertToLong(pageParam);
            Long countPerPage = countParam == null ? 5 : Verifier.convertToLong(countParam);

            Long offset = (page - 1) * countPerPage;

            Verifier.verifyId(id);
            Verifier.verifyNonNegative(page);
            Verifier.verifyNonNegative(countPerPage);

            Direction direction = directionService.get(id);
            Verifier.verifyObject(direction);

            ApplicationDto dto = applicationService.getByDirection(id, offset, countPerPage);
            Long totalPagesCount = CommandUtils.getTotalCountOfPages(dto.getCount(), countPerPage);

            request.setAttribute(PARAM_NAMES.getString("direction"), direction);
            request.setAttribute(PARAM_NAMES.getString("userNames"), dto.getUserNames());
            request.setAttribute(PARAM_NAMES.getString("applications"), dto.getApplications());
            request.setAttribute(PARAM_NAMES.getString("page"), page);
            request.setAttribute(PARAM_NAMES.getString("count"), countPerPage);
            request.setAttribute(PARAM_NAMES.getString("pagesCount"), totalPagesCount);

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