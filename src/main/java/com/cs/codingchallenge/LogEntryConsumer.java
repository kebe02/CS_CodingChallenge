package com.cs.codingchallenge;

import com.cs.codingchallenge.api.LogEntry;
import com.cs.codingchallenge.api.LogEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class LogEntryConsumer implements Consumer<LogEntry> {

    private static final Logger logger = LoggerFactory.getLogger(LogEntryConsumer.class);

    private final Map<String, LogEntry> started;
    private final Map<String, LogEntry> finished;

    private final EventDatabase database;

    public LogEntryConsumer(EventDatabase database) {
        this.started = new HashMap<>();
        this.finished = new HashMap<>();
        this.database = database;
    }

    @Override
    public void accept(LogEntry logEntry) {

        logger.debug("Processing entry: {}", logEntry);
        switch(logEntry.getState()) {
            case STARTED:
                onStarted(logEntry);
                break;
            case FINISHED:
                onFinished(logEntry);
                break;
        }
    }

    private void onStarted(LogEntry logEntry) {

        var id = logEntry.getId();
        if(finished.containsKey(id)) {
            var matched = finished.remove(id);
            logger.debug("Found matching entry: {}", (Object) matched);
            save(id, logEntry, matched);
        }
        else {
            started.put(id, logEntry);
        }
    }

    private void onFinished(LogEntry logEntry) {

        var id = logEntry.getId();
        if(started.containsKey(id)) {
            var matched = started.remove(id);
            logger.debug("Found matching entry: {}", (Object) matched);
            save(id, matched, logEntry);
        }
        else {
            finished.put(id, logEntry);
        }
    }

    private void save(String id, LogEntry started, LogEntry finished) {

        var duration = finished.getTimestamp() - started.getTimestamp();
        var alert = duration >= 4;
        var type = started.getType();
        var host = finished.getHost();
        try {
            var event = new LogEvent(id, duration, type, host, alert);
            database.writeEvent(event);
            database.commit();
            logger.debug("Wrote event: {}", event);
        } catch (SQLException e) {
            logger.error("Failed to write event. Reason: {}", e.getMessage());
        }
    }
}
