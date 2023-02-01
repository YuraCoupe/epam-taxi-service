package com.epam.rd.java.basic.taxiservice.repository;

import com.epam.rd.java.basic.taxiservice.config.DatabaseManager;
import com.epam.rd.java.basic.taxiservice.model.Car.CarModel;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CarModelRepository {
    private static final String INSERT =
            "INSERT INTO car_model (brand_id, title)\n" +
                    "   SELECT b.id, c.model\n" +
                    "       FROM (\n" +
                    "           VALUES\n" +
                    "               (?, ?)\n" +
                    "           ) AS c (brand, model)\n" +
                    "JOIN car_brand b ON c.brand = b.title;";
    private static final String UPDATE = "UPDATE companies SET company_name = ?, company_location = ? WHERE company_id = ?;";
    private static final String FIND_ALL =
            "SELECT m.id AS id, b.id AS brand_id, b.title as brand_title, m.id AS model_id, m.title AS model_title\n" +
            "FROM car_model m\n" +
            "JOIN car_brand b ON b.id = m.brand_id\n" +
            "ORDER BY b.title, m.title;";
    private static final String FIND_ALL_EX_UNEMPLOYED = "SELECT * FROM companies WHERE company_name <> 'Unemployed' ORDER BY company_name;";
    private static final String DELETE = "DELETE FROM companies WHERE companies.company_id = ?;";
    private static final String FIND_BY_ID =
            "SELECT m.id AS model_id, b.id AS brand_id, b.title as brand_title, m.title AS model_title\n" +
            "FROM car_model m\n" +
            "JOIN car_brand b ON b.id = m.brand_id\n" +
            "WHERE m.id = ?";
    private static final String FIND_BY_BRAND =
            "SELECT m.id AS id, b.id AS brand_id, b.title as brand_title, m.title AS model_title\n" +
            "FROM car_model m\n" +
            "JOIN car_brand b ON b.id = m.brand_id\n" +
            "WHERE b.title = ?";

    private static final String FIND_BY_MODEL =
            "SELECT m.id AS id, b.id AS brand_id, b.title as brand_title, m.title AS model_title\n" +
            "FROM car_model m\n" +
            "JOIN car_brand b ON b.id = m.brand_id\n" +
            "WHERE b.title = 'Mercedes-Benz' AND m.title = 'E-class';";

    private final DatabaseManager databaseManager;

    public CarModelRepository(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    public Optional<CarModel> findByBrand(String name) {
        try (Connection connection = databaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_BRAND)) {
            preparedStatement.setString(1, name);
            ResultSet resultSet = preparedStatement.executeQuery();
            return mapToOne(resultSet);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return Optional.empty();
    }

    public Optional<CarModel> findByModel(String name) {
        try (Connection connection = databaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_BRAND)) {
            preparedStatement.setString(1, name);
            ResultSet resultSet = preparedStatement.executeQuery();
            return mapToOne(resultSet);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return Optional.empty();
    }

    public Integer save(CarModel model) {
        try (Connection connection = databaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, model.getBrand());
            preparedStatement.setString(2, model.getModel());
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

    public void update(CarModel model) {
        try (Connection connection = databaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE)) {
            preparedStatement.setString(1, model.getBrand());
            preparedStatement.setString(1, model.getModel());
            preparedStatement.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void delete(CarModel model) {
        try (Connection connection = databaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE)) {
            preparedStatement.setInt(1, model.getId());
            preparedStatement.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public List<CarModel> findAll() {
        try (Connection connection = databaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            return mapToMany(resultSet);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return new ArrayList<>();
    }

    public Optional<CarModel> findById(Integer id) {
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

    private Optional<CarModel> mapToOne(ResultSet resultSet) throws SQLException {
        CarModel carModel = null;
        while (resultSet.next()) {
            carModel = getEntityFromResultSet(resultSet);
        }
        return Optional.ofNullable(carModel);
    }

    private List<CarModel> mapToMany(ResultSet resultSet) throws SQLException {
        List<CarModel> carModels = new ArrayList<>();
        while (resultSet.next()) {
            CarModel model = getEntityFromResultSet(resultSet);
            carModels.add(model);
        }
        return carModels;
    }

    private CarModel getEntityFromResultSet(ResultSet resultSet) throws SQLException {
        CarModel carModel = new CarModel();
        carModel.setId(Integer.parseInt(resultSet.getString("model_id")));
        carModel.setBrand(resultSet.getString("brand_title"));
        carModel.setModel(resultSet.getString("model_title"));
        return carModel;
    }
}
