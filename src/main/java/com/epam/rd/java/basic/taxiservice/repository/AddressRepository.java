package com.epam.rd.java.basic.taxiservice.repository;

import com.epam.rd.java.basic.taxiservice.config.DatabaseManager;
import com.epam.rd.java.basic.taxiservice.model.Address;
import com.epam.rd.java.basic.taxiservice.model.Street;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AddressRepository {
    private static final String INSERT =
            "INSERT INTO address (street_id, building_number)\n" +
                    "SELECT s.id, b.building\n" +
                    "FROM (\n" +
                    "    VALUES\n" +
                    "        (?, ?)\n" +
                    "    ) AS b(street, building)\n" +
                    "JOIN street s ON s.title = b.street;";
    private static final String UPDATE =
            "UPDATE car\n" +
                    "SET\n" +
                    "street_id = s.id,\n" +
                    "building_number = q.building_number,\n" +
                    "FROM (\n" +
                    "VALUES\n" +
                    "(?, ?)\n" +
                    ") AS q (street, building_number)\n" +
                    "JOIN street s ON s.title = q.street\n" +
                    "WHERE address.id = ?;";
    private static final String FIND_BY_NAME = "SELECT * FROM companies WHERE companies.company_name = ?;";
    private static final String FIND_ALL =
            "SELECT address.id AS id, st.title as street_type, s.title as street, \n" +
                    "address.building_number as building_number\n" +
                    "FROM address\n" +
                    "JOIN street s ON s.id = address.street_id\n" +
                    "JOIN street_type st ON st.id = s.type_id\n" +
                    "ORDER BY s.title;";
    private static final String FIND_ALL_WITH_OFFSET_AND_LIMIT =
            "SELECT address.id AS id, st.title as street_type, s.title as street, \n" +
                    "address.building_number as building_number\n" +
                    "FROM address\n" +
                    "JOIN street s ON s.id = address.street_id\n" +
                    "JOIN street_type st ON st.id = s.type_id\n" +
                    "ORDER BY s.title\n" +
                    "OFFSET ? LIMIT ?;";
    private static final String GET_TOTAL_NUMBER =
            "SELECT COUNT (*) as number\n" +
                    "FROM address\n" +
                    "JOIN street s ON s.id = address.street_id\n" +
                    "JOIN street_type st ON st.id = s.type_id;";
    private static final String DELETE = "DELETE FROM address WHERE address.id = ?;";
    private static final String FIND_BY_ID =
            "SELECT address.id AS id, s.id AS street_id, st.title as street_type, s.title as street,\n" +
                    "address.building_number as building_number\n" +
                    "FROM address\n" +
                    "JOIN street s ON s.id = address.street_id\n" +
                    "JOIN street_type st ON st.id = s.type_id\n" +
                    "WHERE address.id = ?;";
    private static final String FIND_BY_STREET =
            "SELECT address.id AS id, s.id AS street_id, st.title as street_type, s.title as street,\n" +
                    "address.building_number as building_number\n" +
                    "FROM address\n" +
                    "JOIN street s ON s.id = address.street_id\n" +
                    "JOIN street_type st ON st.id = s.type_id\n" +
                    "WHERE st.title = ? AND s.title = ?;";
    private static final String FIND_BY_STREET_WITH_OFFSET_AND_LIMIT =
            "SELECT address.id AS id, s.id AS street_id, st.title as street_type, s.title as street,\n" +
                    "address.building_number as building_number\n" +
                    "FROM address\n" +
                    "JOIN street s ON s.id = address.street_id\n" +
                    "JOIN street_type st ON st.id = s.type_id\n" +
                    "WHERE st.title = ? AND s.title = ?\n" +
                    "OFFSET ? LIMIT ?;";
    private static final String FIND_BY_STREET_AND_BUILDING =
            "SELECT address.id AS id, s.id AS street_id, st.title as street_type, s.title as street,\n" +
                    "address.building_number as building_number\n" +
                    "FROM address\n" +
                    "JOIN street s ON s.id = address.street_id\n" +
                    "JOIN street_type st ON st.id = s.type_id\n" +
                    "WHERE st.title = ? AND s.title = ? AND address.building_number = ?;";

    private final DatabaseManager databaseManager;

    public AddressRepository(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    public Optional<Address> findByStreetAndBuilding(String streetType, String street, String building) {
        try (Connection connection = databaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_STREET_AND_BUILDING)) {
            preparedStatement.setString(1, streetType);
            preparedStatement.setString(2, street);
            preparedStatement.setString(3, building);
            ResultSet resultSet = preparedStatement.executeQuery();
            return mapToOne(resultSet);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return Optional.empty();
    }

    public Integer save(Address address) {
        try (Connection connection = databaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, address.getStreet().getStreetType());
            preparedStatement.setString(2, address.getStreet().getTitle());
            preparedStatement.setString(3, address.getBuilding());
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

    public void update(Address address) {
        try (Connection connection = databaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE)) {
            preparedStatement.setString(1, address.getStreet().getStreetType());
            preparedStatement.setString(2, address.getStreet().getTitle());
            preparedStatement.setString(3, address.getBuilding());
            preparedStatement.setInt(6, address.getId());
            preparedStatement.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void delete(Address address) {
        try (Connection connection = databaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE)) {
            preparedStatement.setInt(1, address.getId());
            preparedStatement.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public List<Address> findAll() {
        try (Connection connection = databaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            return mapToMany(resultSet);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return new ArrayList<>();
    }

    public List<Address> findAllWithOffsetAndLimit(int offset, int limit) {
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

    public Optional<Address> findById(Integer id) {
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

    public List<Address> findByStreet(String streetType, String street) {
        try (Connection connection = databaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_STREET)) {
            preparedStatement.setString(1, streetType);
            preparedStatement.setString(2, street);
            ResultSet resultSet = preparedStatement.executeQuery();
            return mapToMany(resultSet);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return new ArrayList<>();
    }

    public List<Address> findByStreetWithOffsetAndLimit(String streetType, String street, int offset, int limit) {
        try (Connection connection = databaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_STREET_WITH_OFFSET_AND_LIMIT)) {
            preparedStatement.setString(1, streetType);
            preparedStatement.setString(2, street);
            preparedStatement.setInt(3, offset);
            preparedStatement.setInt(4, limit);
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

    private Optional<Address> mapToOne(ResultSet resultSet) throws SQLException {
        Address address = null;
        while (resultSet.next()) {
            address = getAddressFromResultSet(resultSet);
        }
        return Optional.ofNullable(address);
    }

    private List<Address> mapToMany(ResultSet resultSet) throws SQLException {
        List<Address> addresses = new ArrayList<>();
        while (resultSet.next()) {
            Address address = getAddressFromResultSet(resultSet);
            addresses.add(address);
        }
        return addresses;
    }

    private Address getAddressFromResultSet(ResultSet resultSet) throws SQLException {
        Address address = new Address();
        address.setId(resultSet.getInt("id"));
        Street street = new Street();
        street.setId(resultSet.getInt("street_id"));
        street.setStreetType(resultSet.getString("street_type"));
        street.setTitle(resultSet.getString("street"));
        address.setStreet(street);
        address.setBuilding(resultSet.getString("building_number"));
        return address;
    }
}
