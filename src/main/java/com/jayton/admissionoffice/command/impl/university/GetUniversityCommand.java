package com.jayton.admissionoffice.command.impl.university;

import com.jayton.admissionoffice.command.Command;
import com.jayton.admissionoffice.command.exception.VerificationException;
import com.jayton.admissionoffice.command.util.CommandUtils;
import com.jayton.admissionoffice.command.util.Verifier;
import com.jayton.admissionoffice.model.to.PaginationDto;
import com.jayton.admissionoffice.model.university.Faculty;
import com.jayton.admissionoffice.model.university.University;
import com.jayton.admissionoffice.service.FacultyService;
import com.jayton.admissionoffice.service.UniversityService;
import com.jayton.admissionoffice.service.exception.ServiceException;
import com.jayton.admissionoffice.util.di.Injected;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class GetUniversityCommand implements Command {

    @Injected
    private UniversityService universityService;
    @Injected
    private FacultyService facultyService;

    private final Logger logger = LoggerFactory.getLogger(GetUniversityCommand.class);

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

            University university = universityService.get(id);
            Verifier.verifyObject(university);

            PaginationDto<Faculty> dto = facultyService.getWithCountByUniversity(id, offset, countPerPage);

            Long totalPagesCount = CommandUtils.getTotalCountOfPages(dto.getCount(), countPerPage);

            request.setAttribute(PARAM_NAMES.getString("university"), university);
            request.setAttribute(PARAM_NAMES.getString("faculties"), dto.getEntries());
            request.setAttribute(PARAM_NAMES.getString("page"), page);
            request.setAttribute(PARAM_NAMES.getString("count"), countPerPage);
            request.setAttribute(PARAM_NAMES.getString("pagesCount"), totalPagesCount);

            request.getRequestDispatcher(PAGE_NAMES.getString("page.university")).forward(request, response);
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