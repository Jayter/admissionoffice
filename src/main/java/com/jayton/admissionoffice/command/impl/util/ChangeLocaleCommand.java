package com.jayton.admissionoffice.command.impl.util;

import com.jayton.admissionoffice.command.Command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class ChangeLocaleCommand implements Command {

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();

        String newLocale = request.getParameter(PARAM_NAMES.getString("locale"));
        session.setAttribute(PARAM_NAMES.getString("locale"), newLocale);

        String referer = getReferer(request);
        response.sendRedirect(referer);
    }
}