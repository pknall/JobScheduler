package com.ccgautomation.servlets;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JobStatusServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        response.setContentType("text/html");
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("<html>");
        stringBuilder.append("<head>");
        stringBuilder.append("<title>Job Status</title>");
        stringBuilder.append("</head>");
        stringBuilder.append("<body>");
        stringBuilder.append("<h1>Job Status:</h1>");
        stringBuilder.append("</body>");
        stringBuilder.append("</html>");

        response.getWriter().write(stringBuilder.toString());
    }
}
