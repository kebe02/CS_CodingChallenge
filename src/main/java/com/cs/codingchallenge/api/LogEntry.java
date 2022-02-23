package com.cs.codingchallenge.api;

import com.google.gson.annotations.SerializedName;

public class LogEntry {

    @SerializedName("id")
    private String id;

    @SerializedName("state")
    private LogEntryState state;

    @SerializedName("timestamp")
    private Long timestamp;

    @SerializedName("type")
    private String type;

    @SerializedName("host")
    private String host;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LogEntryState getState() {
        return state;
    }

    public void setState(LogEntryState state) {
        this.state = state;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    @Override
    public String toString() {
        return "com.cs.codingchallenge.api.LogEntry{" +
                "id='" + id + '\'' +
                ", state=" + state +
                ", timestamp=" + timestamp +
                ", type='" + type + '\'' +
                ", host='" + host + '\'' +
                '}';
    }
}
