package com.aem.geeks.core.servlets;

import java.io.IOException;
import java.security.SecureRandom;

import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.servlets.annotations.SlingServletPaths;
import org.osgi.service.component.annotations.Component;
@Component(service=Servlet.class)
@SlingServletPaths(value="/bin/task5/example")
public class ServletExample extends SlingSafeMethodsServlet {
    private static final long serialVersionUID = 1L;
    private static final String NUMBERS = "0123456789";
    private static final String LETTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static final String ALPHANUMERIC = NUMBERS + LETTERS;
    private static final SecureRandom RANDOM = new SecureRandom();

    @Override
    protected void doGet(SlingHttpServletRequest req, SlingHttpServletResponse res)
            throws ServletException, IOException {
                String type = req.getParameter("type");
                String result = "";

        if ("Number".equalsIgnoreCase(type)) {
            result = generateRandomString(NUMBERS, 6);
        } else if ("Letters".equalsIgnoreCase(type)) {
            result = generateRandomString(LETTERS, 6);
        } else if ("Random".equalsIgnoreCase(type)) {
            result = generateRandomAlphanumericString(3, 3);
        }

        res.setContentType("application/json");
        res.getWriter().write("{\"result\":\"" + result + "\"}");
    }

    private String generateRandomString(String characters, int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(characters.charAt(RANDOM.nextInt(characters.length())));
        }
        return sb.toString();
    }

    private String generateRandomAlphanumericString(int numCount, int letterCount) {
        StringBuilder sb = new StringBuilder(numCount + letterCount);
        for (int i = 0; i < numCount; i++) {
            sb.append(NUMBERS.charAt(RANDOM.nextInt(NUMBERS.length())));
        }
        for (int i = 0; i < letterCount; i++) {
            sb.append(LETTERS.charAt(RANDOM.nextInt(LETTERS.length())));
        }
        return sb.toString();
    }
}
       
      
                
