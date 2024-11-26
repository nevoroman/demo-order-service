package com.nevoroman.delivery.order.status

import com.nevoroman.delivery.order.model.Order
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Repository

@Repository
class OrderStatusRepository(val jpaRepository: JpaOrderStatusRepository) {

    fun setCreated(order: Order) {
        jpaRepository.save(order.toRecord(OrderStatus.CREATED))
    }

    fun setStatus(order: Order, oldStatus: OrderStatus, newStatus: OrderStatus) {
        jpaRepository.save(
            order.toRecord(newStatus, oldStatus)
        )
    }

    fun getCurrentStatus(order: Order): OrderStatus {
        val status = jpaRepository.findFirstByOrderIdOrderByCreatedAtDesc(order.id)?.newStatus ?: throw RuntimeException()
        return OrderStatus.valueOf(status)
    }

    private fun currentUserId() = SecurityContextHolder.getContext().authentication.name.toLong()

    fun Order.toRecord(status: OrderStatus, previousStatus: OrderStatus? = null) = OrderStatusRecord(
        orderId = this.id,
        previousStatus = previousStatus?.name,
        newStatus = status.name,
        updatedBy = currentUserId()
    )

}