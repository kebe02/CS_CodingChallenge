package com.cs.codingchallenge;

import com.cs.codingchallenge.api.LogEntry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;

public class Main {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    private static final String DATABASE_URL = "jdbc:hsqldb:file:proddb;shutdown=true";
    private static final String DATABASE_USER = "SA";
    private static final String DATABASE_PASSWORD = "";

    public static void main(String[] args) {

        if(args.length != 1) {
            logger.error("Usage: java -jar ./target/CodingAssignment-1.0-SNAPSHOT-jar-with-dependencies.jar <FILE>");
        }

        var file = args[0];
        logger.info("Processsing file: {}", file);
        try(var database = new EventDatabase(DATABASE_URL, DATABASE_USER, DATABASE_PASSWORD);) {
            var consumer = new LogEntryConsumer(database);
            var reader = new StreamingLogReader<>(LogEntry.class, consumer);
            reader.stream(new FileInputStream(file));
        }
        catch (FileNotFoundException e) {
            logger.error("The specified file {} was not found", file);
        }
        catch (SQLException e) {
            logger.error("Failed to initialize database. Reason {}", e.getMessage());
        } catch (IOException e) {
            logger.error("An unexpected exception occurred. Reason {}", e.getMessage());
        }
    }
}
