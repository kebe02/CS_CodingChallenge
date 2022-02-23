package com.cs.codingchallenge;

import com.cs.codingchallenge.api.LogEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.IOException;
import java.sql.*;
import java.util.Optional;

public class EventDatabase implements Closeable {

    private static final Logger logger = LoggerFactory.getLogger(EventDatabase.class);

    private Connection connection;
    private PreparedStatement insertEvent;
    private PreparedStatement selectEvent;

    public EventDatabase(String url, String user, String password) throws SQLException {
        connect(url, user, password);
        createTablesIfNecessary();
        prepareStatements();
    }

    public void writeEvent(LogEvent event) throws SQLException {

        insertEvent.setString(1, event.getId());
        insertEvent.setLong(2, event.getDuration());
        insertEvent.setString(3, event.getType());
        insertEvent.setString(4, event.getHost());
        insertEvent.setBoolean(5, event.isAlert());
        insertEvent.executeUpdate();
    }

    public Optional<LogEvent> readEvent(String id) throws SQLException {

        selectEvent.setString(1, id);
        ResultSet result = selectEvent.executeQuery();
        if(result.next()) {
            return Optional.of(new LogEvent(
                    result.getString(1),
                    result.getLong(2),
                    result.getString(3),
                    result.getString(4),
                    result.getBoolean(5)
            ));
        }

        return Optional.empty();
    }

    public void commit() throws SQLException {
        connection.commit();
    }

    public void rollback() throws SQLException {
        connection.rollback();
    }

    private void connect(String url, String user, String password) throws SQLException {

        logger.info("Connecting to database '{}'", url);
        connection = DriverManager.getConnection(url, user, password);
        connection.setAutoCommit(false);
    }

    private void createTablesIfNecessary() throws SQLException {

        String createEventTable = "CREATE TABLE IF NOT EXISTS EVENTS("
                + "ID CHAR(10) NOT NULL, "
                + "DURATION BIGINT NOT NULL, "
                + "TYPE VARCHAR(255) NULL, "
                + "HOST VARCHAR(255) NULL, "
                + "ALERT BOOLEAN NOT NULL, "
                + "PRIMARY KEY (ID) )";

        var statement = connection.createStatement();
        statement.execute(createEventTable);
        statement.close();
        connection.commit();
    }

    private void prepareStatements() throws SQLException {
        insertEvent = connection.prepareStatement("INSERT INTO EVENTS VALUES ?,?,?,?,?");
        selectEvent = connection.prepareStatement("SELECT ID, DURATION, TYPE, HOST, ALERT FROM EVENTS WHERE ID = ?");
    }

    @Override
    public void close() throws IOException {
        Statement statement = null;
        try {
            statement = connection.createStatement();
            statement.execute("SHUTDOWN");
            statement.close();
            connection.commit();
            connection.close();
        } catch (SQLException e) {
            logger.error("Failed to close database resources. Reason: {}", e.getMessage());
        }
    }
}
