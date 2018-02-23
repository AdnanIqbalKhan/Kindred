package com.kindred.kindred;

/**
 * Created by Adnan Iqbal Khan on 18-Feb-18.
 */

public class Messages {
    private String message;
    private String seen;
    private long timestamp;
    private String from;
    private String to;

    public Messages() {
    }

    public Messages(String message, String seen, long timestamp, String from, String to) {
        this.message = message;
        this.seen = seen;
        this.timestamp = timestamp;
        this.from = from;
        this.to = to;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSeen() {
        return seen;
    }

    public void setSeen(String seen) {
        this.seen = seen;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }
}
