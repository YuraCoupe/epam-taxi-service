package com.epam.rd.java.basic.taxiservice.repository;

import com.epam.rd.java.basic.taxiservice.config.DatabaseManager;
import com.epam.rd.java.basic.taxiservice.model.Car.CarStatus;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CarStatusRepository {
    private static final String INSERT =
            "INSERT INTO car_status (title)\n" +
                    "VALUES\n" +
                    "(?);";
    private static final String UPDATE = "UPDATE companies SET company_name = ?, company_location = ? WHERE company_id = ?;";
    private static final String FIND_ALL =
            "SELECT id, title\n" +
                    "FROM car_status\n" +
                    "ORDER BY title;";
    private static final String DELETE = "DELETE FROM companies WHERE companies.company_id = ?;";
    private static final String FIND_BY_ID =
            "SELECT id, title\n" +
                    "FROM car_status\n" +
                    "WHERE id = ?";
    private static final String FIND_BY_TITLE =
            "SELECT id, title\n" +
                    "FROM car_status\n" +
                    "WHERE title = ?";

    private final DatabaseManager databaseManager;

    public CarStatusRepository(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    public Optional<CarStatus> findByTitle(String name) {
        try (Connection connection = databaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_TITLE)) {
            preparedStatement.setString(1, name);
            ResultSet resultSet = preparedStatement.executeQuery();
            return mapToOne(resultSet);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return Optional.empty();
    }

    public Integer save(CarStatus status) {
        try (Connection connection = databaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, status.getTitle());
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

    public void update(CarStatus status) {
        try (Connection connection = databaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE)) {
            preparedStatement.setString(1, status.getTitle());
            preparedStatement.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void delete(CarStatus status) {
        try (Connection connection = databaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE)) {
            preparedStatement.setInt(1, status.getId());
            preparedStatement.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public List<CarStatus> findAll() {
        try (Connection connection = databaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            return mapToMany(resultSet);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return new ArrayList<>();
    }

    public Optional<CarStatus> findById(Integer id) {
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

    private Optional<CarStatus> mapToOne(ResultSet resultSet) throws SQLException {
        CarStatus status = null;
        while (resultSet.next()) {
            status = getCarStatusFromResultSet(resultSet);
        }
        return Optional.ofNullable(status);
    }

    private List<CarStatus> mapToMany(ResultSet resultSet) throws SQLException {
        List<CarStatus> carStatuses = new ArrayList<>();
        while (resultSet.next()) {
            CarStatus status = getCarStatusFromResultSet(resultSet);
            carStatuses.add(status);
        }
        return carStatuses;
    }

    private CarStatus getCarStatusFromResultSet(ResultSet resultSet) throws SQLException {
        CarStatus status = new CarStatus();
        status.setId(Integer.parseInt(resultSet.getString("id")));
        status.setTitle(resultSet.getString("title"));
        return status;
    }
}
