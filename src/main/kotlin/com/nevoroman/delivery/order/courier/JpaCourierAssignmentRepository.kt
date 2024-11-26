package com.nevoroman.delivery.order.courier

import org.springframework.data.jpa.repository.JpaRepository

interface JpaCourierAssignmentRepository: JpaRepository<CourierAssignmentRecord, Long>