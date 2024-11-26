package com.nevoroman.delivery.order.courier

import com.nevoroman.delivery.order.OrderService
import com.nevoroman.delivery.order.status.OrderStatus
import org.springframework.stereotype.Service

@Service
class CourierAssignmentService(
    private val assignmentRepository: CourierAssignmentRepository,
    private val orderService: OrderService
) {

    fun assign(assignment: CourierAssignment) {
        assignmentRepository.save(assignment)
        orderService.updateStatus(OrderStatus.COURIER_ASSIGNED, assignment.orderId)
    }

}