package com.nevoroman.delivery.order

import com.nevoroman.delivery.order.courier.CourierAssignment
import com.nevoroman.delivery.order.courier.CourierAssignmentRecord
import com.nevoroman.delivery.order.courier.JpaCourierAssignmentRepository
import com.nevoroman.delivery.order.model.Order
import com.nevoroman.delivery.order.repository.JpaOrderRepository
import com.nevoroman.delivery.order.repository.OrderRecord
import com.nevoroman.delivery.order.status.JpaOrderStatusRepository
import com.nevoroman.delivery.order.status.OrderStatusRecord
import jakarta.persistence.EntityManager
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Component
import org.springframework.test.jdbc.JdbcTestUtils
import java.time.Instant

@Component
class TestRepository {

    @Autowired
    private lateinit var jpaOrderRepository: JpaOrderRepository

    @Autowired
    private lateinit var jpaOrderStatusRepository: JpaOrderStatusRepository

    @Autowired
    private lateinit var jpaCourierAssignmentRepository: JpaCourierAssignmentRepository

    @Autowired
    private lateinit var entityManager: EntityManager

    @Autowired
    lateinit var jdbcTemplate: JdbcTemplate

    var lastInsertId: Long? = null

    /**
     * Adds entity to DB. Manual: could only work if you implemented the logic for every new record type
     */
    fun  <T: Any> saveEntity(entity: T): T {
        return saveEntity(entity, Instant.now())
    }

    fun  <T: Any> saveEntity(entity: T, creationDate: Instant): T {
        lastInsertId = when (entity) {
            is Order -> jpaOrderRepository.save(
                OrderRecord(null, entity.userId, entity.origin, entity.destination, entity.type.name, entity.estimatedWeight, entity.additionalDetail, creationDate, creationDate)
            ).id
            is OrderStatusEntry -> jpaOrderStatusRepository.save(
                OrderStatusRecord(null, entity.orderId, entity.previousStatus.name, entity.orderStatus.name, entity.userId, creationDate)
            ).id
            is CourierAssignment -> jpaCourierAssignmentRepository.save(
                CourierAssignmentRecord(null, entityManager.getReference(OrderRecord::class.java, entity.orderId), entity.courierId, entity.timeWindowFrom, entity.timeWindowTo, creationDate)
            ).id
            else -> throw RuntimeException("Entity type ${entity::class.qualifiedName} is not supported. Consider adding it to TestRepository.saveEntity method.")
        }
        return entity
    }

    fun cleanDatabase() {
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "orders", "order_status", "courier_assignment")
    }

}