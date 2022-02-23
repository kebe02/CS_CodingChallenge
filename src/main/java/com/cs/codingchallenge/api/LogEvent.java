package com.cs.codingchallenge.api;

import java.util.Objects;

public class LogEvent {

    private final String id;
    private final long duration;
    private final String type;
    private final String host;
    private final boolean alert;

    public LogEvent(String id, long duration, String type, String host, boolean alert) {
        this.id = id;
        this.duration = duration;
        this.type = type;
        this.host = host;
        this.alert = alert;
    }

    public String getId() {
        return id;
    }

    public long getDuration() {
        return duration;
    }

    public String getType() {
        return type;
    }

    public String getHost() {
        return host;
    }

    public boolean isAlert() {
        return alert;
    }

    @Override
    public String toString() {
        return "com.cs.codingchallenge.api.LogEvent{" +
                "id='" + id + '\'' +
                ", duration=" + duration +
                ", type='" + type + '\'' +
                ", host='" + host + '\'' +
                ", alert=" + alert +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LogEvent logEvent = (LogEvent) o;
        return duration == logEvent.duration && alert == logEvent.alert && id.equals(logEvent.id) && Objects.equals(type, logEvent.type) && Objects.equals(host, logEvent.host);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, duration, type, host, alert);
    }
}
