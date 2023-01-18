package com.epam.rd.java.basic.taxiservice.repository;

import com.epam.rd.java.basic.taxiservice.config.DatabaseManager;
import com.epam.rd.java.basic.taxiservice.model.Street;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class StreetRepository {
    private static final String INSERT =
            "INSERT INTO street (type_id, title)\n" +
                    "   SELECT st.id, s.title\n" +
                    "       FROM (\n" +
                    "           VALUES\n" +
                    "               (?, ?)\n" +
                    "           ) AS s (type, title)\n" +
                    "JOIN street_type st ON s.type = st.title;";
    private static final String UPDATE = "";
    private static final String FIND_ALL =
            "SELECT s.id AS id, st.id AS type_id, st.title as type_title, s.id AS street_id, s.title AS street_title\n" +
            "FROM street s\n" +
            "JOIN street_type st ON st.id = s.type_id\n" +
            "ORDER BY s.title;";
    private static final String DELETE = "DELETE FROM street WHERE street.id = ?;";
    private static final String FIND_BY_ID =
            "SELECT s.id AS id, st.id AS type_id, st.title as type_title, s.id AS street_id, s.title AS street_title\n" +
                    "FROM street s\n" +
                    "JOIN street_type st ON st.id = s.type_id\n" +
                    "WHERE s.id = ?";
    private static final String FIND_BY_TITLE =
            "SELECT s.id AS id, st.id AS type_id, st.title as type_title, s.id AS street_id, s.title AS street_title\n" +
                    "FROM street s\n" +
                    "JOIN street_type st ON st.id = s.type_id\n" +
                    "WHERE s.title = ? AND st.title = ?";

    private final DatabaseManager databaseManager;

    public StreetRepository(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    public Optional<Street> findByTitle(String streetType, String title) {
        try (Connection connection = databaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_TITLE)) {
            preparedStatement.setString(1, streetType);
            preparedStatement.setString(1, title);
            ResultSet resultSet = preparedStatement.executeQuery();
            return mapToOne(resultSet);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return Optional.empty();
    }

    public Integer save(Street street) {
        try (Connection connection = databaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, street.getStreetType());
            preparedStatement.setString(2, street.getTitle());
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

    public void update(Street street) {
        try (Connection connection = databaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE)) {
            preparedStatement.setString(1, street.getStreetType());
            preparedStatement.setString(1, street.getTitle());
            preparedStatement.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void delete(Street street) {
        try (Connection connection = databaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE)) {
            preparedStatement.setInt(1, street.getId());
            preparedStatement.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public List<Street> findAll() {
        try (Connection connection = databaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            return mapToMany(resultSet);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return new ArrayList<>();
    }

    public Optional<Street> findById(Integer id) {
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

    private Optional<Street> mapToOne(ResultSet resultSet) throws SQLException {
        Street street = null;
        while (resultSet.next()) {
            street = getEntityFromResultSet(resultSet);
        }
        return Optional.ofNullable(street);
    }

    private List<Street> mapToMany(ResultSet resultSet) throws SQLException {
        List<Street> streets = new ArrayList<>();
        while (resultSet.next()) {
            Street street = getEntityFromResultSet(resultSet);
            streets.add(street);
        }
        return streets;
    }

    private Street getEntityFromResultSet(ResultSet resultSet) throws SQLException {
        Street street = new Street();
        street.setId(Integer.parseInt(resultSet.getString("street_id")));
        street.setStreetType(resultSet.getString("type_title"));
        street.setTitle(resultSet.getString("street_title"));
        return street;
    }
}
