package com.jayton.admissionoffice.command.impl.admin;

import com.jayton.admissionoffice.command.Command;
import com.jayton.admissionoffice.command.exception.ShownException;
import com.jayton.admissionoffice.model.to.SessionTerms;
import com.jayton.admissionoffice.service.ServiceFactory;
import com.jayton.admissionoffice.service.UtilService;
import com.jayton.admissionoffice.service.exception.ServiceException;
import com.jayton.admissionoffice.util.proxy.HttpServletRequestProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;

public class HandleApplicationsCommand implements Command {

    private final Logger logger = LoggerFactory.getLogger(HandleApplicationsCommand.class);

    @Override
    public String execute(HttpServletRequestProxy request) {
        try {
            UtilService utilService = ServiceFactory.getInstance().getUtilService();

            LocalDateTime current = LocalDateTime.now();
            SessionTerms terms = utilService.getSessionTerms((short)current.getYear());

            LocalDateTime sessionEnd = terms.getSessionEnd();

            if(current.isBefore(sessionEnd)) {
                logger.error("Failed to count handle results.");
                request.setAttribute(PARAM_NAMES.getString("shownException"),
                        new ShownException("Can not count results unless admission session expires."));
            } else {
                utilService.handleApplications();
            }

            return PAGE_NAMES.getString("page.admin");
        } catch (ServiceException e) {
            logger.error("Exception is caught.", e);
            request.setAttribute(PARAM_NAMES.getString("exception"), e);
            return PAGE_NAMES.getString("page.exception");
        }
    }
}