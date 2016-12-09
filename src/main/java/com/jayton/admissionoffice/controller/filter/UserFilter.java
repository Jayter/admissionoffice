package com.jayton.admissionoffice.controller.filter;

import com.jayton.admissionoffice.command.PageNames;
import com.jayton.admissionoffice.controller.CommandHelper;
import com.jayton.admissionoffice.controller.CommandName;

import static com.jayton.admissionoffice.controller.CommandName.*;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jayton on 07.12.2016.
 */
public class UserFilter implements Filter {

    private String COMMAND = "command";
    private String EXCEPTION = "exception";
    private String ACCESS_DENIED = "Access denied.";

    private List<CommandName> adminCommands;
    private List<CommandName> userCommands;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        adminCommands = new ArrayList<>();
        userCommands = new ArrayList<>();

        adminCommands.add(CREATE_USER);
        adminCommands.add(EDIT_USER);
        adminCommands.add(UPDATE_USER);
        adminCommands.add(DELETE_USER);
        adminCommands.add(ADD_USER_RESULT);
        adminCommands.add(DELETE_USER_RESULT);
        adminCommands.add(CREATE_UNIVERSITY);
        adminCommands.add(EDIT_UNIVERSITY);
        adminCommands.add(UPDATE_UNIVERSITY);
        adminCommands.add(DELETE_UNIVERSITY);
        adminCommands.add(CREATE_FACULTY);
        adminCommands.add(EDIT_FACULTY);
        adminCommands.add(UPDATE_FACULTY);
        adminCommands.add(DELETE_FACULTY);
        adminCommands.add(CREATE_DIRECTION);
        adminCommands.add(EDIT_DIRECTION);
        adminCommands.add(UPDATE_DIRECTION);
        adminCommands.add(DELETE_DIRECTION);
        adminCommands.add(ADD_ENTRANCE_SUBJECT);
        adminCommands.add(DELETE_ENTRANCE_SUBJECT);

        userCommands.add(USER_APPLY);
        userCommands.add(USER_CANCEL_APPLICATION);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String commandName = request.getParameter(COMMAND);

        if(commandName == null || commandName.isEmpty()) {
            chain.doFilter(request, servletResponse);
        }

        CommandName command = CommandHelper.getInstance().getCommandName(commandName);
        if(adminCommands.contains(command) || userCommands.contains(command)) {
            HttpSession session = request.getSession(false);
            if(session != null) {
                Boolean isAuthorizedAdmin = (Boolean) session.getAttribute("isAuthorizedAdmin");
                Boolean isAuthorizedUser = (Boolean) session.getAttribute("isAuthorizedUser");
                if(isAuthorizedAdmin == null || isAuthorizedUser == null) {
                    request.setAttribute(EXCEPTION, new IllegalAccessException(ACCESS_DENIED));
                    request.getRequestDispatcher(PageNames.EXCEPTION).forward(request, servletResponse);
                }

                boolean isAdminCommandViolated = adminCommands.contains(command) && !isAuthorizedAdmin;
                boolean isUserCommandViolated = userCommands.contains(command) && !isAuthorizedUser;

                 if(isAdminCommandViolated || isUserCommandViolated) {
                    request.setAttribute(EXCEPTION, new IllegalAccessException(ACCESS_DENIED));
                    request.getRequestDispatcher(PageNames.EXCEPTION).forward(request, servletResponse);
                }
            } else {
                request.setAttribute(EXCEPTION, new IllegalAccessException(ACCESS_DENIED));
                request.getRequestDispatcher(PageNames.EXCEPTION).forward(request, servletResponse);
            }
        }

        chain.doFilter(request, servletResponse);
    }

    @Override
    public void destroy() {
        //nothing to destroy
    }
}