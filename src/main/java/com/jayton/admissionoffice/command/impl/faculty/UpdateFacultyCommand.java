package com.jayton.admissionoffice.command.impl.faculty;

import com.jayton.admissionoffice.command.Command;
import com.jayton.admissionoffice.command.exception.ShownException;
import com.jayton.admissionoffice.command.exception.VerificationException;
import com.jayton.admissionoffice.command.util.Verifier;
import com.jayton.admissionoffice.model.university.Faculty;
import com.jayton.admissionoffice.service.FacultyService;
import com.jayton.admissionoffice.service.ServiceFactory;
import com.jayton.admissionoffice.service.exception.ServiceException;
import com.jayton.admissionoffice.util.proxy.HttpServletRequestProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UpdateFacultyCommand implements Command {

    private final Logger logger = LoggerFactory.getLogger(UpdateFacultyCommand.class);

    @Override
    public String execute(HttpServletRequestProxy request) {
        try {
            Long id = Long.parseLong(request.getParameter(PARAM_NAMES.getString("id")));
            String name = request.getParameter(PARAM_NAMES.getString("name"));
            String phone = request.getParameter(PARAM_NAMES.getString("phone"));
            String email = request.getParameter(PARAM_NAMES.getString("email"));
            String address = request.getParameter(PARAM_NAMES.getString("address"));
            Long universityId = Long.parseLong(request.getParameter(PARAM_NAMES.getString("universityId")));

            verifyInput(id, name, phone, email, address, universityId);

            FacultyService facultyService = ServiceFactory.getInstance().getFacultyService();
            facultyService.update(new Faculty(id, name, phone, email, address, universityId));

            return PAGE_NAMES.getString("controller.get_faculty");
        } catch (VerificationException e) {
            logger.error("Incorrect data.", e);
            request.setAttribute(PARAM_NAMES.getString("shownException"), new ShownException(e.getMessage()));
            return PAGE_NAMES.getString("controller.edit_faculty");
        } catch (ServiceException | NumberFormatException e) {
            logger.error("Exception is caught.", e);
            request.setAttribute(PARAM_NAMES.getString("exception"), e);
            return PAGE_NAMES.getString("page.exception");
        }
    }

    private void verifyInput(Long id, String name, String phone, String email, String address, Long universityId)
            throws VerificationException {
        Verifier.verifyIds(id, universityId);
        Verifier.verifyStrings(name, phone, address);
        Verifier.verifyEmail(email);
    }
}