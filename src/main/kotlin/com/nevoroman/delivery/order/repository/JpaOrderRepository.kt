package com.nevoroman.delivery.order.repository

import org.springframework.data.jpa.repository.JpaRepository

interface JpaOrderRepository: JpaRepository<OrderRecord, Long> {

    fun findAllByUserId(userId: Long): List<OrderRecord>

}