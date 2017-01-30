package com.jayton.admissionoffice.command.impl.util;

import com.jayton.admissionoffice.command.Command;
import com.jayton.admissionoffice.command.exception.VerificationException;
import com.jayton.admissionoffice.command.util.CommandUtils;
import com.jayton.admissionoffice.command.util.Verifier;
import com.jayton.admissionoffice.model.to.PaginationDto;
import com.jayton.admissionoffice.model.to.SessionTerms;
import com.jayton.admissionoffice.model.university.University;
import com.jayton.admissionoffice.service.UniversityService;
import com.jayton.admissionoffice.service.UtilService;
import com.jayton.admissionoffice.service.exception.ServiceException;
import com.jayton.admissionoffice.util.di.Injected;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;

public class LoadMainPageCommand implements Command {

    @Injected
    private UniversityService universityService;

    @Injected
    private UtilService utilService;

    private final Logger logger = LoggerFactory.getLogger(LoadMainPageCommand.class);

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        try {
            String city = request.getParameter(PARAM_NAMES.getString("city"));
            String pageParam = request.getParameter(PARAM_NAMES.getString("page"));
            String countParam = request.getParameter(PARAM_NAMES.getString("count"));
            Long page = pageParam == null ? 1 : Verifier.convertToLong(pageParam);
            Long countPerPage = countParam == null ? 5 : Verifier.convertToLong(countParam);

            Long offset = (page - 1) * countPerPage;

            Verifier.verifyNonNegative(page);
            Verifier.verifyNonNegative(countPerPage);

            PaginationDto<University> dto;
            if(city == null || city.isEmpty()) {
                dto = universityService.getWithCount(offset, countPerPage);
            } else {
                dto = universityService.getWithCountByCity(city, offset, countPerPage);
                request.setAttribute(PARAM_NAMES.getString("city"), city);
            }

            Long totalPagesCount = CommandUtils.getTotalCountOfPages(dto.getCount(), countPerPage);

            request.setAttribute(PARAM_NAMES.getString("universities"), dto.getEntries());
            request.setAttribute(PARAM_NAMES.getString("page"), page);
            request.setAttribute(PARAM_NAMES.getString("count"), countPerPage);
            request.setAttribute(PARAM_NAMES.getString("pagesCount"), totalPagesCount);

            SessionTerms terms = utilService.getSessionTerms((short) LocalDate.now().getYear());
            request.setAttribute(PARAM_NAMES.getString("sessionTerms"), terms);

            request.getRequestDispatcher(PAGE_NAMES.getString("page.main")).forward(request, response);
        } catch (ServiceException | VerificationException e) {
            logger.error("Exception is caught.", e);
            request.setAttribute(PARAM_NAMES.getString("exception"), e);
            request.getRequestDispatcher(PAGE_NAMES.getString("page.exception")).forward(request, response);
        }
    }
}