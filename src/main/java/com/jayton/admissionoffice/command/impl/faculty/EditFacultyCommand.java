package com.jayton.admissionoffice.command.impl.faculty;

import com.jayton.admissionoffice.command.Command;
import com.jayton.admissionoffice.command.exception.VerificationException;
import com.jayton.admissionoffice.command.util.Verifier;
import com.jayton.admissionoffice.model.university.Faculty;
import com.jayton.admissionoffice.service.FacultyService;
import com.jayton.admissionoffice.service.ServiceFactory;
import com.jayton.admissionoffice.service.exception.ServiceException;
import com.jayton.admissionoffice.util.proxy.HttpServletRequestProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EditFacultyCommand implements Command {

    private final Logger logger = LoggerFactory.getLogger(EditFacultyCommand.class);

    @Override
    public String execute(HttpServletRequestProxy request) {
        try {
            String id = request.getParameter(PARAM_NAMES.getString("id"));

            if(id != null && !id.isEmpty()) {
                Long facultyId = Long.parseLong(id);
                Verifier.verifyId(facultyId);

                FacultyService facultyService = ServiceFactory.getInstance().getFacultyService();

                Faculty faculty = facultyService.get(facultyId);

                request.setAttribute(PARAM_NAMES.getString("id"), faculty.getId());
                request.setAttribute(PARAM_NAMES.getString("universityId"), faculty.getUniversityId());
                request.setAttribute(PARAM_NAMES.getString("name"), faculty.getName());
                request.setAttribute(PARAM_NAMES.getString("phone"), faculty.getOfficePhone());
                request.setAttribute(PARAM_NAMES.getString("email"), faculty.getOfficeEmail());
                request.setAttribute(PARAM_NAMES.getString("address"), faculty.getOfficeAddress());
            } else {
                Long universityId = Long.parseLong(request.getParameter(PARAM_NAMES.getString("universityId")));
                Verifier.verifyId(universityId);
                request.setAttribute(PARAM_NAMES.getString("universityId"), universityId);
            }

            return PAGE_NAMES.getString("page.faculty.edit");
        } catch (ServiceException | VerificationException | NumberFormatException e) {
            logger.error("Exception is caught.", e);
            request.setAttribute(PARAM_NAMES.getString("exception"), e);
            return PAGE_NAMES.getString("page.exception");
        }
    }
}