package com.epam.rd.java.basic.taxiservice.filter;

import com.epam.rd.java.basic.taxiservice.config.ConfigurationManager;
import com.epam.rd.java.basic.taxiservice.model.Role;
import com.epam.rd.java.basic.taxiservice.model.User;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@WebFilter(filterName = "authorizationFilter")
public class AuthorizationFilter implements Filter {

    private static final Map<String, List<String>> ROLE_PATH_MAP;
    private static final String ANY_PATH = ConfigurationManager.getProperty("path.uri.any");
    private static final String HOME_PATH = ConfigurationManager.getProperty("path.uri.home");
    private static final String LOGIN_PATH = ConfigurationManager.getProperty("path.uri.login");
    private static final String LOGOUT_PATH = ConfigurationManager.getProperty("path.uri.logout");
    private static final String LOGIN_PAGE_PATH = ConfigurationManager.getProperty("path.uri.loginPage");
    private static final String INDEX_PATH = ConfigurationManager.getProperty("path.uri.indexPage");

    private static final String TRIP_NEW_PATH = ConfigurationManager.getProperty("path.uri.trips.new");
    private static final String TRIP_SAVE_PATH = ConfigurationManager.getProperty("path.uri.trips.save");
    private static final String TRIP_LIST_PATH = ConfigurationManager.getProperty("path.uri.trips.list");
    private static final String TRIP_VIEW_PATH = ConfigurationManager.getProperty("path.uri.trips.view");
    private static final String TRIP_UPDATE_PATH = ConfigurationManager.getProperty("path.uri.trips.update");
    private static final String TRIP_EDIT_PATH = ConfigurationManager.getProperty("path.uri.trips.edit");
    private static final String TRIP_START_PATH = ConfigurationManager.getProperty("path.uri.trips.start");
    private static final String TRIP_FINISH_PATH = ConfigurationManager.getProperty("path.uri.trips.finish");


    private static final String CAR_NEW_PATH = ConfigurationManager.getProperty("path.uri.cars.new");
    private static final String CAR_SAVE_PATH = ConfigurationManager.getProperty("path.uri.cars.save");
    private static final String CAR_LIST_PATH = ConfigurationManager.getProperty("path.uri.cars.list");
    private static final String CAR_VIEW_PATH = ConfigurationManager.getProperty("path.uri.cars.view");
    private static final String CAR_UPDATE_PATH = ConfigurationManager.getProperty("path.uri.cars.update");
    private static final String CAR_EDIT_PATH = ConfigurationManager.getProperty("path.uri.cars.edit");

    private static final String CLIENT_NEW_PATH = ConfigurationManager.getProperty("path.uri.clients.new");
    private static final String CLIENT_SAVE_PATH = ConfigurationManager.getProperty("path.uri.clients.save");
    private static final String CLIENT_LIST_PATH = ConfigurationManager.getProperty("path.uri.clients.list");
    private static final String CLIENT_VIEW_PATH = ConfigurationManager.getProperty("path.uri.clients.view");
    private static final String CLIENT_UPDATE_PATH = ConfigurationManager.getProperty("path.uri.clients.update");
    private static final String CLIENT_EDIT_PATH = ConfigurationManager.getProperty("path.uri.clients.edit");

    private static final String CLIENT_ROLE = "ROLE_CLIENT";
    private static final String DRIVER_ROLE = "ROLE_DRIVER";
    private static final String ADMIN_ROLE = "ROLE_ADMINISTRATOR";

    static {

        List<String> CLIENT_ROLE_LIST = List.of(
                INDEX_PATH,
                HOME_PATH,
                LOGIN_PATH,
                LOGIN_PAGE_PATH,
                LOGOUT_PATH,
                TRIP_LIST_PATH,
                TRIP_VIEW_PATH,
                TRIP_NEW_PATH,
                TRIP_SAVE_PATH
                );
        List<String> DRIVER_ROLE_LIST = List.of(
                INDEX_PATH,
                HOME_PATH,
                LOGIN_PATH,
                LOGIN_PAGE_PATH,
                LOGOUT_PATH,
                TRIP_VIEW_PATH,
                TRIP_LIST_PATH,
                TRIP_START_PATH,
                TRIP_FINISH_PATH
        );
        List<String> ADMIN_ROLE_LIST = List.of(
                INDEX_PATH,
                HOME_PATH,
                LOGIN_PATH,
                LOGIN_PAGE_PATH,
                LOGOUT_PATH,
                TRIP_VIEW_PATH,
                TRIP_LIST_PATH,
                TRIP_EDIT_PATH,
                TRIP_UPDATE_PATH,
                CAR_EDIT_PATH,
                CAR_LIST_PATH,
                CAR_NEW_PATH,
                CAR_SAVE_PATH,
                CAR_UPDATE_PATH,
                CAR_VIEW_PATH,
                CLIENT_EDIT_PATH,
                CLIENT_LIST_PATH,
                CLIENT_NEW_PATH,
                CLIENT_SAVE_PATH,
                CLIENT_UPDATE_PATH,
                CLIENT_VIEW_PATH
        );

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
        if(HOME_PATH.equals(action)
                ||INDEX_PATH.equals(action)
                ||LOGIN_PATH.equals(action)
                || LOGIN_PAGE_PATH.equals(action)
                || CLIENT_NEW_PATH.equals(action)){
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
                String path = INDEX_PATH;
                resp.sendRedirect(path);
            }
        }
    }
}