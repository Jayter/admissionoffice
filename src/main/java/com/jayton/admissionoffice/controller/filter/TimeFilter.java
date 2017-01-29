package com.jayton.admissionoffice.controller.filter;

import com.jayton.admissionoffice.command.exception.VerificationException;
import com.jayton.admissionoffice.model.to.SessionTerms;
import com.jayton.admissionoffice.service.UtilService;
import com.jayton.admissionoffice.service.exception.ServiceException;
import com.jayton.admissionoffice.util.di.BeanContext;
import com.jayton.admissionoffice.util.di.BeanContextHolder;
import com.jayton.admissionoffice.util.di.Injected;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

public class TimeFilter implements Filter {

    private static final String COMMAND = "command";
    private static final String EXCEPTION_PAGE = "error.jsp";
    private static final String EXCEPTION = "exception";
    private static final String BEYOND = "beyond";
    private static final String WITHIN = "within";
    private static final String BEFORE = "before";
    private static final String AFTER = "after";

    private List<String> onlyBeyondSessionCommands;
    private List<String> onlyWithinSessionCommands;
    private List<String> onlyBeforeSessionCommands;
    private List<String> onlyAfterSessionCommands;

    @Injected
    private UtilService utilService;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        BeanContext context = BeanContextHolder.getInstance().getActualContext();
        utilService = (UtilService) context.getBean("utilService");

        onlyBeyondSessionCommands = new ArrayList<>();
        onlyWithinSessionCommands = new ArrayList<>();
        onlyBeforeSessionCommands = new ArrayList<>();
        onlyAfterSessionCommands = new ArrayList<>();

        String path = filterConfig.getInitParameter("commandsPath");
        ResourceBundle commands = ResourceBundle.getBundle(path);
        Set<String> commandKeys = commands.keySet();

        for(String key: commandKeys) {
            if(key.contains(BEYOND)) {
                onlyBeyondSessionCommands.add(commands.getString(key));
            }
            if (key.contains(WITHIN)) {
                onlyWithinSessionCommands.add(commands.getString(key));
            }
            if(key.contains(BEFORE)) {
                onlyBeforeSessionCommands.add(commands.getString(key));
            }
            if(key.contains(AFTER)) {
                onlyAfterSessionCommands.add(commands.getString(key));
            }
        }
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String command = request.getParameter(COMMAND);

        if(command == null || command.isEmpty()) {
            chain.doFilter(request, servletResponse);
            return;
        }

        LocalDateTime current = LocalDateTime.now();
        SessionTerms terms = null;
        try {
             terms = utilService.getSessionTerms((short)current.getYear());
        } catch (ServiceException e) {
            request.setAttribute(EXCEPTION, new RuntimeException("Internal server error."));
            request.getRequestDispatcher(EXCEPTION_PAGE).forward(request, servletResponse);
        }

        try {
            verifyCommand(command, terms, current);
        } catch (VerificationException e) {
            request.setAttribute(EXCEPTION, e);
            request.getRequestDispatcher(EXCEPTION_PAGE).forward(request, servletResponse);
        }
        chain.doFilter(request, servletResponse);
    }

    private void verifyCommand(String command, SessionTerms terms, LocalDateTime current) throws VerificationException {
        if(onlyBeforeSessionCommands.contains(command)) {
            if(terms != null && !current.isBefore(terms.getSessionStart())) {
                throw new VerificationException("Operation is unavailable until application session starts.");
            }
        }
        if(onlyAfterSessionCommands.contains(command)) {
            if(terms == null || current.isBefore(terms.getSessionEnd())) {
                throw new VerificationException("Operation is unavailable until application session ends.");
            }
        }
        if(onlyWithinSessionCommands.contains(command)) {
            if(terms == null || !(current.isBefore(terms.getSessionEnd()) && current.isAfter(terms.getSessionStart()))) {
                throw new VerificationException("Operation is unavailable beyond application session period.");
            }
        }
        if(onlyBeyondSessionCommands.contains(command)) {
            if(terms != null && (current.isBefore(terms.getSessionEnd()) && current.isAfter(terms.getSessionStart()))) {
                throw new VerificationException("Operation is unavailable within application session period.");
            }
        }
    }

    @Override
    public void destroy() {
    }
}