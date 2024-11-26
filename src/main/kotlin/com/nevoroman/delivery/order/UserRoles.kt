package com.nevoroman.delivery.order

import org.springframework.security.core.Authentication

enum class UserRole {
    USER,
    COURIER,
    ADMIN
}

fun Authentication.hasRole(role: UserRole) = this.authorities.any { it.authority == role.name }
fun Authentication.id() = this.name.toLong()