package com.cs.codingchallenge;

import com.cs.codingchallenge.api.LogEntry;
import com.cs.codingchallenge.api.LogEntryState;
import com.cs.codingchallenge.api.LogEvent;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.sql.SQLException;

import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class LogEntryConsumerTest {

    private LogEntryConsumer logEntryConsumer;

    @Mock
    private EventDatabase mockdatabase;

    @Before
    public void setUp() {
        logEntryConsumer = new LogEntryConsumer(mockdatabase);
     }

    @Test
    public void startedThenFinished_shouldWriteToDatabase() throws SQLException {

        var id = "test";
        var now = System.currentTimeMillis();
        var type = "type";
        var host = "host";

        logEntryConsumer.accept(createLogEntry(id, LogEntryState.STARTED, type, host, now));
        logEntryConsumer.accept(createLogEntry(id, LogEntryState.FINISHED, type, host, now));

        verify(mockdatabase).writeEvent(new LogEvent(id, 0L, type, host, false));
    }

    @Test
    public void finishedThenStarted_shouldWriteToDatabase() throws SQLException {

        var id = "test";
        var now = System.currentTimeMillis();
        var type = "type";
        var host = "host";

        logEntryConsumer.accept(createLogEntry(id, LogEntryState.FINISHED, type, host, now));
        logEntryConsumer.accept(createLogEntry(id, LogEntryState.STARTED, type, host, now));

        verify(mockdatabase).writeEvent(new LogEvent(id, 0L, type, host, false));
    }

    @Test
    public void durationLessThan4Milliseconds_shouldNotAlert() throws SQLException {

        var id = "test";
        var now = System.currentTimeMillis();
        var type = "type";
        var host = "host";

        logEntryConsumer.accept(createLogEntry(id, LogEntryState.STARTED, type, host, now));
        logEntryConsumer.accept(createLogEntry(id, LogEntryState.FINISHED, type, host, now+3));

        verify(mockdatabase).writeEvent(new LogEvent(id, 3L, type, host, false));
    }

    @Test
    public void durationEqualTo4Milliseconds_shouldNotAlert() throws SQLException {

        var id = "test";
        var now = System.currentTimeMillis();
        var type = "type";
        var host = "host";

        logEntryConsumer.accept(createLogEntry(id, LogEntryState.STARTED, type, host, now));
        logEntryConsumer.accept(createLogEntry(id, LogEntryState.FINISHED, type, host, now+4));

        verify(mockdatabase).writeEvent(new LogEvent(id, 4L, type, host, true));
    }

    private static LogEntry createLogEntry(String id, LogEntryState state, String type, String host, long timestamp) {
        var entry = new LogEntry();
        entry.setId(id);
        entry.setState(state);
        entry.setType(type);
        entry.setHost(host);
        entry.setTimestamp(timestamp);
        return entry;
    }

}