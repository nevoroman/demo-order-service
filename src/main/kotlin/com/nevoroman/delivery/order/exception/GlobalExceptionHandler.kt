package com.nevoroman.delivery.order.exception

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException::class)
    fun handleBusinessException(e: BusinessException): ResponseEntity<String> {
        e.printStackTrace()
        return ResponseEntity
            .status(e.code)
            .body(e.message)
    }

}