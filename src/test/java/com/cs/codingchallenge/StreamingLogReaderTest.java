package com.cs.codingchallenge;

import com.cs.codingchallenge.api.LogEntry;
import com.cs.codingchallenge.api.LogEntryState;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

public class StreamingLogReaderTest {

    private StreamingLogReader logReader;
    private List<LogEntry> values;

    @Before
    public void setUp() {
        values = new ArrayList<>();
        logReader = new StreamingLogReader<>(LogEntry.class, v -> values.add(v));
    }

    @Test
    public void readsAllRecords() throws FileNotFoundException {

        var file = new File("src/main/resources/logfile.txt");
        var stream = new FileInputStream(file);

        logReader.stream(stream);
        assertThat(values)
                .extracting(
                        LogEntry::getId,
                        LogEntry::getState,
                        LogEntry::getType,
                        LogEntry::getHost,
                        LogEntry::getTimestamp)
                .containsExactlyInAnyOrder(
                        tuple("scsmbstgra", LogEntryState.STARTED, "APPLICATION_LOG", "12345", 1491377495213L),
                        tuple("scsmbstgrb", LogEntryState.STARTED, null, null, 1491377495213L),
                        tuple("scsmbstgra", LogEntryState.FINISHED, "APPLICATION_LOG", "12345", 1491377495216L),
                        tuple("scsmbstgrb", LogEntryState.FINISHED, null, null, 1491377495216L)
                );
    }

}