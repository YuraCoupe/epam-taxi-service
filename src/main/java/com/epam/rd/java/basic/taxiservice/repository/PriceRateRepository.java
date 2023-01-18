package com.epam.rd.java.basic.taxiservice.repository;

import com.epam.rd.java.basic.taxiservice.config.DatabaseManager;
import com.epam.rd.java.basic.taxiservice.model.Car.CarCategory;
import com.epam.rd.java.basic.taxiservice.model.Car.CarStatus;
import com.epam.rd.java.basic.taxiservice.model.PriceRate;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PriceRateRepository {
    private static final String INSERT =
            "INSERT INTO price_rate (category_id, rate, min_order_price)\n" +
                    "VALUES\n" +
                    "(?, ?, ?);";
    private static final String UPDATE = "UPDATE price_rate SET category_id = ?, rate = ?, min_order_price = ? WHERE id = ?;";
    private static final String FIND_ALL =
            "SELECT id, category_id, c.title AS category_title, rate, min_order_price\n" +
                    "FROM price_rate\n" +
                    "JOIN car_category ON category_id = c.id\n" +
                    "ORDER BY category_id;";
    private static final String DELETE = "DELETE FROM price_rate WHERE id = ?;";
    private static final String FIND_BY_ID =
            "SELECT id, category_id, c.title AS category_title, rate, min_order_price\n" +
                    "FROM price_rate\n" +
                    "JOIN car_category ON category_id = c.id\n" +
                    "WHERE id = ?";
    private static final String FIND_BY_CATEGORY =
            "SELECT id, category_id, c.title AS category_title, rate, min_order_price\n" +
                    "FROM price_rate\n" +
                    "JOIN car_category ON category_id = c.id\n" +
                    "WHERE c.title = ?";

    private final DatabaseManager databaseManager;

    public PriceRateRepository(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    public Optional<PriceRate> findByCategory(String category) {
        try (Connection connection = databaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_CATEGORY)) {
            preparedStatement.setString(1, category);
            ResultSet resultSet = preparedStatement.executeQuery();
            return mapToOne(resultSet);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return Optional.empty();
    }

    public Integer save(PriceRate priceRate) {
        try (Connection connection = databaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setInt(1, priceRate.getCategory().getId());
            preparedStatement.setBigDecimal(2, priceRate.getRate());
            preparedStatement.setBigDecimal(3, priceRate.getMinOrderPrice());
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

    public void update(PriceRate priceRate) {
        try (Connection connection = databaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE)) {
            preparedStatement.setInt(1, priceRate.getCategory().getId());
            preparedStatement.setBigDecimal(2, priceRate.getRate());
            preparedStatement.setBigDecimal(3, priceRate.getMinOrderPrice());
            preparedStatement.setInt(4, priceRate.getId());
            preparedStatement.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void delete(PriceRate priceRate) {
        try (Connection connection = databaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE)) {
            preparedStatement.setInt(1, priceRate.getId());
            preparedStatement.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public List<PriceRate> findAll() {
        try (Connection connection = databaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            return mapToMany(resultSet);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return new ArrayList<>();
    }

    public Optional<PriceRate> findById(Integer id) {
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

    private Optional<PriceRate> mapToOne(ResultSet resultSet) throws SQLException {
        PriceRate priceRate = null;
        while (resultSet.next()) {
            priceRate = getCarStatusFromResultSet(resultSet);
        }
        return Optional.ofNullable(priceRate);
    }

    private List<PriceRate> mapToMany(ResultSet resultSet) throws SQLException {
        List<PriceRate> priceRates = new ArrayList<>();
        while (resultSet.next()) {
            PriceRate priceRate = getCarStatusFromResultSet(resultSet);
            priceRates.add(priceRate);
        }
        return priceRates;
    }

    private PriceRate getCarStatusFromResultSet(ResultSet resultSet) throws SQLException {
        PriceRate priceRate = new PriceRate();
        priceRate.setId(Integer.parseInt(resultSet.getString("id")));
        CarCategory category = new CarCategory();
        category.setId(resultSet.getInt("category_id"));
        category.setTitle(resultSet.getString("category_title"));
        priceRate.setCategory(category);
        priceRate.setRate(resultSet.getBigDecimal("rate"));
        priceRate.setMinOrderPrice(resultSet.getBigDecimal("min_order_price"));
        return priceRate;
    }
}
