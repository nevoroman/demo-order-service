package com.nevoroman.delivery.order.status

data class OrderStatusUpdatedEvent(
    override val orderId: Long,
    override val orderStatus: OrderStatus,
    override val userId: Long,
    val previousStatus: OrderStatus
) : OrderStatusEvent()