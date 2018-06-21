package com.lnquy.kafka.bare;

import java.util.concurrent.ExecutionException;

public class KafkaBareClient {
    public static final String KAFKA_BOOTSTRAP_SERVERS = "127.0.0.1:9092";
    public static final String KAFKA_TOPIC = "bare-topic";

    private static final String PRODUCER = "producer";
    private static final String CONSUMER = "consumer";

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        String runMode = args.length > 0 ? args[0] : PRODUCER;

        switch (runMode.toLowerCase()) {
            case PRODUCER:
                (new Producer()).pushToKafka();
                break;
            case CONSUMER:
                String consumerGroupId = args.length > 1 ? args[1] : "";
                (new Consumer(consumerGroupId)).pollFromKafka();
                break;
            default:
                System.err.println("Invalid run mode: " + runMode);
                System.exit(1);
        }
        System.out.println("Application end.");
    }
}
