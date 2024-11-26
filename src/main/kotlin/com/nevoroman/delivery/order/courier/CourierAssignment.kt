package com.nevoroman.delivery.order.courier

import java.time.Instant

data class CourierAssignment(
    val courierId: Long,
    val orderId: Long,
    val timeWindowFrom: Instant,
    val timeWindowTo: Instant,
)