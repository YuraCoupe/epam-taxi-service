package com.epam.rd.java.basic.taxiservice.controller;

import com.epam.rd.java.basic.taxiservice.config.DatabaseManager;
import com.epam.rd.java.basic.taxiservice.config.PostgresHikariProvider;
import com.epam.rd.java.basic.taxiservice.config.PropertiesUtil;
import com.epam.rd.java.basic.taxiservice.exception.CarNotFoundException;
import com.epam.rd.java.basic.taxiservice.model.*;
import com.epam.rd.java.basic.taxiservice.model.Car.Car;
import com.epam.rd.java.basic.taxiservice.model.Car.CarCategory;
import com.epam.rd.java.basic.taxiservice.model.Car.CarStatus;
import com.epam.rd.java.basic.taxiservice.repository.*;
import com.epam.rd.java.basic.taxiservice.service.*;
import com.epam.rd.java.basic.taxiservice.validator.TripValidator;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.*;

@WebServlet(urlPatterns = "/trips/*")
public class TripServlet extends HttpServlet {
    private TripService tripService;
    private UserService userService;
    private StreetService streetService;
    private CarService carService;
    private TripStatusService tripStatusService;
    private CarStatusService carStatusService;
    private CarCategoryService categoryService;
    private BingMapsService bingMapsService;
    private PriceService priceService;
    private TripValidator validator;


