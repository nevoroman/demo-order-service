package com.nevoroman.delivery.order

import com.nevoroman.delivery.order.status.OrderStatus
import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "app.kafka")
data class EventMessageConfigProperties(
    val statusEvents: List<StatusEventSpec>
)

fun EventMessageConfigProperties.findByStatus(status: OrderStatus): StatusEventSpec {
    return statusEvents.first { it.status == status }
}

data class StatusEventSpec(
    val status: OrderStatus,
    val topic: String
)