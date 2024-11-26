package com.nevoroman.delivery.order.model

import com.nevoroman.delivery.order.status.PackagingType

data class Order(
    val id: Long,
    val userId: Long,
    val origin: String,
    val destination: String,
    val type: PackagingType,
    val estimatedWeight: Double,
    val additionalDetail: String?
)