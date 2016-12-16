package com.jayton.admissionoffice.command.impl.university;

import com.jayton.admissionoffice.command.Command;
import com.jayton.admissionoffice.command.exception.VerificationException;
import com.jayton.admissionoffice.command.util.Verifier;
import com.jayton.admissionoffice.model.university.University;
import com.jayton.admissionoffice.service.ServiceFactory;
import com.jayton.admissionoffice.service.UniversityService;
import com.jayton.admissionoffice.service.exception.ServiceException;
import com.jayton.admissionoffice.util.proxy.HttpServletRequestProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EditUniversityCommand implements Command {

    private final Logger logger = LoggerFactory.getLogger(EditUniversityCommand.class);

    @Override
    public String execute(HttpServletRequestProxy request) {
        try {
            String id = request.getParameter(PARAM_NAMES.getString("id"));

            if(id != null && !id.isEmpty()) {
                Long universityId = Long.parseLong(id);
                Verifier.verifyId(universityId);

                UniversityService service = ServiceFactory.getInstance().getUniversityService();

                University university = service.get(universityId);
                request.setAttribute(PARAM_NAMES.getString("id"), university.getId());
                request.setAttribute(PARAM_NAMES.getString("name"), university.getName());
                request.setAttribute(PARAM_NAMES.getString("city"), university.getCity());
                request.setAttribute(PARAM_NAMES.getString("address"), university.getAddress());
            }

            return PAGE_NAMES.getString("page.university.edit");
        } catch (ServiceException | VerificationException | NumberFormatException e) {
            logger.error("Exception is caught.", e);
            request.setAttribute(PARAM_NAMES.getString("exception"), e);
            return PAGE_NAMES.getString("page.exception");
        }
    }
}