package com.lnquy.kafka.connect.sink;

import org.apache.kafka.connect.sink.SinkRecord;
import org.apache.kafka.connect.sink.SinkTask;

import java.util.Collection;
import java.util.Map;

public class MySinkTask extends SinkTask {
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
    public void put(Collection<SinkRecord> records) {
        // Do your actual business here.
        // E.g: Write all records to DB or HDFS...
        //
        // Kafka Connect will automatically poll records from Kafka topic then deserialize and pass it to your connector
        // via this method.
        // No delay/sleep mechanism should be placed here (differ from SourceTask); instead you should impl this method
        // as asynchronous/non-blocking to prevent any blocking on KafkaConnect.
        // E.g: You can impl your own in-memory buffer here, put all records to buffer then return immediately.
        // Another batch process can run on background to handle the data in your buffer.
        //
        // IMPORTANT: I'm not saying you MUST impl this method as async/nio, but you SHOULD.
        // Async can provide better I/O (since you can make batch write, etc) and some other benefits, but of course,
        // it comes with the cost of complexity and potential of data-loss...
        // You must UNDERSTAND your business to decide which model (async vs sync) should be used here!
    }

    @Override
    public void stop() {
        // Handle graceful shutdown for task here
    }
}
