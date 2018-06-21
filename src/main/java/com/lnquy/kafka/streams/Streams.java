package com.lnquy.kafka.streams;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Produced;

import java.util.Arrays;
import java.util.Properties;

public class Streams {
    private static final String KAFKA_BOOTSTRAP_SERVERS = "127.0.0.1:9092";
    private static final String KAFKA_IN_TOPIC = "stream-in";
    private static final String KAFKA_OUT_TOPIC = "stream-out";

    public static void main(final String[] args) throws Exception {
        Properties config = new Properties();
        config.put(StreamsConfig.APPLICATION_ID_CONFIG, "word-count-application");
        config.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, KAFKA_BOOTSTRAP_SERVERS);
        config.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        config.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());

        StreamsBuilder builder = new StreamsBuilder();
        KStream<String, String> textLines = builder.stream(KAFKA_IN_TOPIC);
        KTable<String, Long> wordCounts = textLines
                .peek((k, v) -> System.out.println("Received line: " + v))
                .flatMapValues(line -> Arrays.asList(line.toLowerCase().split("\\W+")))
                .groupBy((key, word) -> word)
                .count();

        wordCounts
                .toStream()
                .to(KAFKA_OUT_TOPIC, Produced.with(Serdes.String(), Serdes.Long()));

        KafkaStreams streams = new org.apache.kafka.streams.KafkaStreams(builder.build(), config);
        System.out.println("Stream started");
        streams.start();
    }
}
