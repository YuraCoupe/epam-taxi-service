package com.epam.rd.java.basic.taxiservice.repository;

import com.epam.rd.java.basic.taxiservice.config.DatabaseManager;
import com.epam.rd.java.basic.taxiservice.model.Car.CarStatus;
import com.epam.rd.java.basic.taxiservice.model.Role;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RoleRepository {
    private static final String INSERT =
            "INSERT INTO role (title)\n" +
                    "VALUES\n" +
                    "(?);";
    private static final String UPDATE = "UPDATE role SET title = ? WHERE id = ?;";
    private static final String FIND_ALL =
            "SELECT id, title\n" +
                    "FROM role\n" +
                    "ORDER BY title;";
    private static final String DELETE = "DELETE FROM role WHERE id = ?;";
    private static final String FIND_BY_ID =
            "SELECT id, title\n" +
                    "FROM role\n" +
                    "WHERE id = ?";
    private static final String FIND_BY_TITLE =
            "SELECT id, title\n" +
                    "FROM role\n" +
                    "WHERE title = ?";
    private static final String GET_CLIENT_ROLE =
            "SELECT id, title FROM role WHERE title = 'ROLE_CLIENT';";

    private final DatabaseManager databaseManager;

    public RoleRepository(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    public Optional<Role> findByTitle(String name) {
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

    public Integer save(Role role) {
        try (Connection connection = databaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, role.getTitle());
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

    public void update(Role role) {
        try (Connection connection = databaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE)) {
            preparedStatement.setString(1, role.getTitle());
            preparedStatement.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void delete(Role role) {
        try (Connection connection = databaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE)) {
            preparedStatement.setInt(1, role.getId());
            preparedStatement.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public List<Role> findAll() {
        try (Connection connection = databaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            return mapToMany(resultSet);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return new ArrayList<>();
    }

    public Optional<Role> findById(Integer id) {
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

    public Optional<Role> getClientRole() {
        try (Connection connection = databaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_CLIENT_ROLE)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            return mapToOne(resultSet);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return Optional.empty();
    }

    private Optional<Role> mapToOne(ResultSet resultSet) throws SQLException {
        Role role = null;
        while (resultSet.next()) {
            role = getRoleFromResultSet(resultSet);
        }
        return Optional.ofNullable(role);
    }

    private List<Role> mapToMany(ResultSet resultSet) throws SQLException {
        List<Role> roles = new ArrayList<>();
        while (resultSet.next()) {
            Role role = getRoleFromResultSet(resultSet);
            roles.add(role);
        }
        return roles;
    }

    private Role getRoleFromResultSet(ResultSet resultSet) throws SQLException {
        Role role = new Role();
        role.setId(Integer.parseInt(resultSet.getString("id")));
        role.setTitle(resultSet.getString("title"));
        return role;
    }
}
