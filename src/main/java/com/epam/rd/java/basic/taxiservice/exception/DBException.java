package com.epam.rd.java.basic.taxiservice.exception;

import java.sql.SQLException;

public class DBException extends SQLException {

    public DBException(String reason, Throwable cause) {
        super(reason, cause);
    }
}
