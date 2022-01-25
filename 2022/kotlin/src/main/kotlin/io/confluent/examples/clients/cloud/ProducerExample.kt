/**
 * Copyright 2020 Confluent Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
@file:JvmName("ProducerExample")

package io.confluent.examples.clients.cloud

import io.confluent.examples.clients.cloud.model.DataRecord
import io.confluent.examples.clients.cloud.util.loadConfig
import io.confluent.kafka.serializers.KafkaJsonSerializer
import org.apache.kafka.clients.admin.AdminClient
import org.apache.kafka.clients.admin.NewTopic
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerConfig.*
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.clients.producer.RecordMetadata
import org.apache.kafka.common.errors.TopicExistsException
import org.apache.kafka.common.serialization.StringSerializer
import java.util.*
import java.util.concurrent.ExecutionException
import kotlin.system.exitProcess

/*
Some scenarios you can test yourself:

1. ACK=0 vs ACK=all
    - Create topic with replicas > 1.
    - Send 1_000_000 messages to the created topic with ACK=0 -> Track time needed to finish publishing.
    - Send 1_000_000 messages to the created topic with ACK=all -> Track time needed to finish publishing.
    - Compare the time to understand how replication affect to the write throughput.

2. Round-robin partition on empty message key
    - Change the message key to empty string.
    - Send messages to the topic.
    - Check topic partitions latest offset to verify messages will be written in round-robin fashion.
    ! I couldn't verify this behavior, need further check in the impl of the Java client.

3. Same message key always go to the same topic partition
    - Set message key to fixed value.
    - Send messages to the topic.
    - Check topic partition offsets to verify all messages goes to the same partition.

4. Need flush on producer before exit
    - Set ACK=0.
    - Comment the `producer.flush()` LoC.
    - Run the code to see the order of log messages has been changed.
    => `10 messages were produced to topic` is being logged out before `Produced record to topic...`
    => producer.send() is async, it can batch/buffer many messages to send at once.
 */
fun main(args: Array<String>) {
    if (args.size != 2) {
        println("Please provide command line arguments: configPath topic")
        exitProcess(1)
    }

    // Load properties from file
    val props = loadConfig(args[0])

    // Create topic if needed
    val topic = args[1]
    val partitionNo = 3
    val replicationFactor = 3
    val messageNo = 10

    createTopic(topic, partitionNo, replicationFactor.toShort(), props)

    // Add additional properties.
    props[ACKS_CONFIG] = "all"
    props[KEY_SERIALIZER_CLASS_CONFIG] = StringSerializer::class.qualifiedName
    props[VALUE_SERIALIZER_CLASS_CONFIG] = KafkaJsonSerializer::class.qualifiedName

    // Produce sample data
    // `use` will execute block and close producer automatically
    KafkaProducer<String, DataRecord>(props).use { producer ->
        repeat(messageNo) { i ->
            val key = i.toString()
            val record = DataRecord(i.toLong())
            println("Producing record #$i: $key\t$record")

            producer.send(ProducerRecord(topic, key, record)) { m: RecordMetadata, e: Exception? ->
                when (e) {
                    // no exception, good to go!
                    null -> println("Produced record #$i to topic ${m.topic()} partition [${m.partition()}] @ offset ${m.offset()}")
                    // print stacktrace in case of exception
                    else -> e.printStackTrace()
                }
            }
        }

        producer.flush() // Force send
        println("$messageNo messages were produced to topic $topic")
    }
}

// Create topic in Confluent Cloud
fun createTopic(topic: String,
                partitions: Int,
                replication: Short,
                cloudConfig: Properties) {

    val newTopic = NewTopic(topic, partitions, replication)

    try {
        with(AdminClient.create(cloudConfig)) {
            createTopics(listOf(newTopic)).all().get()
        }
    } catch (e: ExecutionException) {
        if (e.cause !is TopicExistsException) throw e
    }
}
