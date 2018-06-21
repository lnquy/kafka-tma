package com.lnquy.kafka.bare;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;

import java.time.LocalDateTime;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class Producer {
    private KafkaProducer prod;
    private String topic;

    public Producer() {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, KafkaBareClient.KAFKA_BOOTSTRAP_SERVERS);
        props.put(ProducerConfig.CLIENT_ID_CONFIG, "BareKafkaProducer");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        prod = new KafkaProducer<>(props);
        this.topic = KafkaBareClient.KAFKA_TOPIC;
    }

    @SuppressWarnings("unchecked")
    public void pushToKafka() throws InterruptedException, ExecutionException {
        System.out.println("Producer started");
        int id = 0;
        while (true) {
            String time = LocalDateTime.now().toString();
            Future<RecordMetadata> meta = prod.send(new ProducerRecord(topic, "msg#" + id, time));
            // meta.get(); // Uncomment to force synchronous send instead of async
            System.out.println("Sent msg#" + id++ + ": " + time);
            Thread.sleep(1000L);
        }
    }
}
