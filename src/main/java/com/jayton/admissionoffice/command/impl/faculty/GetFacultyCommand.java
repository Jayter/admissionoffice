package com.jayton.admissionoffice.command.impl.faculty;

import com.jayton.admissionoffice.command.Command;
import com.jayton.admissionoffice.command.exception.VerificationException;
import com.jayton.admissionoffice.command.util.Verifier;
import com.jayton.admissionoffice.model.university.Direction;
import com.jayton.admissionoffice.model.university.Faculty;
import com.jayton.admissionoffice.service.DirectionService;
import com.jayton.admissionoffice.service.FacultyService;
import com.jayton.admissionoffice.service.ServiceFactory;
import com.jayton.admissionoffice.service.exception.ServiceException;
import com.jayton.admissionoffice.util.proxy.HttpServletRequestProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class GetFacultyCommand implements Command {

    private final Logger logger = LoggerFactory.getLogger(GetFacultyCommand.class);

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

            FacultyService facultyService = ServiceFactory.getInstance().getFacultyService();
            DirectionService directionService = ServiceFactory.getInstance().getDirectionService();

            Faculty faculty = facultyService.get(id);
            List<Direction> directions = directionService.getByFaculty(id, offset, count);
            long totalCount = directionService.getCount(id);

            request.setAttribute(PARAM_NAMES.getString("faculty"), faculty);
            request.setAttribute(PARAM_NAMES.getString("directions"), directions);
            request.setAttribute(PARAM_NAMES.getString("offset"), offset);
            request.setAttribute(PARAM_NAMES.getString("count"), count);
            request.setAttribute(PARAM_NAMES.getString("totalCount"), totalCount);

            return PAGE_NAMES.getString("page.faculty");
        } catch (ServiceException | VerificationException | NumberFormatException e) {
            logger.error("Exception is caught.", e);
            request.setAttribute(PARAM_NAMES.getString("exception"), e);
            return PAGE_NAMES.getString("page.exception");
        }
    }
}