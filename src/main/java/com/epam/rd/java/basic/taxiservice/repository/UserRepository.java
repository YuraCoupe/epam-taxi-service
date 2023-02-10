package com.epam.rd.java.basic.taxiservice.repository;

import com.epam.rd.java.basic.taxiservice.config.DatabaseManager;
import com.epam.rd.java.basic.taxiservice.model.User;
import com.epam.rd.java.basic.taxiservice.model.Role;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserRepository implements BaseRepository {
    private static final String INSERT =
            "INSERT INTO person (phone_number, password, first_name, last_name, role_id)\n" +
                    "SELECT phone_number, password, first_name, last_name, r.id\n" +
                    "FROM (\n" +
                    "VALUES\n" +
                    "(?, ?, ?, ?, ?)\n" +
                    ") AS p (phone_number, password, first_name, last_name, role)\n" +
                    "JOIN role r ON r.title = p.role;";
    private static final String UPDATE =
            "UPDATE person\n" +
            "SET \n" +
                "phone_number = ?,\n" +
                "password = ?,\n" +
                "first_name = ?,\n" +
                "last_name = ?,\n" +
                "role_id = sq.id\n" +
                "FROM (SELECT id FROM role r where r.title = ?) AS sq\n" +
                "WHERE person.id = ?;";
    private static final String FIND_ALL =
            "SELECT person.id AS id, phone_number, first_name, last_name, password, r.id AS role_id, r.title AS role_title\n" +
                    "FROM person\n" +
                    "JOIN role r ON r.id = person.role_id\n" +
                    "ORDER BY phone_number;";
    private static final String FIND_ALL_CLIENTS =
            "SELECT person.id AS id, phone_number, first_name, last_name, password, r.id AS role_id, r.title AS role_title\n" +
                    "FROM person\n" +
                    "JOIN role r ON r.id = person.role_id\n" +
                    "WHERE r.title = 'ROLE_CLIENT'\n" +
                    "ORDER BY phone_number;";
    private static final String FIND_ALL_WITH_OFFSET_AND_LIMIT =
            "SELECT person.id AS id, phone_number, first_name, last_name, password, r.id AS role_id, r.title AS role_title\n" +
                    "FROM person\n" +
                    "JOIN role r ON r.id = person.role_id\n" +
                    "ORDER BY phone_number\n" +
                    "OFFSET ? LIMIT ?;";
    private static final String GET_TOTAL_NUMBER =
            "SELECT COUNT (*) as number\n" +
                    "FROM person\n" +
                    "JOIN role r ON r.id = person.role_id;";
    private static final String DELETE =
            "DELETE FROM person WHERE id = ?;";
    private static final String FIND_BY_ID =
            "SELECT person.id AS id, phone_number, first_name, last_name, password, r.id AS role_id, r.title AS role_title\n" +
                    "FROM person\n" +
                    "JOIN role r ON r.id = person.role_id\n" +
                    "WHERE person.id = ?;";
    private static final String FIND_BY_PHONE_NUMBER =
            "SELECT person.id AS id, phone_number, first_name, last_name, password, r.id AS role_id, r.title AS role_title\n" +
                    "FROM person\n" +
                    "JOIN role r ON r.id = person.role_id\n" +
                    "WHERE person.phone_number = ?;";
    private static final String FIND_FREE_DRIVERS =
            "SELECT p.id AS id, phone_number, first_name, last_name, password, r.id AS role_id, r.title AS role_title\n" +
                    "FROM person p\n" +
                    "JOIN role r on r.id = p.role_id\n" +
                    "LEFT JOIN car c on c.driver_id = p.id\n" +
                    "WHERE r.title = 'ROLE_DRIVER' AND c.id IS NULL;";
    private static final String FIND_MONEY_SPENT_BY_USER_ID =
            "SELECT SUM(price) AS total_spent\n" +
                    "FROM trip\n" +
                    "WHERE person_id = ?;";
    private static final String FIND_DISCOUNT_RATE_BY_MONEY_SPENT =
            "SELECT id, MAX(money_spent), discount\n" +
                    "FROM discount_rate\n" +
                    "WHERE money_spent = (SELECT MAX(money_spent) FROM discount_rate WHERE money_spent <= ?)\n" +
                    "GROUP BY id;";

    private final DatabaseManager databaseManager;

    public UserRepository(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    public Optional<User> findByPhoneNumner(String phoneNumber) {
        try (Connection connection = databaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_PHONE_NUMBER)) {
            preparedStatement.setString(1, phoneNumber);
            ResultSet resultSet = preparedStatement.executeQuery();
            return mapToOne(resultSet);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return Optional.empty();
    }

    public Integer save(User user) {
        try (Connection connection = databaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, user.getPhoneNumber());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setString(3, user.getFirstName());
            preparedStatement.setString(4, user.getLastName());
            preparedStatement.setString(5, user.getRole().getTitle());
            preparedStatement.execute();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            resultSet.next();
            return resultSet.getInt(1);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    public void update(User user) {
        try (Connection connection = databaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE)) {
            preparedStatement.setString(1, user.getPhoneNumber());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setString(3, user.getFirstName());
            preparedStatement.setString(4, user.getLastName());
            preparedStatement.setString(5, user.getRole().getTitle());
            preparedStatement.setInt(6, user.getId());
            preparedStatement.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void delete(User user) {
        try (Connection connection = databaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE)) {
            preparedStatement.setInt(1, user.getId());
            preparedStatement.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public List<User> findAll() {
        try (Connection connection = databaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            return mapToMany(resultSet);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return new ArrayList<>();
    }

    public List<User> findAllWithOffsetAndLimit(int offset, int limit) {
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

    public Optional<User> findById(Integer id) {
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

    public Optional<User> findByPhoneNumber(String phoneNumber) {
        try (Connection connection = databaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_PHONE_NUMBER)) {
            preparedStatement.setString(1, phoneNumber);
            ResultSet resultSet = preparedStatement.executeQuery();
            return mapToOne(resultSet);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return Optional.empty();
    }

    public List<User> findFreeDrivers() {
        try (Connection connection = databaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_FREE_DRIVERS)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            return mapToMany(resultSet);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return new ArrayList<>();
    }

    public List<User> findAllClients() {
        try (Connection connection = databaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL_CLIENTS)) {
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

    private Optional<User> mapToOne(ResultSet resultSet) throws SQLException {
        User user = null;
        while (resultSet.next()) {
            user = getEntityFromResultSet(resultSet);
        }
        return Optional.ofNullable(user);
    }

    private List<User> mapToMany(ResultSet resultSet) throws SQLException {
        List<User> users = new ArrayList<>();
        while (resultSet.next()) {
            User user = getEntityFromResultSet(resultSet);
            users.add(user);
        }
        return users;
    }

    private User getEntityFromResultSet(ResultSet resultSet) throws SQLException {
        User user = new User();
        Role role = new Role();
        user.setId(resultSet.getInt("id"));
        role.setId(Integer.parseInt(resultSet.getString("role_id")));
        role.setTitle(resultSet.getString("role_title"));
        user.setRole(role);
        user.setPhoneNumber(resultSet.getString("phone_number"));
        user.setFirstName(resultSet.getString("first_name"));
        user.setLastName(resultSet.getString("last_name"));
        user.setPassword(resultSet.getString("password"));
        user.setSumSpent(getMoneySpentByUserId(user.getId()));
        user.setDiscountRate(getDiscontRateByMoneySpent(user.getSumSpent()));
        return user;
    }

    private BigDecimal getMoneySpentByUserId(Integer userId) {
        try (Connection connection = databaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_MONEY_SPENT_BY_USER_ID)) {
            preparedStatement.setLong(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();
            BigDecimal moneySpent = BigDecimal.valueOf(0.0);
            while (resultSet.next()) {
                moneySpent = resultSet.getBigDecimal("total_spent");
            }
            return moneySpent != null ? moneySpent : BigDecimal.valueOf(0.0);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return BigDecimal.valueOf(0.0);
    }

    private Integer getDiscontRateByMoneySpent(BigDecimal moneySpent) {
        try (Connection connection = databaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_DISCOUNT_RATE_BY_MONEY_SPENT)) {
            preparedStatement.setBigDecimal(1, moneySpent);
            ResultSet resultSet = preparedStatement.executeQuery();
            int discountRate = 0;
            while (resultSet.next()) {
                discountRate = resultSet.getInt("discount");
            }
            return discountRate;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return 0;
    }
}
