# Kafka Introduction

> Demo code for Kafka Introduction session at TMA on June 21st 2018.  

```
@author: Quy Le <lnquy@tma.com.vn, lnquy.it@gmail.com>
@lastEdited: June 21th 2018
@version: 0.0.2
```

Source code is written in Java and built with Gradle. 

```
$ cd /path/to/src
$ gradle init   // Install dependencies
$ gradle <taskName>   // Run specific demo
// Available demos are:
// - kafkaBareProducer
// - kafkaBareConsumer
// - kafkaBareConsumer2
// - kafkaStreams

// Kafka Connect code is not runable.
// Kafka Security (SSL + SASL/Kerberos) package contains no code but some notes.
```

### Notes

##### KafkaBareClient (Producer + Consumer)

- Configure `KAFKA_BOOTSTRAP_SERVERS` and `KAFKA_TOPIC` in `KafkaBareClient.java` file to match your Kafka system.
  `KAFKA_BOOTSTRAP_SERVERS` is a list of addresses to Kafka brokers, separated by comma.
  `KAFKA_TOPIC` is your topic name.
- Producer send a message to topic every second. Consumer consumes messages from Kafka topic and prints it to stdout.
- Start `kafkaBareProducer` first, then start one of these cases below to see the difference behaviour of consumer group.  
  - Broadcast model: Run a `kafkaBareConsumer` process and a `kafkaBareConsumer2` process.
  - LoadBalancing model: Run 2 `kafkaBareConsumer` processes.

##### KafkaConnect

- Code for this part is not runnable, instead you can use it as reference. I also put some comments on important parts of code here, just take a look.

##### KafkaStreams

- Configure `KAFKA_BOOTSTRAP_SERVERS`, `KAFKA_IN_TOPIC` and `KAFKA_OUT_TOPIC` to match your system.
  `KAFKA_IN_TOPIC` is the topic name where stream polls source messages from.
  `KAFKA_OUT_TOPIC` is the topic name where stream will send processed data to.
- Stream automatically polls messages from `KAFKA_IN_TOPIC`, deserializes to String, splits message's value to words (separated by whitespace(s)), count number of appearances of words in stream then send those count values to `KAFKA_OUT_TOPIC`.

##### KafkaSecurity

- Please read the `README.md` file inside `security` package for more details.

