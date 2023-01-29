package com.epam.rd.java.basic.taxiservice.filter;

import com.epam.rd.java.basic.taxiservice.config.ConfigurationManager;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(filterName = "authenticationFilter")
public class AuthenticationFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse resp = (HttpServletResponse) servletResponse;

        final String HOME_URI = ConfigurationManager.getProperty("path.uri.home");
        final String INDEX_URI = ConfigurationManager.getProperty("path.uri.indexPage");
        final String LOGIN_PAGE_URI = ConfigurationManager.getProperty("path.uri.loginPage");
        final String LOGIN_URI = ConfigurationManager.getProperty("path.uri.login");
        final String NEW_USER_URI = ConfigurationManager.getProperty("path.uri.clients.new");
        final String SAVE_USER_URI = ConfigurationManager.getProperty("path.uri.clients.save");


        String action = req.getRequestURI();
        if (HOME_URI.equals(action)
                || INDEX_URI.equals(action)
                || LOGIN_PAGE_URI.equals(action)
                || LOGIN_URI.equals(action)
                || NEW_USER_URI.equals(action)
                || SAVE_USER_URI.equals(action)) {
            filterChain.doFilter(servletRequest, servletResponse);
        } else {
            Object isLoggedObj = req.getSession().getAttribute("isLoggedIn");
            if (isLoggedObj != null) {
                boolean isLoggedIn = (Boolean) isLoggedObj;
                if (isLoggedIn) {
                    filterChain.doFilter(servletRequest, servletResponse);
                    return;
                }
            }
            resp.sendRedirect(LOGIN_PAGE_URI);
        }
    }
}