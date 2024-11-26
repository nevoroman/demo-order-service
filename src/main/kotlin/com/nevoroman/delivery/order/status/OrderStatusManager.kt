package com.nevoroman.delivery.order.status

import com.nevoroman.delivery.order.model.Order
import com.nevoroman.delivery.order.OrderCreatedEvent
import com.nevoroman.delivery.order.id
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service

@Service
class OrderStatusManager(
    private val repository: OrderStatusRepository,
    private val orderStatusEventSender: OrderStatusEventSender
) {

    fun setCreated(order: Order) {
        repository.setCreated(order)
        orderStatusEventSender.send(
            OrderCreatedEvent(order.id, order.userId, order.origin, order.destination)
        )
    }

    fun updateStatus(order: Order, newStatus: OrderStatus) {
        val currentStatus = repository.getCurrentStatus(order)
        if (currentStatus == newStatus) return

        val user = SecurityContextHolder.getContext().authentication
        newStatus.validate(currentStatus, order, user)
        repository.setStatus(order, currentStatus, newStatus)

        orderStatusEventSender.send(
            OrderStatusUpdatedEvent(order.id, newStatus, user.id(), currentStatus)
        )
    }

}