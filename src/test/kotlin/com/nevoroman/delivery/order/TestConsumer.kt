package com.nevoroman.delivery.order

import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.common.serialization.StringDeserializer
import org.springframework.boot.autoconfigure.kafka.KafkaProperties
import org.springframework.kafka.core.DefaultKafkaConsumerFactory
import org.springframework.kafka.support.serializer.JsonDeserializer
import org.springframework.kafka.test.utils.KafkaTestUtils
import org.springframework.stereotype.Component
import java.time.Duration

open class TestConsumer<T>(
    private val topic: String,
    private val kafkaProperties: KafkaProperties
) {

    fun takeOne(delay: Long = 5): ConsumerRecord<String, T>? {
        val consumerProperties = kafkaProperties.buildConsumerProperties(null)
        consumerProperties[ConsumerConfig.AUTO_OFFSET_RESET_CONFIG] = "earliest"
        val consumer = DefaultKafkaConsumerFactory<String, T>(consumerProperties, StringDeserializer(), JsonDeserializer<T>()).createConsumer()
        consumer.subscribe(listOf(topic))
        return KafkaTestUtils.getSingleRecord(consumer, topic, Duration.ofSeconds(delay))
    }

}

@Component
class OrderCreatedEventConsumer(kafkaProperties: KafkaProperties): TestConsumer<OrderCreatedEvent>(
    "order_created",
    kafkaProperties
)