topic ?= 'test-topic'

.PHONY: all build
.DEFAULT_GOAL: build

build:
	./gradlew clean build

run-producer: build
	./gradlew runApp -PmainClass="io.confluent.examples.clients.cloud.ProducerExample" \
		-PconfigPath="kafka.config" \
		-Ptopic=$(topic)

run-consumer: build
	./gradlew runApp -PmainClass="io.confluent.examples.clients.cloud.ConsumerExample" \
		-PconfigPath="kafka.config" \
		-Ptopic=$(topic)

run-stream: build
	./gradlew runApp -PmainClass="io.confluent.examples.clients.cloud.StreamsExample" \
		-PconfigPath="kafka.config" \
		-Ptopic=$(topic)
