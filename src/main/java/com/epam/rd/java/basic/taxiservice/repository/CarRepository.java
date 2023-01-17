package com.epam.rd.java.basic.taxiservice.repository;

import com.epam.rd.java.basic.taxiservice.config.DatabaseManager;
import com.epam.rd.java.basic.taxiservice.model.Car.Car;
import com.epam.rd.java.basic.taxiservice.model.Car.CarCategory;
import com.epam.rd.java.basic.taxiservice.model.Car.CarModel;
import com.epam.rd.java.basic.taxiservice.model.Car.CarStatus;
import com.epam.rd.java.basic.taxiservice.model.Trip;
import com.epam.rd.java.basic.taxiservice.model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CarRepository {
    private static final String INSERT =
            "INSERT INTO car (model_id, capacity, category_id, status_id, license_plate)\n" +
                    "VALUES\n" +
                    "(?, ?, ?, ?, ?, ?);";
    private static final String UPDATE =
            "UPDATE car\n" +
                    "SET\n" +
                    "model_id = ?,\n" +
                    "capacity = ?,\n" +
                    "category_id = ?,\n" +
                    "status_id = ?,\n" +
                    "license_plate = ?,\n" +
                    "driver_id = ?,\n" +
                    "current_trip_id = ?\n" +
                    "WHERE car.id = ?;";
    private static final String FIND_ALL =
            "SELECT car.id AS id, b.id AS brand_id, b.title AS brand_title, m.id AS model_id, m.title AS model_title,\n" +
                    "car.capacity, c.id AS category_id, c.title AS category_title, s.id AS status_id, \n" +
                    "s.title AS status_title, car.license_plate, car.driver_id as driver_id,\n" +
                    "p.id AS driver_id, p.first_name as driver_first_name, p.last_name AS driver_last_name,\n" +
                    "car.current_trip_id\n" +
                    "FROM car\n" +
                    "JOIN car_model m ON m.id = car.model_id\n" +
                    "JOIN car_brand b ON b.id = m.brand_id\n" +
                    "JOIN car_category c ON c.id = car.category_id\n" +
                    "JOIN car_status s ON s.id = car.status_id\n" +
                    "LEFT JOIN person p ON p.id = car.driver_id\n" +
                    "ORDER BY s.title;";
    private static final String FIND_ALL_WITH_OFFSET_AND_LIMIT =
            "SELECT car.id AS id, b.id AS brand_id, b.title AS brand_title, m.id AS model_id, m.title AS model_title,\n" +
                    "car.capacity, c.id AS category_id, c.title AS category_title, s.id AS status_id, \n" +
                    "s.title AS status_title, car.license_plate, car.driver_id as driver_id,\n" +
                    "p.id AS driver_id, p.first_name as driver_first_name, p.last_name AS driver_last_name,\n" +
                    "car.current_trip_id\n" +
                    "FROM car\n" +
                    "JOIN car_model m ON m.id = car.model_id\n" +
                    "JOIN car_brand b ON b.id = m.brand_id\n" +
                    "JOIN car_category c ON c.id = car.category_id\n" +
                    "JOIN car_status s ON s.id = car.status_id\n" +
                    "LEFT JOIN person p ON p.id = car.driver_id\n" +
                    "ORDER BY s.title\n" +
                    "OFFSET ? LIMIT ?;";
    private static final String GET_TOTAL_NUMBER =
            "SELECT COUNT (*) as number\n" +
                    "FROM car\n" +
                    "JOIN car_model m ON m.id = car.model_id\n" +
                    "JOIN car_brand b ON b.id = m.brand_id\n" +
                    "JOIN car_category c ON c.id = car.category_id\n" +
                    "JOIN car_status s ON s.id = car.status_id;";
    private static final String DELETE = "DELETE FROM car WHERE car.id = ?;";
    private static final String FIND_BY_ID =
            "SELECT car.id AS id, b.id AS brand_id, b.title AS brand_title, m.id AS model_id, m.title AS model_title,\n" +
                    "car.capacity, c.id AS category_id, c.title AS category_title, s.id AS status_id, \n" +
                    "s.title AS status_title, car.license_plate,\n" +
                    "p.id AS driver_id, p.first_name as driver_first_name, p.last_name AS driver_last_name,\n" +
                    "car.current_trip_id\n" +
                    "FROM car\n" +
                    "JOIN car_model m ON m.id = car.model_id\n" +
                    "JOIN car_brand b ON b.id = m.brand_id\n" +
                    "JOIN car_category c ON c.id = car.category_id\n" +
                    "JOIN car_status s ON s.id = car.status_id\n" +
                    "LEFT JOIN person p ON p.id = car.driver_id\n" +
                    "WHERE car.id = ?";
    private static final String FIND_BY_LICENSE_PLATE =
            "SELECT car.id AS id, b.id AS brand_id, b.title AS brand_title, m.id AS model_id, m.title AS model_title," +
                    "car.capacity, c.id AS category_id, c.title AS category_title, s.id AS status_id," +
                    "s.title AS status_title, car.license_plate, " +
                    "p.id AS driver_id, p.first_name as driver_first_name, p.last_name AS driver_last_name,\n" +
                    "car.current_trip_id\n" +
                    "FROM car" +
                    "JOIN car_model m ON m.id = car.model_id " +
                    "JOIN car_brand b ON b.id = m.brand_id " +
                    "JOIN car_category c ON c.id = car.category_id " +
                    "JOIN car_status s ON s.id = car.status_id " +
                    "LEFT JOIN person p ON p.id = car.driver_id " +
                    "WHERE car.license_plate = ?;";
    private static final String FIND_SEVERAL_BY_CATEGORY =
            "SELECT car.id AS id, b.id AS brand_id, b.title AS brand_title, m.id AS model_id, m.title AS model_title,\n" +
                    "car.capacity, c.id AS category_id, c.title AS category_title, s.id AS status_id, \n" +
                    "s.title AS status_title, car.license_plate,\n" +
                    "p.id AS driver_id, p.first_name as driver_first_name, p.last_name AS driver_last_name,\n" +
                    "car.current_trip_id\n" +
                    "FROM car\n" +
                    "JOIN car_model m ON m.id = car.model_id\n" +
                    "JOIN car_brand b ON b.id = m.brand_id\n" +
                    "JOIN car_category c ON c.id = car.category_id\n" +
                    "JOIN car_status s ON s.id = car.status_id\n" +
                    "LEFT JOIN person p ON p.id = car.driver_id\n" +
                    "WHERE c.title = ? AND s.title = 'available for order'" +
                    "LIMIT 4";

    private static final String FIND_ONE_BY_CATEGORY_AND_CAPACITY =
            "SELECT car.id AS id, b.id AS brand_id, b.title AS brand_title, m.id AS model_id, m.title AS model_title,\n" +
                    "car.capacity, c.id AS category_id, c.title AS category_title, s.id AS status_id, \n" +
                    "s.title AS status_title, car.license_plate,\n" +
                    "p.id AS driver_id, p.first_name as driver_first_name, p.last_name AS driver_last_name,\n" +
                    "car.current_trip_id\n" +
                    "FROM car\n" +
                    "JOIN car_model m ON m.id = car.model_id\n" +
                    "JOIN car_brand b ON b.id = m.brand_id\n" +
                    "JOIN car_category c ON c.id = car.category_id\n" +
                    "JOIN car_status s ON s.id = car.status_id\n" +
                    "LEFT JOIN person p ON p.id = car.driver_id\n" +
                    "WHERE c.title = ? AND s.title = 'available for order' AND car.capacity >= ?" +
                    "LIMIT 1";

    private static final String FIND_BY_TRIP =
            "SELECT car.id AS id, b.id AS brand_id, b.title AS brand_title, m.id AS model_id, m.title AS model_title,\n" +
                    "car.capacity, c.id AS category_id, c.title AS category_title, s.id AS status_id,\n" +
                    "s.title AS status_title, car.license_plate,\n" +
                    "p.id AS driver_id, p.first_name as driver_first_name, p.last_name AS driver_last_name,\n" +
                    "car.current_trip_id\n" +
                    "FROM car\n" +
                    "JOIN car_model m ON m.id = car.model_id\n" +
                    "JOIN car_brand b ON b.id = m.brand_id\n" +
                    "JOIN car_category c ON c.id = car.category_id\n" +
                    "JOIN car_status s ON s.id = car.status_id\n" +
                    "JOIN trip_car tc ON car.id = tc.car_id\n" +
                    "JOIN trip t ON t.id = tc.trip_id\n" +
                    "LEFT JOIN person p ON p.id = car.driver_id\n" +
                    "WHERE tc.trip_id = ?";


    private final DatabaseManager databaseManager;

    public CarRepository(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    public Integer save(Car car) {
        try (Connection connection = databaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setInt(1, car.getModel().getId());
            preparedStatement.setInt(2, car.getCapacity());
            preparedStatement.setInt(3, car.getCategory().getId());
            preparedStatement.setInt(4, car.getStatus().getId());
            preparedStatement.setString(5, car.getLicensePlate());
            preparedStatement.setObject(6, car.getDriver().getId(), Types.INTEGER);
            preparedStatement.execute();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            resultSet.next();
            Integer id = resultSet.getInt(1);
            return id;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    public void update(Car car) {
        try (Connection connection = databaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE)) {
            preparedStatement.setInt(1, car.getModel().getId());
            preparedStatement.setInt(2, car.getCapacity());
            preparedStatement.setInt(3, car.getCategory().getId());
            preparedStatement.setInt(4, car.getStatus().getId());
            preparedStatement.setString(5, car.getLicensePlate());
            preparedStatement.setObject(6, car.getDriver().getId(), Types.INTEGER);
            preparedStatement.setObject(7, car.getCurrentTrip().getId(), Types.INTEGER);
            preparedStatement.setInt(8, car.getId());
            preparedStatement.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void delete(Car car) {
        try (Connection connection = databaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE)) {
            preparedStatement.setInt(1, car.getId());
            preparedStatement.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public List<Car> findAll() {
        try (Connection connection = databaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            return mapToMany(resultSet);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return new ArrayList<>();
    }

    public List<Car> findAllWithOffsetAndLimit(int offset, int limit) {
        try (Connection connection = databaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL_WITH_OFFSET_AND_LIMIT)) {
            preparedStatement.setInt(1, offset);
            preparedStatement.setInt(2, limit);

            ResultSet resultSet = preparedStatement.executeQuery();
            return mapToMany(resultSet);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return new ArrayList<>();
    }

    public Optional<Car> findById(Integer id) {
        try (Connection connection = databaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_ID)) {
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            return mapToOne(resultSet);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return Optional.empty();
    }

    public List<Car> findSevaralByCategory(String category) {
        try (Connection connection = databaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_SEVERAL_BY_CATEGORY)) {
            preparedStatement.setString(1, category);

            ResultSet resultSet = preparedStatement.executeQuery();
            return mapToMany(resultSet);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return new ArrayList<>();
    }

    public Optional<Car> findOneByCategoryAndCapacity(String category, int capacity) {
        try (Connection connection = databaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_ONE_BY_CATEGORY_AND_CAPACITY)) {
            preparedStatement.setString(1, category);
            preparedStatement.setInt(2, capacity);

            ResultSet resultSet = preparedStatement.executeQuery();
            return mapToOne(resultSet);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return Optional.empty();
    }

    public Optional<Car> findByLicensePlate(String licensePlate) {
        try (Connection connection = databaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_LICENSE_PLATE)) {
            preparedStatement.setString(1, licensePlate);
            ResultSet resultSet = preparedStatement.executeQuery();
            return mapToOne(resultSet);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return Optional.empty();
    }


    public List<Car> findByTripId(Integer tripId) {
        try (Connection connection = databaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_TRIP)) {
            preparedStatement.setInt(1, tripId);

            ResultSet resultSet = preparedStatement.executeQuery();
            return mapToMany(resultSet);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return new ArrayList<>();
    }

    public int findTotalNumber() {
        int number = 0;
        try (Connection connection = databaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_TOTAL_NUMBER)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            number = resultSet.getInt("number");
            return number;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return number;
    }

    private Optional<Car> mapToOne(ResultSet resultSet) throws SQLException {
        Car car = null;
        while (resultSet.next()) {
            car = getCarFromResultSet(resultSet);
        }
        return Optional.ofNullable(car);
    }

    private List<Car> mapToMany(ResultSet resultSet) throws SQLException {
        List<Car> cars = new ArrayList<>();
        while (resultSet.next()) {
            Car car = getCarFromResultSet(resultSet);
            cars.add(car);
        }
        return cars;
    }

    private Car getCarFromResultSet(ResultSet resultSet) throws SQLException {
        Car car = new Car();
        CarModel carModel = new CarModel();
        CarCategory category = new CarCategory();
        CarStatus status = new CarStatus();
        car.setId(resultSet.getInt("id"));
        carModel.setId(Integer.parseInt(resultSet.getString("model_id")));
        carModel.setBrand(resultSet.getString("brand_title"));
        carModel.setModel(resultSet.getString("model_title"));
        car.setModel(carModel);
        car.setCapacity(resultSet.getInt("capacity"));
        category.setId(resultSet.getInt("category_id"));
        category.setTitle(resultSet.getString("category_title"));
        car.setCategory(category);
        status.setId(resultSet.getInt("status_id"));
        status.setTitle(resultSet.getString("status_title"));
        car.setStatus(status);
        car.setLicensePlate(resultSet.getString("license_plate"));

        Integer driverId = resultSet.getInt("driver_id");
        if (!resultSet.wasNull()) {
            User driver = new User();
            driver.setId(driverId);
            driver.setFirstName(resultSet.getString("driver_first_name"));
            driver.setLastName(resultSet.getString("driver_last_name"));
            car.setDriver(driver);
        }
        if (!resultSet.wasNull()) {
            Trip currentTrip = new Trip();
            currentTrip.setId(resultSet.getInt("current_trip_id"));
            car.setCurrentTrip(currentTrip);
        }
        return car;
    }
}
