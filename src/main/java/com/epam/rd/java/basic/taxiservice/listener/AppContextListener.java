package com.epam.rd.java.basic.taxiservice.listener;

import com.epam.rd.java.basic.taxiservice.config.DatabaseManager;
import com.epam.rd.java.basic.taxiservice.config.PostgresHikariProvider;
import com.epam.rd.java.basic.taxiservice.config.PropertiesUtil;
import com.epam.rd.java.basic.taxiservice.repository.*;
import com.epam.rd.java.basic.taxiservice.service.*;
import com.epam.rd.java.basic.taxiservice.validator.CarValidator;
import com.epam.rd.java.basic.taxiservice.validator.TripValidator;
import com.epam.rd.java.basic.taxiservice.validator.UserValidator;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class AppContextListener implements ServletContextListener {

    public void contextInitialized(ServletContextEvent servletContextEvent) {

        ServletContext ctx = servletContextEvent.getServletContext();

        PropertiesUtil util = new PropertiesUtil(ctx);
        DatabaseManager dbConnector = new PostgresHikariProvider(util.getHostname(), util.getPort(),
                util.getSchema(), util.getUser(), util.getPassword(), util.getJdbcDriver());
        ctx.setAttribute("dbConnector", dbConnector);
        System.out.println("Database connection initialized for Application.");

        CarCategoryRepository carCategoryRepository = new CarCategoryRepository(dbConnector);
        ctx.setAttribute("carCategoryRepository", carCategoryRepository);
        System.out.println("CarCategoryRepository initialized for Application.");

        CarModelRepository carModelRepository = new CarModelRepository(dbConnector);
        ctx.setAttribute("carModelRepository", carModelRepository);
        System.out.println("CarModelRepository initialized for Application.");

        CarRepository carRepository = new CarRepository(dbConnector);
        ctx.setAttribute("carRepository", carRepository);
        System.out.println("CarRepository initialized for Application.");

        CarStatusRepository carStatusRepository = new CarStatusRepository(dbConnector);
        ctx.setAttribute("carStatusRepository", carStatusRepository);
        System.out.println("CarStatusRepository initialized for Application.");

        DiscountRateRepository discountRateRepository = new DiscountRateRepository(dbConnector);
        ctx.setAttribute("discountRateRepository", discountRateRepository);
        System.out.println("DiscountRateRepository initialized for Application.");

        PriceRateRepository priceRateRepository = new PriceRateRepository(dbConnector);
        ctx.setAttribute("priceRateRepository", priceRateRepository);
        System.out.println("PriceRateRepository initialized for Application.");

        RoleRepository roleRepository = new RoleRepository(dbConnector);
        ctx.setAttribute("roleRepository", roleRepository);
        System.out.println("RoleRepository initialized for Application.");

        StreetRepository streetRepository = new StreetRepository(dbConnector);
        ctx.setAttribute("streetRepository", streetRepository);
        System.out.println("StreetRepository initialized for Application.");

        TripRepository tripRepository = new TripRepository(dbConnector);
        ctx.setAttribute("tripRepository", tripRepository);
        System.out.println("TripRepository initialized for Application.");

        TripStatusRepository tripStatusRepository = new TripStatusRepository(dbConnector);
        ctx.setAttribute("tripStatusRepository", tripStatusRepository);
        System.out.println("TripStatusRepository initialized for Application.");

        UserRepository userRepository = new UserRepository(dbConnector);
        ctx.setAttribute("userRepository", userRepository);
        System.out.println("UserRepository initialized for Application.");

        BingMapsService bingMapsService = new BingMapsService();
        ctx.setAttribute("bingMapsService", bingMapsService);
        System.out.println("BingMapsService initialized for Application.");

        CarCategoryService carCategoryService = new CarCategoryService(carCategoryRepository);
        ctx.setAttribute("carCategoryService", carCategoryService);
        System.out.println("CarCategoryService initialized for Application.");

        CarModelService carModelService = new CarModelService(carModelRepository);
        ctx.setAttribute("carModelService", carModelService);
        System.out.println("CarModelService initialized for Application.");

        CarService carService = new CarService(carRepository);
        ctx.setAttribute("carService", carService);
        System.out.println("CarService initialized for Application.");

        CarStatusService carStatusService = new CarStatusService(carStatusRepository);
        ctx.setAttribute("carStatusService", carStatusService);
        System.out.println("CarStatusService initialized for Application.");

        PriceService priceService = new PriceService(priceRateRepository, discountRateRepository);
        ctx.setAttribute("priceService", priceService);
        System.out.println("PriceService initialized for Application.");

        RoleService roleService = new RoleService(roleRepository);
        ctx.setAttribute("roleService", roleService);
        System.out.println("RoleService initialized for Application.");

        StreetService streetService = new StreetService(streetRepository);
        ctx.setAttribute("streetService", streetService);
        System.out.println("StreetService initialized for Application.");

        TripService tripService = new TripService(tripRepository, carRepository);
        ctx.setAttribute("tripService", tripService);
        System.out.println("TripService initialized for Application.");

        TripStatusService tripStatusService = new TripStatusService(tripStatusRepository);
        ctx.setAttribute("tripStatusService", tripStatusService);
        System.out.println("TripStatusService initialized for Application.");

        UserService userService = new UserService(userRepository);
        ctx.setAttribute("userService", userService);
        System.out.println("UserService initialized for Application.");

        CarValidator carValidator = new CarValidator(carRepository, carStatusRepository);
        ctx.setAttribute("carValidator", carValidator);
        System.out.println("CarValidator initialized for Application.");

        TripValidator tripValidator = new TripValidator(tripRepository, carCategoryRepository, bingMapsService);
        ctx.setAttribute("tripValidator", tripValidator);
        System.out.println("TripValidator initialized for Application.");

        UserValidator userValidator = new UserValidator(userRepository);
        ctx.setAttribute("userValidator", userValidator);
        System.out.println("UserValidator initialized for Application.");

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        ctx.setAttribute("passwordEncoder", passwordEncoder);
        System.out.println("PasswordEncoder initialized for Application.");
    }

    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        ServletContext ctx = servletContextEvent.getServletContext();
        DatabaseManager dbManager = (DatabaseManager) ctx.getAttribute("dbConnector");
        dbManager.closeConnection();
        System.out.println("Database connection closed for Application.");
    }

}
