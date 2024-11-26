package com.nevoroman.delivery.order

import com.nevoroman.delivery.order.status.PackagingType

data class DeliveryOrderRequest(
    val origin: String,
    val destination: String,
    val type: PackagingType,
    val estimatedWeight: Double,
    val additionalDetail: String?
)