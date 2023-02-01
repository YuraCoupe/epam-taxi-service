package com.epam.rd.java.basic.taxiservice.repository;

import com.epam.rd.java.basic.taxiservice.config.DatabaseManager;
import com.epam.rd.java.basic.taxiservice.model.DiscountRate;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DiscountRateRepository {
    private static final String INSERT =
            "INSERT INTO discount_rate (money_spent, discount)\n" +
                    "VALUES\n" +
                    "(?, ?, ?);";
    private static final String UPDATE = "UPDATE discount_rate SET money_spent = ?, discount = ? WHERE id = ?;";
    private static final String FIND_ALL =
            "SELECT id, money_spent, discount\n" +
                    "FROM discount_rate\n" +
                    "ORDER BY id;";
    private static final String DELETE = "DELETE FROM discount_rate WHERE id = ?;";
    private static final String FIND_BY_ID =
            "SELECT id, money_spent, discount\n" +
                    "FROM discount_rate\n" +
                    "WHERE id = ?";
    private static final String FIND_BY_MONEY_SPENT =
            "SELECT id, MAX(money_spent) AS total_spent, discount\n" +
                    "FROM discount_rate\n" +
                    "WHERE money_spent = (SELECT MAX(money_spent) FROM discount_rate WHERE money_spent <= ?)\n" +
                    "GROUP BY id;";

    private final DatabaseManager databaseManager;

    public DiscountRateRepository(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    public Optional<DiscountRate> findByMoneySpent(BigDecimal moneySpent) {
        try (Connection connection = databaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_MONEY_SPENT)) {
            preparedStatement.setBigDecimal(1, moneySpent);
            ResultSet resultSet = preparedStatement.executeQuery();
            return mapToOne(resultSet);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return Optional.empty();
    }

    public Integer save(DiscountRate discountRate) {
        try (Connection connection = databaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setBigDecimal(1, discountRate.getMoneySpent());
            preparedStatement.setInt(2, discountRate.getDiscountRate());
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

    public void update(DiscountRate discountRate) {
        try (Connection connection = databaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE)) {
            preparedStatement.setBigDecimal(1, discountRate.getMoneySpent());
            preparedStatement.setInt(2, discountRate.getDiscountRate());
            preparedStatement.setInt(3, discountRate.getId());
            preparedStatement.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void delete(DiscountRate discountRate) {
        try (Connection connection = databaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE)) {
            preparedStatement.setInt(1, discountRate.getId());
            preparedStatement.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public List<DiscountRate> findAll() {
        try (Connection connection = databaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            return mapToMany(resultSet);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return new ArrayList<>();
    }

    public Optional<DiscountRate> findById(Integer id) {
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

    private Optional<DiscountRate> mapToOne(ResultSet resultSet) throws SQLException {
        DiscountRate discountRate = null;
        while (resultSet.next()) {
            discountRate = getEntityFromResultSet(resultSet);
        }
        return Optional.ofNullable(discountRate);
    }

    private List<DiscountRate> mapToMany(ResultSet resultSet) throws SQLException {
        List<DiscountRate> discountRates = new ArrayList<>();
        while (resultSet.next()) {
            DiscountRate discountRate = getEntityFromResultSet(resultSet);
            discountRates.add(discountRate);
        }
        return discountRates;
    }

    private DiscountRate getEntityFromResultSet(ResultSet resultSet) throws SQLException {
        DiscountRate discountRate = new DiscountRate();
        discountRate.setId(Integer.parseInt(resultSet.getString("id")));
        discountRate.setMoneySpent(resultSet.getBigDecimal("total_spent"));
        discountRate.setDiscountRate(resultSet.getInt("discount"));
        return discountRate;
    }
}
