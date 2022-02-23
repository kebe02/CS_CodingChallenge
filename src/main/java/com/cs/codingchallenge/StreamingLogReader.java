package com.cs.codingchallenge;

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class StreamingLogReader<T> {

    private final Logger logger = LoggerFactory.getLogger(StreamingLogReader.class);

    private final Class<T> recordType;
    private final Consumer<T> recordConsumer;
    private final Gson gson;

    public StreamingLogReader(Class<T> recordType, Consumer<T> recordConsumer) {
        this.recordType = recordType;
        this.recordConsumer = recordConsumer;
        this.gson = new Gson();
    }

    public void stream(InputStream inputStream) {

        final AtomicInteger currentLine = new AtomicInteger(0);
        try (Stream<String> lines = new BufferedReader(new InputStreamReader(inputStream)).lines()) {
            lines
                .map(line -> {
                    try {
                        currentLine.incrementAndGet();
                        return gson.fromJson(line, recordType);
                    } catch (Exception e) {
                        logger.error("Failed to parse line {}. Value: {}. Reason {}.", currentLine.get(), line, e.getMessage());
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .forEach(recordConsumer);
        }
    }
}
