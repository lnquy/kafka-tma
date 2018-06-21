package com.lnquy.kafka.bare;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.util.Collections;
import java.util.Properties;

public class Consumer {
    private KafkaConsumer cons;
    private String topic;

    public Consumer(String groupId) {
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, KafkaBareClient.KAFKA_BOOTSTRAP_SERVERS);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, (groupId == null || "".equals(groupId)) ? "BareConsumerGroup" : groupId);
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true");
        props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "1000");
        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "30000");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        cons = new KafkaConsumer<>(props);
        this.topic = KafkaBareClient.KAFKA_TOPIC;
    }

    @SuppressWarnings("unchecked")
    public void pollFromKafka() {
        cons.subscribe(Collections.singletonList(this.topic));
        System.out.println("Consumer started");
        while (true) {
            ConsumerRecords<String, String> records = cons.poll(1000);
            for (ConsumerRecord<String, String> record : records) {
                System.out.println("Received " + record.key() + ": " + record.value() + ". Offset: " + record.offset());
            }
        }
    }
}
