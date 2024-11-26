package com.nevoroman.delivery.order.status

import com.nevoroman.delivery.order.model.Order
import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import java.time.Instant


@Entity
@Table(name = "order_status")
data class OrderStatusRecord(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    open var id: Long? = null,

    @Column(name = "order_id", nullable = false)
    val orderId: Long,

    @Column(name = "previous_status", nullable = true)
    val previousStatus: String?,

    @Column(name = "new_status", nullable = false)
    val newStatus: String,

    @Column(name = "updated_by", nullable = false)
    val updatedBy: Long,

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    val createdAt: Instant = Instant.now()

) {
    final override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as Order
        return id == other.id
    }

    final override fun hashCode(): Int = id.hashCode()

    @Override
    override fun toString(): String {
        return this::class.simpleName + "(  id = $id   ,   orderId = $orderId   ,   previousStatus = $previousStatus   ,   newStatus = $newStatus   ,   updatedBy = $updatedBy   ,   createdAt = $createdAt )"
    }
}