package com.epam.rd.java.basic.taxiservice.filter;

import com.epam.rd.java.basic.taxiservice.model.Role;
import com.epam.rd.java.basic.taxiservice.model.User;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@WebFilter(filterName = "authorizationFilter", urlPatterns = "/*")
public class AuthorizationFilter implements Filter {

    private static final Map<String, List<String>> ROLE_PATH_MAP;
    private static final String ANY_PATH = "/*";
    private static final String TRIP_PATH = "/trips";
    private static final String CAR_PATH = "/cars";
    private static final String INDEX_PATH = "/";
    private static final String LOGOUT_PATH = "/logout";
    private static final String CLIENT_ROLE = "ROLE_CLIENT";
    private static final String DRIVER_ROLE = "ROLE_DRIVER";
    private static final String ADMIN_ROLE = "ROLE_ADMINISTRATOR";

    static {

        List<String> CLIENT_ROLE_LIST = List.of(TRIP_PATH, LOGOUT_PATH, INDEX_PATH);
        List<String> DRIVER_ROLE_LIST = List.of(TRIP_PATH, CAR_PATH, LOGOUT_PATH, INDEX_PATH);
        List<String> ADMIN_ROLE_LIST = List.of(ANY_PATH);

        ROLE_PATH_MAP = Map.of(
                CLIENT_ROLE, CLIENT_ROLE_LIST,
                DRIVER_ROLE, DRIVER_ROLE_LIST,
                ADMIN_ROLE, ADMIN_ROLE_LIST
        );
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse resp = (HttpServletResponse) servletResponse;

        String action = req.getRequestURI();
        if ("/login".equals(action) || "/login.jsp".equals(action) || "/users/new".equals(action)) {
            filterChain.doFilter(servletRequest, servletResponse);
        } else {
            Object isLoggedObj = req.getSession().getAttribute("isLoggedIn");
            if (isLoggedObj != null) {
                boolean isLoggedIn = (Boolean) isLoggedObj;
                if (isLoggedIn) {
                    User loggedUser = (User) req.getSession().getAttribute("user");
                    Role role = loggedUser.getRole();
                    String roleTitle = role.getTitle();
                    String contextPath = req.getServletPath();
                    List<String> authorizedPaths = ROLE_PATH_MAP.get(roleTitle);
                    if (authorizedPaths.contains(contextPath) || authorizedPaths.contains(ANY_PATH)) {
                        filterChain.doFilter(servletRequest, servletResponse);
                        return;
                    }
                }
                String path = "/login";
                resp.sendRedirect(path);
            }
        }
    }
}