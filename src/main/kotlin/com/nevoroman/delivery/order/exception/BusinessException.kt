package com.nevoroman.delivery.order.exception

import org.springframework.http.HttpStatusCode

abstract class BusinessException(
    val code: HttpStatusCode,
    message: String,
): RuntimeException(message)