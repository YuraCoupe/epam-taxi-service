package com.epam.rd.java.basic.taxiservice.listener;

import com.epam.rd.java.basic.taxiservice.config.DatabaseManager;
import com.epam.rd.java.basic.taxiservice.config.PostgresHikariProvider;
import com.epam.rd.java.basic.taxiservice.config.PropertiesUtil;
import com.epam.rd.java.basic.taxiservice.repository.*;
import com.epam.rd.java.basic.taxiservice.service.*;
import com.epam.rd.java.basic.taxiservice.validator.CarValidator;
import com.epam.rd.java.basic.taxiservice.validator.TripValidator;
import com.epam.rd.java.basic.taxiservice.validator.UserValidator;
import org.apache.commons.lang3.concurrent.TimedSemaphore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import java.lang.invoke.MethodHandles;
import java.sql.Time;
import java.sql.Timestamp;

@WebListener
public class SessionListener implements HttpSessionListener {
    final static Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Override
    public void sessionCreated(HttpSessionEvent se) {
        se.getSession().setAttribute("tripsFieldToSort", "open_time");
        logger.info("Trips sort field set to {}", se.getSession().getAttribute("tripsFieldToSort"));
        se.getSession().setAttribute("tripsSortOrder", "DESC");
        logger.info("Trips sort order set to {}", se.getSession().getAttribute("tripsSortOrder"));
        Timestamp timeFrom = Timestamp.valueOf("2023-01-01 00:00:00");
        se.getSession().setAttribute("tripsTimeFrom", timeFrom);
        logger.info("Trips filter time from set to {}", timeFrom);
        Timestamp timeTo = new Timestamp(System.currentTimeMillis() / 60000L * 60000L);
        se.getSession().setAttribute("tripsTimeTo", timeTo);
        logger.info("Trips filter time to set to {}", timeTo);
        Integer selectedUserId = 0;
        se.getSession().setAttribute("selectedUserId", selectedUserId);
        logger.info("Selected user ID to set to {}", selectedUserId);
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        se.getSession().removeAttribute("tripsFieldToSort");
        logger.info("Session attribute {} removed", se.getSession().getAttribute("tripsFieldToSort"));
        se.getSession().removeAttribute("tripsSortOrder");
        logger.info("Session attribute {} removed", se.getSession().getAttribute("tripsSortOrder"));
        se.getSession().removeAttribute("tripsTimeFrom");
        logger.info("Session attribute {} removed", se.getSession().getAttribute("tripsTimeFrom"));
        se.getSession().removeAttribute("tripsTimeTo");
        logger.info("Session attribute {} removed", se.getSession().getAttribute("tripsTimeTo"));
        se.getSession().removeAttribute("selectedUserId");
        logger.info("Session attribute {} removed", se.getSession().getAttribute("selectedUserId"));
    }
}
