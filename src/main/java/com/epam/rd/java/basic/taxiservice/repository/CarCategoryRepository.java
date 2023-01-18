package com.epam.rd.java.basic.taxiservice.repository;

import com.epam.rd.java.basic.taxiservice.config.DatabaseManager;
import com.epam.rd.java.basic.taxiservice.model.Car.CarCategory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CarCategoryRepository {
    private static final String INSERT =
            "INSERT INTO car_category (title)\n" +
            "VALUES\n" +
            "(?);";
    private static final String UPDATE = "UPDATE companies SET company_name = ?, company_location = ? WHERE company_id = ?;";
    private static final String FIND_ALL =
            "SELECT id, title\n" +
            "FROM car_category\n" +
            "ORDER BY title;";
    private static final String DELETE = "DELETE FROM companies WHERE companies.company_id = ?;";
    private static final String FIND_BY_ID =
            "SELECT id, title\n" +
            "FROM car_category\n" +
            "WHERE id = ?";
    private static final String FIND_BY_TITLE =
            "SELECT id, title\n" +
            "FROM car_category\n" +
            "WHERE title = ?";

    private final DatabaseManager databaseManager;

    public CarCategoryRepository(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    public Optional<CarCategory> findByTitle(String name) {
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

    public Integer save(CarCategory category) {
        try (Connection connection = databaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, category.getTitle());
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

    public void update(CarCategory category) {
        try (Connection connection = databaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE)) {
            preparedStatement.setString(1, category.getTitle());
            preparedStatement.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void delete(CarCategory category) {
        try (Connection connection = databaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE)) {
            preparedStatement.setInt(1, category.getId());
            preparedStatement.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public List<CarCategory> findAll() {
        try (Connection connection = databaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            return mapToMany(resultSet);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return new ArrayList<>();
    }

    public Optional<CarCategory> findById(Integer id) {
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

    private Optional<CarCategory> mapToOne(ResultSet resultSet) throws SQLException {
        CarCategory category = null;
        while (resultSet.next()) {
            category = getEntityFromResultSet(resultSet);
        }
        return Optional.ofNullable(category);
    }

    private List<CarCategory> mapToMany(ResultSet resultSet) throws SQLException {
        List<CarCategory> carCategories = new ArrayList<>();
        while (resultSet.next()) {
            CarCategory category = getEntityFromResultSet(resultSet);
            carCategories.add(category);
        }
        return carCategories;
    }

    private CarCategory getEntityFromResultSet(ResultSet resultSet) throws SQLException {
        CarCategory category = new CarCategory();
        category.setId(Integer.parseInt(resultSet.getString("id")));
        category.setTitle(resultSet.getString("title"));
        return category;
    }
}
