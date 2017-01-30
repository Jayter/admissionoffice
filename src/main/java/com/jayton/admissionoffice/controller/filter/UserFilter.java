package com.jayton.admissionoffice.controller.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.*;

public class UserFilter implements Filter {

    private final Logger logger = LoggerFactory.getLogger(UserFilter.class);

    private static final String COMMAND = "command";
    private static final String EXCEPTION_PAGE = "error.jsp";
    private static final String EXCEPTION = "exception";
    private static final String ACCESS_DENIED = "Access denied.";
    private static final String ADMIN = "admin";
    private static final String USER = "user";

    private List<String> adminCommands;
    private List<String> userCommands;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        adminCommands = new ArrayList<>();
        userCommands = new ArrayList<>();

        String path = filterConfig.getInitParameter("commandsPath");
        ResourceBundle commands = ResourceBundle.getBundle(path);
        Set<String> commandKeys = commands.keySet();

        for(String key: commandKeys) {
            if(key.startsWith(ADMIN)) {
                adminCommands.add(commands.getString(key));
            }
            if(key.startsWith(USER)) {
                userCommands.add(commands.getString(key));
            }
        }
        logger.info("User filter is initialized.");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String command = request.getParameter(COMMAND);

        if(command == null || command.isEmpty()) {
            chain.doFilter(request, servletResponse);
            return;
        }

        if(adminCommands.contains(command) || userCommands.contains(command)) {
            HttpSession session = request.getSession(false);
            if(session != null) {
                Boolean isAuthorizedAdmin = (Boolean) session.getAttribute("isAuthorizedAdmin");
                Boolean isAuthorizedUser = (Boolean) session.getAttribute("isAuthorizedUser");
                if(isAuthorizedAdmin == null && isAuthorizedUser == null) {
                    request.setAttribute(EXCEPTION, new IllegalAccessException(ACCESS_DENIED));
                    request.getRequestDispatcher(EXCEPTION_PAGE).forward(request, servletResponse);
                    logger.warn("Illegal request is intercepted by UserFilter.");
                    return;
                }

                boolean isAdminCommandViolated = adminCommands.contains(command) && !isAuthorizedAdmin;
                boolean isUserCommandViolated = userCommands.contains(command) && !isAuthorizedUser;

                if(isAdminCommandViolated || isUserCommandViolated) {
                    request.setAttribute(EXCEPTION, new IllegalAccessException(ACCESS_DENIED));
                    request.getRequestDispatcher(EXCEPTION_PAGE).forward(request, servletResponse);
                    logger.warn("Illegal request is intercepted by UserFilter.");
                    return;
                }
            } else {
                request.setAttribute(EXCEPTION, new IllegalAccessException(ACCESS_DENIED));
                request.getRequestDispatcher(EXCEPTION_PAGE).forward(request, servletResponse);
                logger.warn("Illegal request is intercepted by UserFilter.");
                return;
            }
        }
        chain.doFilter(request, servletResponse);
    }

    @Override
    public void destroy() {
        logger.info("User filter is destroyed.");
    }
}