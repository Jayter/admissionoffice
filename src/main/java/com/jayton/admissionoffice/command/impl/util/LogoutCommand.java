package com.jayton.admissionoffice.command.impl.util;

import com.jayton.admissionoffice.command.Command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class LogoutCommand implements Command {

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        session.invalidate();

        String referer = getReferer(request);

        response.sendRedirect(referer);
    }
}