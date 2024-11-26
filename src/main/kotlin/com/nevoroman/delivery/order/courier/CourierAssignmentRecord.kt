package com.nevoroman.delivery.order.courier

import com.nevoroman.delivery.order.model.Order
import com.nevoroman.delivery.order.repository.OrderRecord
import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import java.time.Instant

@Entity
@Table(name = "courier_assignment")
data class CourierAssignmentRecord(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    open var id: Long? = null,

    @ManyToOne(cascade = [CascadeType.ALL])
    @JoinColumn(name = "order_id", nullable = false)
    val order: OrderRecord,

    @Column(name = "courier_id", nullable = false)
    val courierId: Long,

    @Column(name = "time_window_from", nullable = false)
    val timeWindowFrom: Instant,

    @Column(name = "time_window_to", nullable = false)
    val timeWindowTo: Instant,

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
        return this::class.simpleName + "(  id = $id   ,   order = $order   ,   courierId = $courierId   ,   timeWindowFrom = $timeWindowFrom   ,   timeWindowTo = $timeWindowTo   ,   createdAt = $createdAt )"
    }

}