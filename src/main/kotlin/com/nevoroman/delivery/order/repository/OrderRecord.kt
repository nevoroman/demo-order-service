package com.nevoroman.delivery.order.repository

import com.nevoroman.delivery.order.model.Order
import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.Instant

@Entity
@Table(name = "orders")
data class OrderRecord(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    open var id: Long?,

    @Column(name = "user_id", nullable = false)
    val userId: Long,

    @Column(nullable = false)
    val origin: String,

    @Column(nullable = false)
    val destination: String,

    @Column(nullable = false)
    val type: String,

    @Column(name = "estimated_weight", nullable = false)
    val estimatedWeight: Double,

    @Column(name = "additional_detail")
    val additionalDetail: String?,

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    val createdAt: Instant = Instant.now(),

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    val updatedAt: Instant = Instant.now()

){
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as Order
        return id == other.id
    }

    override fun hashCode(): Int = id.hashCode()

    @Override
    override fun toString(): String {
        return this::class.simpleName + "(  id = $id   ,   userId = $userId   ,   origin = $origin   ,   destination = $destination   ,   type = $type   ,   estimatedWeight = $estimatedWeight   ,   additionalDetail = $additionalDetail )"
    }
}