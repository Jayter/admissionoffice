package com.jayton.admissionoffice.command.impl.university;

import com.jayton.admissionoffice.command.Command;
import com.jayton.admissionoffice.command.exception.ShownException;
import com.jayton.admissionoffice.command.exception.VerificationException;
import com.jayton.admissionoffice.command.util.Verifier;
import com.jayton.admissionoffice.model.university.Faculty;
import com.jayton.admissionoffice.model.university.University;
import com.jayton.admissionoffice.service.FacultyService;
import com.jayton.admissionoffice.service.ServiceFactory;
import com.jayton.admissionoffice.service.UniversityService;
import com.jayton.admissionoffice.service.exception.ServiceException;
import com.jayton.admissionoffice.util.proxy.HttpServletRequestProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class UpdateUniversityCommand implements Command {

    private final Logger logger = LoggerFactory.getLogger(UpdateUniversityCommand.class);

    @Override
    public String execute(HttpServletRequestProxy request) {
        try {
            Long id = Long.parseLong(request.getParameter(PARAM_NAMES.getString("id")));
            String name = request.getParameter(PARAM_NAMES.getString("name"));
            String city = request.getParameter(PARAM_NAMES.getString("city"));
            String address = request.getParameter(PARAM_NAMES.getString("address"));

            Verifier.verifyId(id);
            Verifier.verifyStrings(name, city, address);

            UniversityService universityService = ServiceFactory.getInstance().getUniversityService();
            University university = universityService.update(new University(id, name, city, address));

            long offset = 0;
            long count = 3;

            FacultyService facultyService = ServiceFactory.getInstance().getFacultyService();
            List<Faculty> faculties = facultyService.getByUniversity(id, 0, 3);
            long totalCount = facultyService.getCount(university.getId());

            request.setAttribute(PARAM_NAMES.getString("university"), university);
            request.setAttribute(PARAM_NAMES.getString("faculties"), faculties);
            request.setAttribute(PARAM_NAMES.getString("offset"), offset);
            request.setAttribute(PARAM_NAMES.getString("count"), count);
            request.setAttribute(PARAM_NAMES.getString("totalCount"), totalCount);

            return PAGE_NAMES.getString("page.university");
        } catch (VerificationException e) {
            logger.error("Incorrect data.", e);
            request.setAttribute(PARAM_NAMES.getString("shownException"), new ShownException(e.getMessage()));
            return PAGE_NAMES.getString("controller.edit_university");
        } catch (ServiceException | NumberFormatException e) {
            logger.error("Exception is caught.", e);
            request.setAttribute(PARAM_NAMES.getString("exception"), e);
            return PAGE_NAMES.getString("page.exception");
        }
    }
}