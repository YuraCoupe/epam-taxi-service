package com.epam.rd.java.basic.taxiservice.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.lang.invoke.MethodHandles;

@WebFilter(filterName = "localeFilter")
public class LocaleFilter implements Filter {
    final static Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());


    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        if (req.getSession().getAttribute("language") == null) {
            req.getSession().setAttribute("language", req.getLocale().getLanguage());
            logger.info(req.getLocale().getLanguage() + " language set as session attribute from OS Locale");
        }
        chain.doFilter(request, response);
    }
}