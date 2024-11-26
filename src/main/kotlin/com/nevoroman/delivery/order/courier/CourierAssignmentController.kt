package com.nevoroman.delivery.order.courier

import org.springframework.security.access.annotation.Secured
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/courier")
@Secured("COURIER", "ADMIN")
class CourierAssignmentController(
    val service: CourierAssignmentService
) {

    @PutMapping("/assign/")
    fun assign(@RequestBody request: CourierAssignmentRequest) {
        service.assign(request.toDomain())
    }

    fun CourierAssignmentRequest.toDomain(): CourierAssignment {
        return CourierAssignment(
            courierId, orderId, timeWindowStarts, timeWindowEnds
        )
    }

}