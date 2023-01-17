package com.epam.rd.java.basic.taxiservice.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(filterName = "authenticationFilter", urlPatterns = "/*")
public class AuthenticationFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse resp = (HttpServletResponse) servletResponse;

        String action = req.getRequestURI();
        if("/login".equals(action) || "/login.jsp".equals(action) || "/users/new".equals(action)){
            filterChain.doFilter(servletRequest, servletResponse);
        } else{
            Object isLoggedObj = req.getSession().getAttribute("isLoggedIn");
            if(isLoggedObj != null){
                boolean isLoggedIn = (Boolean) isLoggedObj;
                if(isLoggedIn){
                    filterChain.doFilter(servletRequest, servletResponse);
                    return;
                }
            }
            String path = "/login";
            resp.sendRedirect(path);
        }
    }
}