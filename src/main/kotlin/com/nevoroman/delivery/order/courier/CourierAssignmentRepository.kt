package com.nevoroman.delivery.order.courier

import com.nevoroman.delivery.order.repository.OrderRecord
import jakarta.persistence.EntityManager
import org.springframework.stereotype.Repository

@Repository
class CourierAssignmentRepository(
    val jpaRepository: JpaCourierAssignmentRepository,
    val entityManager: EntityManager
) {

    fun save(assignment: CourierAssignment) {
        jpaRepository.save(assignment.toRecord())
    }

    fun CourierAssignment.toRecord(): CourierAssignmentRecord = CourierAssignmentRecord(
        order = entityManager.getReference(OrderRecord::class.java, this.orderId),
        courierId = this.courierId,
        timeWindowFrom = this.timeWindowFrom,
        timeWindowTo = this.timeWindowTo
    )

}