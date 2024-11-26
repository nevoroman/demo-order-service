package com.nevoroman.delivery.order

import com.nevoroman.delivery.order.UserRole.USER
import com.nevoroman.delivery.order.model.Order
import com.nevoroman.delivery.order.status.OrderStatus
import org.springframework.http.HttpStatus
import org.springframework.security.access.annotation.Secured
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/order")
class OrderController(
    val orderService: OrderService
) {

    @Secured("USER", "ADMIN")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@RequestBody deliveryRequest: DeliveryOrderRequest): Long {
        return orderService.create(
            deliveryRequest.toDomain()
        )
    }

    @Secured("ADMIN")
    @PutMapping("/{orderId}")
    fun update(@RequestBody deliveryRequest: DeliveryOrderRequest, @PathVariable orderId: Long) {
        orderService.update(
            deliveryRequest.toDomain(orderId)
        )
    }

    @Secured("USER")
    @PostMapping("/{id}/destination")
    fun requestDestinationChange(@PathVariable("id") id: Long, @RequestBody newDestination: String) {
        orderService.requestDestinationChange(newDestination, id)
    }

    @Secured("USER")
    @PostMapping("/{id}/cancel")
    fun cancel(@PathVariable("id") id: Long) {
        orderService.cancel(id)
    }

    @Secured("ADMIN")
    @PostMapping("/{id}/status/{status}")
    fun updateStatus(@PathVariable("id") id: Long, @PathVariable("status") status: OrderStatus) {
        orderService.updateStatus(status, id)
    }

    @Secured("USER", "ADMIN")
    @GetMapping("/{id}")
    fun get(@PathVariable("id") id: Long): Order {
        return orderService.get(id)
    }

    @Secured("USER", "ADMIN")
    @GetMapping
    fun getAll(): List<Order> {
        val user = SecurityContextHolder.getContext().authentication
        return if (user.hasRole(USER)) {
            orderService.getAll(user.id())
        } else {
            orderService.getAll()
        }
    }

    private fun DeliveryOrderRequest.toDomain(orderId: Long = 0): Order = Order(
        userId = SecurityContextHolder.getContext().authentication.name.toLong(),
        origin = this.origin,
        destination = this.destination,
        type = this.type,
        estimatedWeight = this.estimatedWeight,
        additionalDetail = this.additionalDetail,
        id = orderId
    )

}

