package com.cs.codingchallenge;

import com.cs.codingchallenge.api.LogEvent;
import org.junit.*;

import java.io.IOException;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class EventDatabaseTest {

    private static final String DATABASE_URL = "jdbc:hsqldb:mem:test;shutdown=true";
    private static final String DATABASE_USER = "SA";
    private static final String DATABASE_PASSWORD = "";

    private EventDatabase database;

    @Before
    public void setUp() throws SQLException {
        database = new EventDatabase(DATABASE_URL, DATABASE_USER, DATABASE_PASSWORD);
    }

    @After
    public void after() throws SQLException, IOException {
        database.rollback();
        database.close();
    }

    @Test
    public void writeLogEvent_shouldBePersisted() throws SQLException {

        var id = "test";
        var duration = 123L;
        var type = "type";
        var host = "host";
        var alert = true;

        database.writeEvent(new LogEvent(id, duration, type, host, alert));
        var fetched = database.readEvent(id);

        assertTrue(fetched.isPresent());
        assertEquals(id, fetched.get().getId().trim());
        assertEquals(duration, fetched.get().getDuration());
        assertEquals(type, fetched.get().getType().trim());
        assertEquals(host, fetched.get().getHost().trim());
        assertEquals(alert, fetched.get().isAlert());
    }

    @Test
    public void writeLogEventWithNulls_shouldBePersisted() throws SQLException {

        var id = "test";
        var duration = 123L;
        String type = null;
        String host = null;
        var alert = true;

        database.writeEvent(new LogEvent(id, duration, type, host, alert));
        var fetched = database.readEvent(id);

        assertTrue(fetched.isPresent());
        assertEquals(id, fetched.get().getId().trim());
        assertEquals(duration, fetched.get().getDuration());
        assertEquals(type, fetched.get().getType());
        assertEquals(host, fetched.get().getHost());
        assertEquals(alert, fetched.get().isAlert());
    }
}
