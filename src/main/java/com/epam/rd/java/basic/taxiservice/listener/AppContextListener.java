package com.epam.rd.java.basic.taxiservice.listener;

import com.epam.rd.java.basic.taxiservice.config.DatabaseManager;
import com.epam.rd.java.basic.taxiservice.config.PostgresHikariProvider;
import com.epam.rd.java.basic.taxiservice.config.PropertiesUtil;
import com.epam.rd.java.basic.taxiservice.repository.*;
import com.epam.rd.java.basic.taxiservice.service.*;
import com.epam.rd.java.basic.taxiservice.validator.CarValidator;
import com.epam.rd.java.basic.taxiservice.validator.TripValidator;
import com.epam.rd.java.basic.taxiservice.validator.UserValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.lang.invoke.MethodHandles;

@WebListener
public class AppContextListener implements ServletContextListener {
    final static Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public void contextInitialized(ServletContextEvent servletContextEvent) {

        ServletContext ctx = servletContextEvent.getServletContext();

        PropertiesUtil util = new PropertiesUtil(ctx);
        DatabaseManager dbConnector = new PostgresHikariProvider(util.getHostname(), util.getPort(),
                util.getSchema(), util.getUser(), util.getPassword(), util.getJdbcDriver());
        ctx.setAttribute("dbConnector", dbConnector);
        logger.info("Database connection initialized for Application.");

        CarCategoryRepository carCategoryRepository = new CarCategoryRepository(dbConnector);
        ctx.setAttribute("carCategoryRepository", carCategoryRepository);
        logger.info("CarCategoryRepository initialized for Application.");

        CarModelRepository carModelRepository = new CarModelRepository(dbConnector);
        ctx.setAttribute("carModelRepository", carModelRepository);
        logger.info("CarModelRepository initialized for Application.");

        CarRepository carRepository = new CarRepository(dbConnector);
        ctx.setAttribute("carRepository", carRepository);
        logger.info("CarRepository initialized for Application.");

        CarStatusRepository carStatusRepository = new CarStatusRepository(dbConnector);
        ctx.setAttribute("carStatusRepository", carStatusRepository);
        logger.info("CarStatusRepository initialized for Application.");

        DiscountRateRepository discountRateRepository = new DiscountRateRepository(dbConnector);
        ctx.setAttribute("discountRateRepository", discountRateRepository);
        logger.info("DiscountRateRepository initialized for Application.");

        PriceRateRepository priceRateRepository = new PriceRateRepository(dbConnector);
        ctx.setAttribute("priceRateRepository", priceRateRepository);
        logger.info("PriceRateRepository initialized for Application.");

        RoleRepository roleRepository = new RoleRepository(dbConnector);
        ctx.setAttribute("roleRepository", roleRepository);
        logger.info("RoleRepository initialized for Application.");

        TripRepository tripRepository = new TripRepository(dbConnector);
        ctx.setAttribute("tripRepository", tripRepository);
        logger.info("TripRepository initialized for Application.");

        TripStatusRepository tripStatusRepository = new TripStatusRepository(dbConnector);
        ctx.setAttribute("tripStatusRepository", tripStatusRepository);
        logger.info("TripStatusRepository initialized for Application.");

        UserRepository userRepository = new UserRepository(dbConnector);
        ctx.setAttribute("userRepository", userRepository);
        logger.info("UserRepository initialized for Application.");

        BingMapsService bingMapsService = new BingMapsService();
        ctx.setAttribute("bingMapsService", bingMapsService);
        logger.info("BingMapsService initialized for Application.");

        CarCategoryService carCategoryService = new CarCategoryService(carCategoryRepository);
        ctx.setAttribute("carCategoryService", carCategoryService);
        logger.info("CarCategoryService initialized for Application.");

        CarModelService carModelService = new CarModelService(carModelRepository);
        ctx.setAttribute("carModelService", carModelService);
        logger.info("CarModelService initialized for Application.");

        CarService carService = new CarService(carRepository);
        ctx.setAttribute("carService", carService);
        logger.info("CarService initialized for Application.");

        CarStatusService carStatusService = new CarStatusService(carStatusRepository);
        ctx.setAttribute("carStatusService", carStatusService);
        logger.info("CarStatusService initialized for Application.");

        PriceService priceService = new PriceService(priceRateRepository, discountRateRepository);
        ctx.setAttribute("priceService", priceService);
        logger.info("PriceService initialized for Application.");

        RoleService roleService = new RoleService(roleRepository);
        ctx.setAttribute("roleService", roleService);
        logger.info("RoleService initialized for Application.");

        TripService tripService = new TripService(tripRepository, carRepository);
        ctx.setAttribute("tripService", tripService);
        logger.info("TripService initialized for Application.");

        TripStatusService tripStatusService = new TripStatusService(tripStatusRepository);
        ctx.setAttribute("tripStatusService", tripStatusService);
        logger.info("TripStatusService initialized for Application.");

        UserService userService = new UserService(userRepository);
        ctx.setAttribute("userService", userService);
        logger.info("UserService initialized for Application.");

        CarValidator carValidator = new CarValidator(carRepository, carStatusRepository);
        ctx.setAttribute("carValidator", carValidator);
        logger.info("CarValidator initialized for Application.");

        TripValidator tripValidator = new TripValidator(tripRepository, carCategoryRepository, bingMapsService);
        ctx.setAttribute("tripValidator", tripValidator);
        logger.info("TripValidator initialized for Application.");

        UserValidator userValidator = new UserValidator(userRepository);
        ctx.setAttribute("userValidator", userValidator);
        logger.info("UserValidator initialized for Application.");

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        ctx.setAttribute("passwordEncoder", passwordEncoder);
        logger.info("PasswordEncoder initialized for Application.");
    }

    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        ServletContext ctx = servletContextEvent.getServletContext();
        DatabaseManager dbManager = (DatabaseManager) ctx.getAttribute("dbConnector");
        dbManager.closeConnection();
        logger.info("Database connection closed for Application.");
    }

}
