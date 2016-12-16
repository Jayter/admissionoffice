package com.jayton.admissionoffice.command.impl.university;

import com.jayton.admissionoffice.command.Command;
import com.jayton.admissionoffice.command.exception.ShownException;
import com.jayton.admissionoffice.command.exception.VerificationException;
import com.jayton.admissionoffice.command.util.Verifier;
import com.jayton.admissionoffice.model.university.University;
import com.jayton.admissionoffice.service.ServiceFactory;
import com.jayton.admissionoffice.service.UniversityService;
import com.jayton.admissionoffice.service.exception.ServiceException;
import com.jayton.admissionoffice.util.proxy.HttpServletRequestProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CreateUniversityCommand implements Command {

    private final Logger logger = LoggerFactory.getLogger(CreateUniversityCommand.class);

    @Override
    public String execute(HttpServletRequestProxy request) {
        try {
            String name = request.getParameter(PARAM_NAMES.getString("name"));
            String city = request.getParameter(PARAM_NAMES.getString("city"));
            String address = request.getParameter(PARAM_NAMES.getString("address"));

            Verifier.verifyStrings(name, city, address);

            UniversityService universityService = ServiceFactory.getInstance().getUniversityService();
            University university = universityService.add(new University(name, city, address));

            request.setAttribute(PARAM_NAMES.getString("university"), university);

            return PAGE_NAMES.getString("page.university");
        } catch (VerificationException e) {
            logger.error("Incorrect data.", e);
            request.setAttribute(PARAM_NAMES.getString("shownException"), new ShownException(e.getMessage()));
            return PAGE_NAMES.getString("controller.edit_university");
        } catch (ServiceException e) {
            logger.error("Exception is caught.", e);
            request.setAttribute(PARAM_NAMES.getString("exception"), e);
            return PAGE_NAMES.getString("page.exception");
        }
    }
}