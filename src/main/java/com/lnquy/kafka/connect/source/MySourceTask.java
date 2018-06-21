package com.lnquy.kafka.connect.source;

import org.apache.kafka.connect.source.SourceRecord;
import org.apache.kafka.connect.source.SourceTask;

import java.util.List;
import java.util.Map;

public class MySourceTask extends SourceTask {
    @Override
    public String version() {
        return null;
    }

    @Override
    public void start(Map<String, String> props) {
        // Do initial setup for the task here.
        // The props map contains all task configurations handled at MySinkConnector.taskConfigs()
    }

    @Override
    public List<SourceRecord> poll() throws InterruptedException {
        // Do your actual business here.
        // E.g: Read log/record from text files or console then build the List<SourceRecord> and return it back to KafkaConnect.
        // KafkaConnect will call this poll() method immediately when received the returning from previous poll() call,
        // so you should have a custom delay/sleep mechanism here to prevent the tight loop will drain all CPU resource.
        return null;
    }

    @Override
    public void stop() {
        // Handle graceful shutdown for task here
    }
}
