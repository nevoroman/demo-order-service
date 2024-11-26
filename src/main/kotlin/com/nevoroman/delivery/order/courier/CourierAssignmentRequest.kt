package com.nevoroman.delivery.order.courier

import java.time.Instant

data class CourierAssignmentRequest(
    val courierId: Long,
    val orderId: Long,
    val timeWindowStarts: Instant,
    val timeWindowEnds: Instant,
)