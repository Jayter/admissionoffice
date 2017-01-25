package com.jayton.admissionoffice.command.impl.util;

import com.jayton.admissionoffice.command.Command;
import com.jayton.admissionoffice.command.exception.VerificationException;
import com.jayton.admissionoffice.command.util.Verifier;
import com.jayton.admissionoffice.model.university.University;
import com.jayton.admissionoffice.service.UniversityService;
import com.jayton.admissionoffice.service.exception.ServiceException;
import com.jayton.admissionoffice.util.di.Injected;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class LoadMainPageCommand implements Command {

    @Injected
    private UniversityService universityService;

    private final Logger logger = LoggerFactory.getLogger(LoadMainPageCommand.class);

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        try {
            String offs = request.getParameter(PARAM_NAMES.getString("offset"));
            String coun = request.getParameter(PARAM_NAMES.getString("count"));
            Long offset = offs == null ? 0 : Verifier.convertToLong(offs);
            Long count = coun == null ? 3 : Verifier.convertToLong(coun);

            Verifier.verifyNonNegative(offset);
            Verifier.verifyNonNegative(count);

            List<University> universities = universityService.getAll(offset, count);
            Long totalCount = universityService.getTotalCount();

            request.setAttribute(PARAM_NAMES.getString("universities"), universities);
            request.setAttribute(PARAM_NAMES.getString("offset"), offset);
            request.setAttribute(PARAM_NAMES.getString("count"), count);
            request.setAttribute(PARAM_NAMES.getString("totalCount"), totalCount);

            request.getRequestDispatcher(PAGE_NAMES.getString("page.main")).forward(request, response);
        } catch (ServiceException | VerificationException e) {
            logger.error("Exception is caught.", e);
            request.setAttribute(PARAM_NAMES.getString("exception"), e);
            request.getRequestDispatcher(PAGE_NAMES.getString("page.exception")).forward(request, response);
        }
    }
}