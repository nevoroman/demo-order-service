package com.nevoroman.delivery.order

import com.nevoroman.delivery.order.model.Order
import com.nevoroman.delivery.order.status.OrderStatus
import com.nevoroman.delivery.order.status.PackagingType

object OrderData {

    fun userId() = 999L

    fun aOrder() = Order(
        0,
        userId(),
        aAddress(),
        aAddress(),
        type = PackagingType.SMALL,
        estimatedWeight = 3.0,
        additionalDetail = null
    )

    private fun aAddress() = "7a Natia Bashaleishvili St, Tbilisi"
}

fun Order.toRequest() = DeliveryOrderRequest(
    origin = this.origin,
    destination = this.destination,
    type = this.type,
    estimatedWeight = this.estimatedWeight,
    additionalDetail = this.additionalDetail
)

fun Order.toCreatedEvent() = OrderCreatedEvent(
    this.id, this.userId, this.origin, this.destination
)

data class OrderStatusEntry(
    val orderId: Long,
    val orderStatus: OrderStatus,
    val userId: Long,
    val previousStatus: OrderStatus = OrderStatus.CREATED
)