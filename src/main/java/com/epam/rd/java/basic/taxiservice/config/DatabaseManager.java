package com.epam.rd.java.basic.taxiservice.config;

import java.sql.Connection;
import java.sql.SQLException;

public interface DatabaseManager {
    Connection getConnection() throws SQLException;
    void closeConnection();
}
