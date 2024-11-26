package com.nevoroman.delivery.order

import com.nevoroman.delivery.order.status.OrderStatus
import com.nevoroman.delivery.order.status.OrderStatusEvent

data class OrderCreatedEvent(
    override val orderId: Long,
    override val userId: Long,
    val origin: String,
    val destination: String,
    override val orderStatus: OrderStatus = OrderStatus.CREATED
): OrderStatusEvent()