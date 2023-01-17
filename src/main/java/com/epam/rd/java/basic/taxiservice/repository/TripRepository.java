package com.epam.rd.java.basic.taxiservice.repository;

import com.epam.rd.java.basic.taxiservice.config.DatabaseManager;
import com.epam.rd.java.basic.taxiservice.model.*;
import com.epam.rd.java.basic.taxiservice.model.Car.Car;
import com.epam.rd.java.basic.taxiservice.model.Car.CarCategory;
import com.epam.rd.java.basic.taxiservice.model.Car.CarModel;
import com.epam.rd.java.basic.taxiservice.model.Car.CarStatus;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TripRepository {
    private static final String INSERT =
            "INSERT INTO trip (person_id, dep_address_id, dest_address_id, number_of_passengers, \n" +
                    "category_id, price, status_id, open_time, distance)\n" +
                    "VALUES\n" +
                    "(?, ?, ?, ?, ?, ?, ?, ?, ?);";
    private static final String UPDATE =
            "UPDATE trip\n" +
                    "SET\n" +
                    "person_id = ?,\n" +
                    "dep_address_id = ?,\n" +
                    "dest_address_id = ?,\n" +
                    "number_of_passengers = ?,\n" +
                    "category_id = ?,\n" +
                    "price = ?,\n" +
                    "status_id = ?,\n" +
                    "open_time = ?,\n" +
                    "distance = ?\n" +
                    "WHERE id = ?;";
    private static final String FIND_ALL =
            "SELECT t.id, t.person_id, p.phone_number AS phone_number, p.first_name AS first_name, \n" +
                    "p.last_name AS last_name, dep_address_id, depst.title AS dep_str_type, \n" +
                    "deps.title AS dep_str_title, dep.building_number AS dep_building,\n" +
                    "dest_address_id, destst.title AS dest_str_type, \n" +
                    "dests.title AS dest_str_title, dest.building_number AS dest_building, \n" +
                    "number_of_passengers, \n" +
                    "category_id, c.title AS category, price, status_id, s.title AS status, open_time, close_time, distance\n" +
                    "FROM trip t\n" +
                    "JOIN person p ON p.id = t.person_id\n" +
                    "JOIN address dep ON dep_address_id = dep.id\n" +
                    "JOIN address dest ON dest_address_id = dest.id\n" +
                    "JOIN street deps ON deps.id = dep.street_id\n" +
                    "JOIN street_type depst ON depst.id = deps.type_id\n" +
                    "JOIN street dests ON dests.id = dest.street_id\n" +
                    "JOIN street_type destst ON destst.id = dests.type_id\n" +
                    "JOIN car_category c ON c.id = category_id\n" +
                    "JOIN trip_status s ON s.id = status_id;";
    private static final String FIND_ALL_WITH_OFFSET_AND_LIMIT =
            "SELECT t.id, t.person_id, p.phone_number AS phone_number, p.first_name AS first_name, \n" +
                    "p.last_name AS last_name, dep_address_id, depst.title AS dep_str_type, \n" +
                    "deps.title AS dep_str_title, dep.building_number AS dep_building,\n" +
                    "dest_address_id, destst.title AS dest_str_type, \n" +
                    "dests.title AS dest_str_title, dest.building_number AS dest_building, \n" +
                    "number_of_passengers, \n" +
                    "category_id, c.title AS category, price, status_id, s.title AS status, open_time, close_time, distance\n" +
                    "FROM trip t\n" +
                    "JOIN person p ON p.id = t.person_id\n" +
                    "JOIN address dep ON dep_address_id = dep.id\n" +
                    "JOIN address dest ON dest_address_id = dest.id\n" +
                    "JOIN street deps ON deps.id = dep.street_id\n" +
                    "JOIN street_type depst ON depst.id = deps.type_id\n" +
                    "JOIN street dests ON dests.id = dest.street_id\n" +
                    "JOIN street_type destst ON destst.id = dests.type_id\n" +
                    "JOIN car_category c ON c.id = category_id\n" +
                    "JOIN trip_status s ON s.id = status_id\n" +
                    "OFFSET ? LIMIT ?;";
    private static final String GET_TOTAL_NUMBER =
            "SELECT COUNT (*) as number\n" +
                    "FROM trip;";
    private static final String GET_TOTAL_NUMBER_BY_USER =
            "SELECT COUNT (*) as number\n" +
                    "FROM trip\n" +
                    "WHERE person_id = ?;";
    private static final String GET_TOTAL_NUMBER_BY_DRIVER =
            "SELECT COUNT (*) as number\n" +
                    "FROM trip t\n" +
                    "JOIN trip_car tc ON tc.trip_id = t.id\n" +
                    "JOIN car c ON tc.car_id = c.id\n" +
                    "WHERE c.driver_id = ?;";
    private static final String DELETE =
            "DELETE FROM trip_car WHERE trip_id = ?;\n" +
                    "DELETE FROM trip WHERE trip.id = ?;";
    private static final String FIND_BY_ID =
            "SELECT t.id, t.person_id, p.phone_number AS phone_number, p.first_name AS first_name, \n" +
                    "\tp.last_name AS last_name, dep_address_id, depst.title AS dep_str_type, \n" +
                    "\tdeps.title AS dep_str_title, dep.building_number AS dep_building,\n" +
                    "\tdest_address_id, destst.title AS dest_str_type, \n" +
                    "\tdests.title AS dest_str_title, dest.building_number AS dest_building, \n" +
                    "\tnumber_of_passengers, \n" +
                    "\tcategory_id, c.title AS category, price, status_id, s.title AS status, open_time, close_time, distance\n" +
                    "FROM trip t\n" +
                    "JOIN person p ON p.id = t.person_id\n" +
                    "JOIN address dep ON dep_address_id = dep.id\n" +
                    "JOIN address dest ON dest_address_id = dest.id\n" +
                    "JOIN street deps ON deps.id = dep.street_id\n" +
                    "JOIN street_type depst ON depst.id = deps.type_id\n" +
                    "JOIN street dests ON dests.id = dest.street_id\n" +
                    "JOIN street_type destst ON destst.id = dests.type_id\n" +
                    "JOIN car_category c ON c.id = category_id\n" +
                    "JOIN trip_status s ON s.id = status_id\n" +
                    "WHERE t.id = ?";
    private static final String FIND_BY_USER_ID_WITH_OFFSET_AND_LIMIT =
            "SELECT t.id, t.person_id, p.phone_number AS phone_number, p.first_name AS first_name, \n" +
                    "p.last_name AS last_name, dep_address_id, depst.title AS dep_str_type, \n" +
                    "deps.title AS dep_str_title, dep.building_number AS dep_building,\n" +
                    "dest_address_id, destst.title AS dest_str_type, \n" +
                    "dests.title AS dest_str_title, dest.building_number AS dest_building, \n" +
                    "number_of_passengers, \n" +
                    "category_id, c.title AS category, price, status_id, s.title AS status, open_time, close_time, distance\n" +
                    "FROM trip t\n" +
                    "JOIN person p ON p.id = t.person_id\n" +
                    "JOIN address dep ON dep_address_id = dep.id\n" +
                    "JOIN address dest ON dest_address_id = dest.id\n" +
                    "JOIN street deps ON deps.id = dep.street_id\n" +
                    "JOIN street_type depst ON depst.id = deps.type_id\n" +
                    "JOIN street dests ON dests.id = dest.street_id\n" +
                    "JOIN street_type destst ON destst.id = dests.type_id\n" +
                    "JOIN car_category c ON c.id = category_id\n" +
                    "JOIN trip_status s ON s.id = status_id\n" +
                    "WHERE t.person_id = ?\n" +
                    "OFFSET ? LIMIT ?;";
    private static final String FIND_BY_DRIVER_ID_WITH_OFFSET_AND_LIMIT =
            "SELECT t.id, t.person_id, p.phone_number AS phone_number, p.first_name AS first_name,\n" +
                    "p.last_name AS last_name, dep_address_id, depst.title AS dep_str_type, \n" +
                    "deps.title AS dep_str_title, dep.building_number AS dep_building,\n" +
                    "dest_address_id, destst.title AS dest_str_type,\n" +
                    "dests.title AS dest_str_title, dest.building_number AS dest_building,\n" +
                    "number_of_passengers,\n" +
                    "t.category_id, c.title AS category, price, t.status_id, s.title AS status, open_time, close_time, distance\n" +
                    "FROM trip t\n" +
                    "JOIN person p ON p.id = t.person_id\n" +
                    "JOIN address dep ON dep_address_id = dep.id\n" +
                    "JOIN address dest ON dest_address_id = dest.id\n" +
                    "JOIN street deps ON deps.id = dep.street_id\n" +
                    "JOIN street_type depst ON depst.id = deps.type_id\n" +
                    "JOIN street dests ON dests.id = dest.street_id\n" +
                    "JOIN street_type destst ON destst.id = dests.type_id\n" +
                    "JOIN car_category c ON c.id = category_id\n" +
                    "JOIN trip_status s ON s.id = status_id\n" +
                    "JOIN trip_car tc ON tc.trip_id = t.id\n" +
                    "JOIN car ON car.id = tc.car_id\n" +
                    "WHERE car.driver_id = ?\n" +
                    "OFFSET ? LIMIT ?;";

    private static final String INSERT_TRIP_CAR =
            "INSERT INTO trip_car (trip_id, car_id)\n" +
                    "VALUES\n" +
                    "(?, ?);";

    private static final String FIND_ACTIVE_TRIP_BY_CLIENT_ID =
            "SELECT t.id, t.person_id, p.phone_number AS phone_number, p.first_name AS first_name, \n" +
                    "\tp.last_name AS last_name, dep_address_id, depst.title AS dep_str_type, \n" +
                    "\tdeps.title AS dep_str_title, dep.building_number AS dep_building,\n" +
                    "\tdest_address_id, destst.title AS dest_str_type, \n" +
                    "\tdests.title AS dest_str_title, dest.building_number AS dest_building, \n" +
                    "\tnumber_of_passengers, \n" +
                    "\tcategory_id, c.title AS category, price, status_id, s.title AS status, open_time, close_time, distance\n" +
                    "FROM trip t\n" +
                    "JOIN person p ON p.id = t.person_id\n" +
                    "JOIN address dep ON dep_address_id = dep.id\n" +
                    "JOIN address dest ON dest_address_id = dest.id\n" +
                    "JOIN street deps ON deps.id = dep.street_id\n" +
                    "JOIN street_type depst ON depst.id = deps.type_id\n" +
                    "JOIN street dests ON dests.id = dest.street_id\n" +
                    "JOIN street_type destst ON destst.id = dests.type_id\n" +
                    "JOIN car_category c ON c.id = category_id\n" +
                    "JOIN trip_status s ON s.id = status_id\n" +
                    "WHERE p.id = ? AND s.title <> 'Completed';";

    private static final String FIND_ACTIVE_TRIP_BY_DRIVER_ID =
            "SELECT t.id, t.person_id, p.phone_number AS phone_number, p.first_name AS first_name, \n" +
                    "p.last_name AS last_name, dep_address_id, depst.title AS dep_str_type, \n" +
                    "deps.title AS dep_str_title, dep.building_number AS dep_building,\n" +
                    "dest_address_id, destst.title AS dest_str_type,\n" +
                    "dests.title AS dest_str_title, dest.building_number AS dest_building, \n" +
                    "number_of_passengers, \n" +
                    "t.category_id, c.title AS category, price, t.status_id, s.title AS status, open_time, close_time, distance\n" +
                    "FROM trip t\n" +
                    "JOIN person p ON p.id = t.person_id\n" +
                    "JOIN address dep ON dep_address_id = dep.id\n" +
                    "JOIN address dest ON dest_address_id = dest.id\n" +
                    "JOIN street deps ON deps.id = dep.street_id\n" +
                    "JOIN street_type depst ON depst.id = deps.type_id\n" +
                    "JOIN street dests ON dests.id = dest.street_id\n" +
                    "JOIN street_type destst ON destst.id = dests.type_id\n" +
                    "JOIN car_category c ON c.id = category_id\n" +
                    "JOIN trip_status s ON s.id = status_id\n" +
                    "JOIN trip_car tc ON tc.trip_id = t.id\n" +
                    "JOIN car ON car.id = tc.car_id\n" +
                    "WHERE car.driver_id = ? AND s.title != 'Completed' AND car.current_trip_id IS NOT NULL;";

    private final DatabaseManager databaseManager;

    public TripRepository(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    public Integer save(Trip trip) {
        try (Connection connection = databaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setInt(1, trip.getUser().getId());
            preparedStatement.setInt(2, trip.getDepartureAddress().getId());
            preparedStatement.setInt(3, trip.getDestinationAddress().getId());
            preparedStatement.setInt(4, trip.getNumberOfPassengers());
            preparedStatement.setInt(5, trip.getCategory().getId());
            preparedStatement.setDouble(6, trip.getPrice());
            preparedStatement.setInt(7, trip.getStatus().getId());
            preparedStatement.setTimestamp(8, trip.getOpenTime());
            preparedStatement.setDouble(9, trip.getDistance());
            preparedStatement.execute();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            resultSet.next();
            Integer id = resultSet.getInt(1);
            trip.setId(id);
            if (trip.getCars() != null) {
                saveTripCars(trip);
            }
            return id;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    public void update(Trip trip) {
        try (Connection connection = databaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setInt(1, trip.getUser().getId());
            preparedStatement.setInt(2, trip.getDepartureAddress().getId());
            preparedStatement.setInt(3, trip.getDestinationAddress().getId());
            preparedStatement.setInt(4, trip.getNumberOfPassengers());
            preparedStatement.setInt(5, trip.getCategory().getId());
            preparedStatement.setDouble(6, trip.getPrice());
            preparedStatement.setInt(7, trip.getStatus().getId());
            preparedStatement.setTimestamp(8, trip.getOpenTime());
            preparedStatement.setDouble(9, trip.getDistance());
            preparedStatement.setInt(10, trip.getId());
            preparedStatement.execute();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            resultSet.next();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void delete(Trip trip) {
        try (Connection connection = databaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE)) {
            preparedStatement.setInt(1, trip.getId());
            preparedStatement.setInt(2, trip.getId());
            preparedStatement.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public List<Trip> findAll() {
        try (Connection connection = databaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            return mapToMany(resultSet);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return new ArrayList<>();
    }

    public List<Trip> findAllWithOffsetAndLimit(int offset, int limit) {
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

    public Optional<Trip> findById(Integer id) {
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

    public List<Trip> findByUserIdWithOffsetAndLimit(Integer userId, int offset, int limit) {
        try (Connection connection = databaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_USER_ID_WITH_OFFSET_AND_LIMIT)) {
            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, offset);
            preparedStatement.setInt(3, limit);
            ResultSet resultSet = preparedStatement.executeQuery();
            return mapToMany(resultSet);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return new ArrayList<>();
    }

    public List<Trip> findByDriverIdWithOffsetAndLimit(Integer driverId, int offset, int limit) {
        try (Connection connection = databaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_DRIVER_ID_WITH_OFFSET_AND_LIMIT)) {
            preparedStatement.setInt(1, driverId);
            preparedStatement.setInt(2, offset);
            preparedStatement.setInt(3, limit);
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

    public int findTotalNumberByUser(Integer userId) {
        int number = 0;
        try (Connection connection = databaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_TOTAL_NUMBER_BY_USER)) {
            preparedStatement.setInt(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            number = resultSet.getInt("number");
            return number;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return number;
    }

    public int findTotalNumberByDriver(Integer driverId) {
        int number = 0;
        try (Connection connection = databaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_TOTAL_NUMBER_BY_DRIVER)) {
            preparedStatement.setInt(1, driverId);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            number = resultSet.getInt("number");
            return number;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return number;
    }

    public Optional<Trip> findActiveTripByClientId(Integer clientId) {
        try (Connection connection = databaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_ACTIVE_TRIP_BY_CLIENT_ID)) {
            preparedStatement.setLong(1, clientId);
            ResultSet resultSet = preparedStatement.executeQuery();
            return mapToOne(resultSet);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return Optional.empty();
    }

    public Optional<Trip> findActiveTripByDriverId(Integer driverId) {
        try (Connection connection = databaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_ACTIVE_TRIP_BY_DRIVER_ID)) {
            preparedStatement.setLong(1, driverId);
            ResultSet resultSet = preparedStatement.executeQuery();
            return mapToOne(resultSet);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return Optional.empty();
    }


    private void saveTripCars(Trip trip) {
        try (Connection connection = databaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_TRIP_CAR, Statement.RETURN_GENERATED_KEYS)) {
            for (Car car : trip.getCars()) {
                preparedStatement.setInt(1, trip.getId());
                preparedStatement.setInt(2, car.getId());
                preparedStatement.execute();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private Optional<Trip> mapToOne(ResultSet resultSet) throws SQLException {
        Trip trip = null;
        while (resultSet.next()) {
            trip = getTripFromResultSet(resultSet);
        }
        return Optional.ofNullable(trip);
    }

    private List<Trip> mapToMany(ResultSet resultSet) throws SQLException {
        List<Trip> trips = new ArrayList<>();
        while (resultSet.next()) {
            Trip trip = getTripFromResultSet(resultSet);
            trips.add(trip);
        }
        return trips;
    }

    private Trip getTripFromResultSet(ResultSet resultSet) throws SQLException {
        Trip trip = new Trip();
        trip.setId(resultSet.getInt("id"));
        User user = new User();
        user.setId(resultSet.getInt("person_id"));
        user.setPhoneNumber(resultSet.getString("phone_number"));
        user.setFirstName(resultSet.getString("first_name"));
        user.setLastName(resultSet.getString("last_name"));
        trip.setUser(user);
        Address departureAddress = new Address();
        departureAddress.setId(resultSet.getInt("dep_address_id"));
        Street departureStreet = new Street();
        departureStreet.setStreetType(resultSet.getString("dep_str_type"));
        departureStreet.setTitle(resultSet.getString("dep_str_title"));
        departureAddress.setStreet(departureStreet);
        departureAddress.setBuilding(resultSet.getString("dep_building"));
        trip.setDepartureAddress(departureAddress);
        Address destinationAddress = new Address();
        destinationAddress.setId(resultSet.getInt("dest_address_id"));
        Street destinationStreet = new Street();
        destinationStreet.setStreetType(resultSet.getString("dest_str_type"));
        destinationStreet.setTitle(resultSet.getString("dest_str_title"));
        destinationAddress.setStreet(destinationStreet);
        destinationAddress.setBuilding(resultSet.getString("dest_building"));
        trip.setDestinationAddress(destinationAddress);
        CarCategory category = new CarCategory();
        category.setId(resultSet.getInt("category_id"));
        category.setTitle(resultSet.getString("category"));
        trip.setCategory(category);
        TripStatus status = new TripStatus();
        status.setId(resultSet.getInt("status_id"));
        status.setTitle(resultSet.getString("status"));
        trip.setStatus(status);
        trip.setNumberOfPassengers(resultSet.getInt("number_of_passengers"));
        trip.setPrice(resultSet.getDouble("price"));
        trip.setDistance(resultSet.getDouble("distance"));
        trip.setOpenTime(resultSet.getTimestamp("open_time"));
        return trip;
    }
}
