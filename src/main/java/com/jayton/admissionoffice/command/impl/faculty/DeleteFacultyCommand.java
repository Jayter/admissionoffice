package com.jayton.admissionoffice.command.impl.faculty;

import com.jayton.admissionoffice.command.Command;
import com.jayton.admissionoffice.command.exception.VerificationException;
import com.jayton.admissionoffice.command.util.Verifier;
import com.jayton.admissionoffice.service.FacultyService;
import com.jayton.admissionoffice.service.ServiceFactory;
import com.jayton.admissionoffice.service.exception.ServiceException;
import com.jayton.admissionoffice.util.proxy.HttpServletRequestProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DeleteFacultyCommand implements Command {

    private final Logger logger = LoggerFactory.getLogger(DeleteFacultyCommand.class);

    @Override
    public String execute(HttpServletRequestProxy request) {
        try {
            Long id = Long.parseLong(request.getParameter(PARAM_NAMES.getString("id")));
            Verifier.verifyId(id);

            FacultyService facultyService = ServiceFactory.getInstance().getFacultyService();
            facultyService.delete(id);

            return PAGE_NAMES.getString("controller.load_main");
        } catch (ServiceException | VerificationException | NumberFormatException e) {
            logger.error("Exception is caught.", e);
            request.setAttribute(PARAM_NAMES.getString("exception"), e);
            return PAGE_NAMES.getString("page.exception");
        }
    }
}