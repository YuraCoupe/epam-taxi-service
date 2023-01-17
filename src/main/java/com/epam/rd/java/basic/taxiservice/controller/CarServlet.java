package com.epam.rd.java.basic.taxiservice.controller;

import com.epam.rd.java.basic.taxiservice.config.DatabaseManager;
import com.epam.rd.java.basic.taxiservice.config.PostgresHikariProvider;
import com.epam.rd.java.basic.taxiservice.config.PropertiesUtil;
import com.epam.rd.java.basic.taxiservice.model.Car.Car;
import com.epam.rd.java.basic.taxiservice.model.Car.CarCategory;
import com.epam.rd.java.basic.taxiservice.model.Car.CarModel;
import com.epam.rd.java.basic.taxiservice.model.Car.CarStatus;
import com.epam.rd.java.basic.taxiservice.model.ErrorMessage;
import com.epam.rd.java.basic.taxiservice.model.Trip;
import com.epam.rd.java.basic.taxiservice.model.User;
import com.epam.rd.java.basic.taxiservice.repository.*;
import com.epam.rd.java.basic.taxiservice.service.*;
import com.epam.rd.java.basic.taxiservice.validator.CarValidator;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

@WebServlet(urlPatterns = "/cars/*")
public class CarServlet extends HttpServlet {
    private CarService carService;
    private CarModelService carModelService;
    private CarCategoryService carCategoryService;
    private CarStatusService carStatusService;
    private UserService userService;
    private CarValidator validator;


    @Override
    public void init() {
        PropertiesUtil util = new PropertiesUtil(getServletContext());

        DatabaseManager dbConnector = new PostgresHikariProvider(util.getHostname(), util.getPort(),
                util.getSchema(), util.getUser(), util.getPassword(), util.getJdbcDriver());
        CarRepository carRepository = new CarRepository(dbConnector);
        CarModelRepository carModelRepository = new CarModelRepository(dbConnector);
        CarCategoryRepository carCategoryRepository = new CarCategoryRepository(dbConnector);
        CarStatusRepository carStatusRepository = new CarStatusRepository(dbConnector);
        UserRepository userRepository = new UserRepository(dbConnector);
        this.carService = new CarService(carRepository);
        this.carModelService = new CarModelService(carModelRepository);
        this.carCategoryService = new CarCategoryService(carCategoryRepository);
        this.carStatusService = new CarStatusService(carStatusRepository);
        this.userService = new UserService(userRepository);
        validator = new CarValidator(carRepository, carStatusRepository);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        Integer carId = null;
        Car car = new Car();

        if (!req.getParameter("carId").isBlank()) {
            carId = Integer.parseInt(req.getParameter("carId"));
            car.setId(carId);
        }

        ErrorMessage errorMessage = validator.validateCar(req);
        if (!errorMessage.getErrors().isEmpty()) {
            req.setAttribute("errorMessage", errorMessage);
            if (Objects.nonNull(carId)) {
                handleId(carId, req, resp);
            } else {
                handleNew(req, resp);
            }
            return;
        }

        Integer modelId = Integer.parseInt(req.getParameter("id"));
        CarModel model = carModelService.findById(modelId);
        Integer capacity = Integer.parseInt(req.getParameter("capacity"));
        Integer categoryId = Integer.parseInt(req.getParameter("categoryId"));
        Integer statusId = Integer.parseInt(req.getParameter("statusId"));
        String licensePlate = req.getParameter("licensePlate");

        car.setModel(model);
        car.setCapacity(capacity);
        CarCategory category = carCategoryService.findById(categoryId);
        car.setCategory(category);
        CarStatus status = carStatusService.findById(statusId);
        car.setStatus(status);
        car.setLicensePlate(licensePlate);

        String driverIdString = req.getParameter("driverId");
        Integer driverId = null;
        User driver = new User();
        if (driverIdString != null && !driverIdString.isBlank()) {
            driverId = Integer.parseInt(driverIdString);
            driver = userService.findById(driverId);
        }
        car.setDriver(driver);

        if (car.getCurrentTrip() == null) {
            Trip currentTrip = new Trip();
            currentTrip.setId(null);
            car.setCurrentTrip(currentTrip);
        }

        if (Objects.isNull(carId)) {
            carId = carService.save(car);
        } else {
            carService.update(car);
        }

        resp.sendRedirect("/cars");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String requestURI = req.getRequestURI();
        String idStr = requestURI.replaceAll("/cars/?", "");
        String deleteId = req.getParameter("deleteId");
        String carId = req.getParameter("id");
        if (deleteId != null) {
            Car car = carService.findById(Integer.parseInt(deleteId));
            carService.delete(car);
            resp.sendRedirect("/cars");
        } else if ("new".equalsIgnoreCase(idStr)) {
            handleNew(req, resp);
        } else if (carId != null) {
            handleId(Integer.parseInt(carId), req, resp);
        } else if (!idStr.equals("")) {
            try {
                Integer id = Integer.parseInt(idStr);

                handleId(id, req, resp);
            } catch (RuntimeException e) {
                resp.sendRedirect("/cars");
            }
        } else {
            int totalNumber = carService.getTotalNumber();
            int page = req.getParameter("page") != null ? Integer.parseInt(req.getParameter("page")) : 1;
            int limit = 5;
            int offset = (page - 1) * limit;
            int pageCount = totalNumber / limit + 1;

            List<Car> cars = carService.findAllWithOffsetAndLimit(offset, limit);
            req.setAttribute("cars", cars);
            req.setAttribute("page", page);
            req.setAttribute("pageCount", pageCount);
            req.getRequestDispatcher("/jsp/cars.jsp").forward(req, resp);
        }
    }

    private void handleNew(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("car", new Car());
        List<CarModel> carModels = carModelService.findAll();
        req.setAttribute("carModels", carModels);
        List<CarCategory> carCategories = carCategoryService.findAll();
        req.setAttribute("carCategories", carCategories);
        List<CarStatus> carStatuses = carStatusService.findAll();
        req.setAttribute("carStatuses", carStatuses);
        List<User> freeDrivers = userService.findFreeDrivers();
        req.setAttribute("freeDrivers", freeDrivers);
        req.getRequestDispatcher("/jsp/car.jsp").forward(req, resp);
    }

    private void handleId(Integer id, HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Car car = carService.findById(id);
        req.setAttribute("car", car);
        List<CarModel> carModels = carModelService.findAll();
        req.setAttribute("carModels", carModels);
        List<CarCategory> carCategories = carCategoryService.findAll();
        req.setAttribute("carCategories", carCategories);
        List<CarStatus> carStatuses = carStatusService.findAll();
        req.setAttribute("carStatuses", carStatuses);
        req.setCharacterEncoding("UTF-8");
        List<User> freeDrivers = userService.findFreeDrivers();
        req.setAttribute("freeDrivers", freeDrivers);
        req.getRequestDispatcher("/jsp/car.jsp").forward(req, resp);

    }
}
