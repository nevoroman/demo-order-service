package com.nevoroman.delivery.order.repository

import com.nevoroman.delivery.order.exception.EntityNotFoundException
import com.nevoroman.delivery.order.model.Order
import com.nevoroman.delivery.order.status.PackagingType
import org.springframework.stereotype.Repository
import kotlin.jvm.optionals.getOrNull

@Repository
class OrderRepository(val jpaRepository: JpaOrderRepository) {

    fun save(order: Order): Order {
        val record = jpaRepository.save(order.toRecord())
        return record.toDomain()
    }

    fun get(id: Long): Order = jpaRepository.findById(id).getOrNull()?.toDomain()
        ?: throw EntityNotFoundException()

    fun getAll(): List<Order> = jpaRepository.findAll().map { it.toDomain() }

    fun getAll(userId: Long): List<Order> = jpaRepository.findAllByUserId(userId).map { it.toDomain() }

    fun Order.toRecord(): OrderRecord = OrderRecord(
        id = if (this.id == 0L) null else this.id,
        userId = this.userId,
        origin = this.origin,
        destination = this.destination,
        type = this.type.name,
        estimatedWeight = this.estimatedWeight,
        additionalDetail = this.additionalDetail
    )

    fun OrderRecord.toDomain() = Order(
        id = this.id ?: throw IllegalStateException("ID of the record received from a DB can not be null"),
        userId = this.userId,
        origin = this.origin,
        destination = this.destination,
        type = PackagingType.valueOf(this.type),
        estimatedWeight = this.estimatedWeight,
        additionalDetail = this.additionalDetail
    )

}