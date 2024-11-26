package com.nevoroman.delivery.order

import com.nevoroman.delivery.order.model.Order
import com.nevoroman.delivery.order.repository.OrderRepository
import com.nevoroman.delivery.order.status.OrderStatus
import com.nevoroman.delivery.order.status.OrderStatusManager
import org.springframework.stereotype.Service

@Service
class OrderService(
    val repository: OrderRepository,
    val statusManager: OrderStatusManager
) {

    fun create(order: Order): Long {
        val createdOrder = repository.save(order)
        statusManager.setCreated(createdOrder)
        return createdOrder.id
    }

    fun get(orderId: Long): Order {
        return repository.get(orderId)
    }

    fun getAll(userId: Long? = null): List<Order> {
        return if (userId == null) {
            repository.getAll()
        } else {
            repository.getAll(userId)
        }
    }

    fun update(order: Order) {
        repository.save(order)
        // A separate event should be sent here, but no time to implement one more event type
    }

    fun requestDestinationChange(newDestination: String, orderId: Long) {
        val order = repository.get(orderId)

        statusManager.updateStatus(order, OrderStatus.DESTINATION_CHANGE_REQUEST)
        // The plan here is to add a separate entity to the database, which represents the order change requests
        // General idea is that the user shouldn't be allowed to change order details without admins approval
    }

    fun cancel(orderId: Long) {
        val order = repository.get(orderId)

        statusManager.updateStatus(order, OrderStatus.CANCELED)
        // Cancel is an exception: if order is not yet finalized, user should be able to cancel the order themselves
    }

    fun updateStatus(newStatus: OrderStatus, orderId: Long) {
        val order = repository.get(orderId)

        statusManager.updateStatus(order, newStatus)
    }

}