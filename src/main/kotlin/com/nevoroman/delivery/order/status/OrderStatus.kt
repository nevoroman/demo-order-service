package com.nevoroman.delivery.order.status

import com.nevoroman.delivery.order.UserRole
import com.nevoroman.delivery.order.status.OrderStatus.*
import com.nevoroman.delivery.order.UserRole.ADMIN
import com.nevoroman.delivery.order.UserRole.COURIER
import com.nevoroman.delivery.order.hasRole
import com.nevoroman.delivery.order.id
import com.nevoroman.delivery.order.model.Order
import org.springframework.security.core.Authentication

enum class OrderStatus(vararg val statusConditions: OrderStatusCondition) {
    CREATED(
        previousStatusShouldBe(null),
        authorizedUserOrAdmin()
    ),
    AWAITING_CONFIRMATION(
        previousStatusShouldBe(CREATED),
        requireRole(ADMIN)
    ),
    CONFIRMED(
        previousStatusShouldBe(AWAITING_CONFIRMATION),
        requireRole(ADMIN)
    ),
    COURIER_ASSIGNED(
        previousStatusShouldBe(CONFIRMED),
        requireRole(ADMIN)
    ),
    AWAITING_PICKUP(
        previousStatusShouldBe(COURIER_ASSIGNED),
        requireRole(COURIER, ADMIN)
    ),
    IN_PROGRESS(
        previousStatusShouldBe(AWAITING_PICKUP),
        requireRole(COURIER, ADMIN)
    ),
    DELIVERED(
        previousStatusShouldBe(IN_PROGRESS),
        requireRole(COURIER, ADMIN)
    ),
    DESTINATION_CHANGE_REQUEST(
        nonFinalizedStatus(),
        authorizedUserOrAdmin()
    ),
    CANCELED(
        nonFinalizedStatus().or(requireRole(ADMIN)),
        authorizedUserOrAdmin()
    ),
    IN_DISPUTE(
        previousStatusShouldBe(DELIVERED, CANCELED),
        authorizedUserOrAdmin()
    ),
    CLOSED(
        previousStatusShouldBe(DELIVERED, IN_DISPUTE)
    ),
}

typealias OrderStatusCondition = (order: Order, currentStatus: OrderStatus?, user: Authentication) -> Boolean

fun OrderStatusCondition.or(another: OrderStatusCondition): OrderStatusCondition =
    { o, s, u -> this(o, s, u) || another(o, s, u) }

fun previousStatusShouldBe(vararg allowedStatuses: OrderStatus?): OrderStatusCondition =
    { _, currentStatus, _ -> allowedStatuses.contains(currentStatus) }

fun requireRole(vararg allowedRoles: UserRole) : OrderStatusCondition =
    { _, _, user -> allowedRoles.any { user.hasRole(it)  } }

fun authorizedUserOrAdmin(): OrderStatusCondition =
    { order, _, user ->
        user.hasRole(UserRole.USER) && user.id() == order.userId
                || user.hasRole(ADMIN)
    }

fun nonFinalizedStatus() = previousStatusShouldBe(
    CREATED, AWAITING_CONFIRMATION, CONFIRMED, COURIER_ASSIGNED
)

fun OrderStatus.validate(currentStatus: OrderStatus, order: Order, user: Authentication): OrderStatus {
    this.statusConditions.all { it(order, currentStatus, user) }

    if (!this.statusConditions.all { it(order, currentStatus, user) }) {
        throw InvalidOrderStatusException(currentStatus, this, order, user)
    }

    return this
}

class InvalidOrderStatusException(currentStatus: OrderStatus, newStatus: OrderStatus, order: Order, user: Authentication):
    RuntimeException(
        "Order status $newStatus can not be applied to the order $order " +
        "with current status $currentStatus. User ID: ${user.id()}, roles: ${user.authorities}")