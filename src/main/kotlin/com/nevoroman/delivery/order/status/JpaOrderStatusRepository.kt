package com.nevoroman.delivery.order.status

import org.springframework.data.jpa.repository.JpaRepository

interface JpaOrderStatusRepository: JpaRepository<OrderStatusRecord, Long> {

    fun findFirstByOrderIdOrderByCreatedAtDesc(orderId: Long): OrderStatusRecord?

}