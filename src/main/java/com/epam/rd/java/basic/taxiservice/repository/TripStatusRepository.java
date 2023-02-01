package com.epam.rd.java.basic.taxiservice.repository;

import com.epam.rd.java.basic.taxiservice.config.DatabaseManager;
import com.epam.rd.java.basic.taxiservice.model.TripStatus;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TripStatusRepository {
    private static final String INSERT =
            "INSERT INTO trip_status (title)\n" +
                    "VALUES\n" +
                    "(?);";
    private static final String UPDATE = ";";
    private static final String FIND_ALL =
            "SELECT id, title\n" +
                    "FROM trip_status\n" +
                    "ORDER BY title;";
    private static final String DELETE = "DELETE FROM trip_status WHERE id = ?;";
    private static final String FIND_BY_ID =
            "SELECT id, title\n" +
                    "FROM trip_status\n" +
                    "id = ?";
    private static final String FIND_BY_TITLE =
            "SELECT id, title\n" +
                    "FROM trip_status\n" +
                    "WHERE title = ?";

    private final DatabaseManager databaseManager;

    public TripStatusRepository(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    public Optional<TripStatus> findByTitle(String name) {
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

    public Integer save(TripStatus status) {
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

    public void update(TripStatus status) {
        try (Connection connection = databaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE)) {
            preparedStatement.setString(1, status.getTitle());
            preparedStatement.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void delete(TripStatus status) {
        try (Connection connection = databaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE)) {
            preparedStatement.setInt(1, status.getId());
            preparedStatement.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public List<TripStatus> findAll() {
        try (Connection connection = databaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            return mapToMany(resultSet);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return new ArrayList<>();
    }

    public Optional<TripStatus> findById(Integer id) {
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

    private Optional<TripStatus> mapToOne(ResultSet resultSet) throws SQLException {
        TripStatus status = null;
        while (resultSet.next()) {
            status = getEntityFromResultSet(resultSet);
        }
        return Optional.ofNullable(status);
    }

    private List<TripStatus> mapToMany(ResultSet resultSet) throws SQLException {
        List<TripStatus> tripStatuses = new ArrayList<>();
        while (resultSet.next()) {
            TripStatus status = getEntityFromResultSet(resultSet);
            tripStatuses.add(status);
        }
        return tripStatuses;
    }

    private TripStatus getEntityFromResultSet(ResultSet resultSet) throws SQLException {
        TripStatus status = new TripStatus();
        status.setId(Integer.parseInt(resultSet.getString("id")));
        status.setTitle(resultSet.getString("title"));
        return status;
    }
}
