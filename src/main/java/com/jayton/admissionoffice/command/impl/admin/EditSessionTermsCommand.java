package com.jayton.admissionoffice.command.impl.admin;

import com.jayton.admissionoffice.command.Command;
import com.jayton.admissionoffice.model.to.SessionTerms;
import com.jayton.admissionoffice.service.ServiceFactory;
import com.jayton.admissionoffice.service.UtilService;
import com.jayton.admissionoffice.service.exception.ServiceException;
import com.jayton.admissionoffice.util.proxy.HttpServletRequestProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;

public class EditSessionTermsCommand implements Command {

    private final Logger logger = LoggerFactory.getLogger(EditSessionTermsCommand.class);

    @Override
    public String execute(HttpServletRequestProxy request) {
        try {
            LocalDateTime current = LocalDateTime.now();
            short currentYear = (short)current.getYear();
            request.setAttribute(PARAM_NAMES.getString("year"), currentYear);

            UtilService utilService = ServiceFactory.getInstance().getUtilService();
            SessionTerms currentTerms = utilService.getSessionTerms(currentYear);

            if(currentTerms != null) {
                request.setAttribute("isNew", false);
                request.setAttribute(PARAM_NAMES.getString("sessionStart"), currentTerms.getSessionStart());
                request.setAttribute(PARAM_NAMES.getString("sessionEnd"), currentTerms.getSessionEnd());
            } else {
                request.setAttribute("isNew", true);
            }

            return PAGE_NAMES.getString("page.session_terms.edit");
        } catch (ServiceException e) {
            logger.error("Exception is caught.", e);
            request.setAttribute(PARAM_NAMES.getString("exception"), e);
            return PAGE_NAMES.getString("page.exception");
        }
    }
}