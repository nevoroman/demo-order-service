package com.nevoroman.delivery.order.status

abstract class OrderStatusEvent {
    abstract val orderId: Long
    abstract val orderStatus: OrderStatus
    abstract val userId: Long
}