    @Override
    public void init() {
        PropertiesUtil util = new PropertiesUtil(getServletContext());

        DatabaseManager dbConnector = new PostgresHikariProvider(util.getHostname(), util.getPort(),
                util.getSchema(), util.getUser(), util.getPassword(), util.getJdbcDriver());
        TripRepository tripRepository = new TripRepository(dbConnector);
        UserRepository userRepository = new UserRepository(dbConnector);
        StreetRepository streetRepository = new StreetRepository(dbConnector);
        TripStatusRepository tripStatusRepository = new TripStatusRepository(dbConnector);
        CarCategoryRepository categoryRepository = new CarCategoryRepository(dbConnector);
        CarStatusRepository carStatusRepository = new CarStatusRepository(dbConnector);
        CarRepository carRepository = new CarRepository(dbConnector);
        PriceRateRepository priceRateRepository = new PriceRateRepository(dbConnector);
        DiscountRateRepository discountRateRepository = new DiscountRateRepository(dbConnector);
        this.userService = new UserService(userRepository);
        this.tripService = new TripService(tripRepository, carRepository);
        this.streetService = new StreetService(streetRepository);
        this.tripStatusService = new TripStatusService(tripStatusRepository);
        this.categoryService = new CarCategoryService(categoryRepository);
        this.carStatusService = new CarStatusService(carStatusRepository);
        this.carService = new CarService(carRepository);
        this.bingMapsService = new BingMapsService();
        this.priceService = new PriceService(priceRateRepository, discountRateRepository);
        this.validator = new TripValidator(tripRepository, categoryRepository, bingMapsService);

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User loggedInUser = (User) req.getSession().getAttribute("user");
        Integer userId = loggedInUser.getId();

        if (req.getRequestURI().contains("processing")) {
            Integer tripId = Integer.parseInt(req.getParameter("tripId"));
            Trip trip = tripService.findById(tripId);
            TripStatus status = tripStatusService.findByTitle("Processing");
            trip.setStatus(status);
            tripService.update(trip);
            resp.sendRedirect("/trips/view/" + trip.getId());
            return;
        }

        if (req.getRequestURI().contains("finish")) {
            Integer tripId = Integer.parseInt(req.getParameter("tripId"));
            Trip trip = tripService.findById(tripId);

            CarStatus carStatus = carStatusService.findByTitle("available for order");
            for (Car car : trip.getCars()) {
                if (car.getDriver().getId().equals(userId)) {
                    car.setStatus(carStatus);
                    car.getCurrentTrip().setId(null);
                    carService.update(car);
                }
            }

            req.getSession().removeAttribute("activeTripId");

            boolean tripIsCompleted = true;
            for (Car car : trip.getCars()) {
                if (car.getStatus().getTitle().equals("on route")) {
                    tripIsCompleted = false;
                }
            }
            if (tripIsCompleted) {
                TripStatus status = tripStatusService.findByTitle("Completed");
                trip.setStatus(status);
                trip.setCloseTime(new Timestamp(System.currentTimeMillis()));
                tripService.update(trip);
            }

            resp.sendRedirect("/trips/view/" + trip.getId());
            return;
        }

        Integer tripId = null;
        Trip trip = new Trip();

        if (!req.getParameter("tripId").isBlank()) {
            tripId = Integer.parseInt(req.getParameter("tripId"));
            trip.setId(tripId);
        }

        ErrorMessage errorMessage = validator.validate(req);
        if (!errorMessage.getErrors().isEmpty()) {
            req.setAttribute("errorMessage", errorMessage);
            if (Objects.nonNull(tripId)) {
                handleId(tripId, req, resp);
            } else {
                handleNew(req, resp);
            }
            return;
        }

        String allowSeveralCars = req.getParameter("allowSeveralCars");
        List<Car> cars = new ArrayList<>();
        if (allowSeveralCars == null) {
            try {
                Integer categoryId = Integer.parseInt(req.getParameter("categoryId"));
                CarCategory category = categoryService.findById(categoryId);
                Integer capacity = Integer.parseInt(req.getParameter("passengersNumber"));
                cars.add(carService.findOneByCategoryAndCapacity(category.getTitle(), capacity));
                ;
            } catch (CarNotFoundException e) {
                req.setAttribute("noCarAvailable", "true");
                handleNew(req, resp);
                return;
            }
        } else {
            try {
                Integer categoryId = Integer.parseInt(req.getParameter("categoryId"));
                CarCategory category = categoryService.findById(categoryId);
                Integer capacity = Integer.parseInt(req.getParameter("passengersNumber"));
                cars.addAll(carService.findSeveralByCategoryAndCapacity(category.getTitle(), capacity));
            } catch (CarNotFoundException e) {
                req.setAttribute("noCarAvailable", "true");
                handleNew(req, resp);
                return;
            }
        }

        User user = userService.findById(userId);
        String departureAddress = req.getParameter("departureAddress");
        String destinationAddress = req.getParameter("destinationAddress");
        Integer numberOfPassengers = Integer.parseInt(req.getParameter("passengersNumber"));
        Integer categoryId = Integer.parseInt(req.getParameter("categoryId"));
        CarCategory category = categoryService.findById(categoryId);
        TripStatus status = tripStatusService.findByTitle("Open");
        trip.setUser(user);

        trip.setNumberOfPassengers(numberOfPassengers);
        trip.setCategory(category);
        trip.setStatus(status);
        BingRoute bingRoute = bingMapsService.getRoute(departureAddress, destinationAddress);
        trip.setDepartureAddress(bingRoute.getStartLocation());
        trip.setDestinationAddress(bingRoute.getEndLocation());
        trip.setDistance(bingRoute.getTravelDistance());
        BigDecimal price = priceService.calculateTripPrice(trip.getDistance(), user.getSumSpent(), category, trip.getCars().size());
        trip.setPrice(price);
        trip.setOpenTime(new Timestamp(System.currentTimeMillis()));
        trip.setCars(new HashSet<>(cars));

        req.setAttribute("trip", trip);

        if (Objects.isNull(tripId)) {
            tripId = tripService.save(trip);
        } else {
            tripService.update(trip);
        }
        CarStatus carStatus = carStatusService.findByTitle("on route");
        cars.forEach(car -> car.setStatus(carStatus));
        cars.forEach(car -> car.setCurrentTrip(trip));
        cars.forEach(car -> carService.update(car));

        req.getSession().setAttribute("activeTripId", tripId);

        resp.sendRedirect("/trips/view/" + tripId);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User loggedInUser = (User) req.getSession().getAttribute("user");
        String loggedUserRoleTitle = loggedInUser.getRole().getTitle();
        if (req.getSession().getAttribute("activeTripId") == null) {
            if (loggedUserRoleTitle.equals("ROLE_CLIENT")) {
                Optional<Trip> activeTripOptional = tripService.findActiveByClientId(loggedInUser.getId());
                activeTripOptional.ifPresent(trip -> req.getSession().setAttribute("activeTripId", trip.getId()));
            } else if (loggedUserRoleTitle.equals("ROLE_DRIVER")) {
                Optional<Trip> activeTripOptional = tripService.findActiveByDriverId(loggedInUser.getId());
                activeTripOptional.ifPresent(trip -> req.getSession().setAttribute("activeTripId", trip.getId()));
            }
        } else {
            if (loggedUserRoleTitle.equals("ROLE_CLIENT")) {
                Optional<Trip> activeTripOptional = tripService.findActiveByClientId(loggedInUser.getId());
                if (activeTripOptional.isEmpty()) {
                    req.getSession().removeAttribute("activeTripId");
                }
            } else if (loggedUserRoleTitle.equals("ROLE_DRIVER")) {
                Optional<Trip> activeTripOptional = tripService.findActiveByDriverId(loggedInUser.getId());
                if (activeTripOptional.isEmpty()) {
                    req.getSession().removeAttribute("activeTripId");
                }
            }
        }
        String requestURI = req.getRequestURI();
        String deleteId = req.getParameter("deleteId");
        String tripId = req.getParameter("id");
        if (requestURI.contains("view")) {
            Integer tripNumber = Integer.parseInt(requestURI.replaceAll("/trips/view/", ""));
            handleView(tripNumber, req, resp);
        }
        if (deleteId != null) {
            Trip trip = tripService.findById(Integer.parseInt(deleteId));
            tripService.delete(trip);
            resp.sendRedirect("/trips");
            return;
        }
        if (requestURI.equalsIgnoreCase("/trips/new")) {
            handleNew(req, resp);
        }
        if (tripId != null) {
            handleId(Integer.parseInt(tripId), req, resp);
        }
        if (requestURI.contains("trips/edit")) {
            try {
                tripId = requestURI.replaceAll("trips/edit/", "");
                Integer id = Integer.parseInt(tripId);
                handleId(id, req, resp);
            } catch (RuntimeException e) {
                resp.sendRedirect("/trips");
            }
        }
        if (requestURI.equals("/trips")) {

            if (req.getSession().getAttribute("activeTripId") != null) {
                Integer activeTripId = (Integer) req.getSession().getAttribute("activeTripId");
                handleView(activeTripId, req, resp);
                return;
            }

        }
        int totalNumber;
        int page = req.getParameter("page") != null ? Integer.parseInt(req.getParameter("page")) : 1;
        int limit = 5;
        int offset = (page - 1) * limit;
        List<Trip> trips = null;

        switch (loggedUserRoleTitle) {
            case "ROLE_ADMINISTRATOR": {
                totalNumber = tripService.getTotalNumber();
                trips = tripService.findAllWithOffsetAndLimit(offset, limit);
                break;
            }
            case "ROLE_DRIVER": {
                totalNumber = tripService.getTotalNumberByDriverId(loggedInUser.getId());
                trips = tripService.findByDriverIdWithOffsetAndLimit(loggedInUser.getId(), offset, limit);
                break;
            }
            default: {
                totalNumber = tripService.getTotalNumberByUserId(loggedInUser.getId());
                trips = tripService.findByUserIdWithOffsetAndLimit(loggedInUser.getId(), offset, limit);
            }
        }

        int pageCount = totalNumber % limit == 0 ? totalNumber / limit : totalNumber / limit + 1;
        req.setAttribute("trips", trips);
        req.setAttribute("page", page);
        req.setAttribute("pageCount", pageCount);
        req.getRequestDispatcher("/jsp/trips.jsp").forward(req, resp);
    }


    private void handleNew(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Trip trip = new Trip();

        req.setAttribute("trip", trip);
        List<CarCategory> categories = categoryService.findAll();
        req.setAttribute("categories", categories);
        req.setCharacterEncoding("UTF-8");
        req.setAttribute("msKey", System.getenv("MS_KEY"));
        req.getRequestDispatcher("/jsp/trip2.jsp").forward(req, resp);
    }

    private void handleId(Integer id, HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Trip trip = tripService.findById(id);
        req.setAttribute("trip", trip);
        req.setCharacterEncoding("UTF-8");
        req.getRequestDispatcher("/jsp/trip.jsp").forward(req, resp);

    }

    private void handleView(Integer id, HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Trip trip = tripService.findById(id);
        req.setAttribute("trip", trip);
        req.setCharacterEncoding("UTF-8");
        req.getRequestDispatcher("/jsp/tripview.jsp").forward(req, resp);

    }
}
