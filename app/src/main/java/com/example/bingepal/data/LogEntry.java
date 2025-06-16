package com.example.bingepal.data;

public class LogEntry {
    public String source;
    public String source_id;
    public String type;
    public String title;

    public LogEntry(String source, String source_id, String type, String title) {
        this.source = source;
        this.source_id = source_id;
        this.type = type;
        this.title = title;
    }
}
