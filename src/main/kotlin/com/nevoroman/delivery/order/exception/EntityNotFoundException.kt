package com.nevoroman.delivery.order.exception

import org.springframework.http.HttpStatus

class EntityNotFoundException: BusinessException(HttpStatus.NOT_FOUND, "Entity not found")