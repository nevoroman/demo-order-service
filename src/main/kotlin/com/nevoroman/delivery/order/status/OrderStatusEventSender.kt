package com.nevoroman.delivery.order.status

import com.nevoroman.delivery.order.EventMessageConfigProperties
import com.nevoroman.delivery.order.findByStatus
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component

@Component
class OrderStatusEventSender(
    private val kafkaTemplate: KafkaTemplate<String, OrderStatusEvent>,
    private val eventMessageConfig: EventMessageConfigProperties
) {

    fun send(event: OrderStatusEvent) {
        val config = eventMessageConfig.findByStatus(event.orderStatus)
        kafkaTemplate.send(
            config.topic,
            event.orderId.toString(),
            event
        )
    }

}