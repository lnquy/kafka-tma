package com.lnquy.kafka.connect.sink;

import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.connect.connector.Task;
import org.apache.kafka.connect.sink.SinkConnector;

import java.util.List;
import java.util.Map;

public class MySinkConnector extends SinkConnector {

    @Override
    public String version() {
        return null;
    }

    @Override
    public void start(Map<String, String> props) {
        // Do your initial setup for the whole sink connector here
    }

    @Override
    public Class<? extends Task> taskClass() {
        return MySinkTask.class; // Return the actual task class for this connector
    }

    @Override
    public List<Map<String, String>> taskConfigs(int maxTasks) {
        // Do initial setup for task configuration
        // Returned result from this method will be passed to child tasks at beginning of each task spin up
        return null;
    }

    @Override
    public void stop() {
        // Handle graceful shutdown for connector if need here
    }

    @Override
    public ConfigDef config() {
        return MySinkConfig.conf(); // Parse custom config
    }
}
