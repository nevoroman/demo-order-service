package com.nevoroman.delivery.order

import com.nevoroman.delivery.order.OrderData.aOrder
import com.nevoroman.delivery.order.OrderData.userId
import com.nevoroman.delivery.order.model.Order
import com.nevoroman.delivery.order.status.OrderStatus
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpMethod

class OrderControllerIT: ControllerIT() {

    @Autowired
    lateinit var orderCreatedConsumer: OrderCreatedEventConsumer

    val createOrderEndpoint = Endpoint("/order", HttpMethod.POST)
    fun changeDestinationEndpoint(orderId: Long) = Endpoint("/order/$orderId/destination", HttpMethod.POST)

    @Test
    fun `Should create an order for an authorized user`() {
        // given
        val order = aOrder()

        // when
        val response = createOrderEndpoint.send(order.toRequest(), UserRole.USER)

        // then
        response.statusCode(201)

        val orderId = response.extract().body().`as`(Long::class.java)
        val expectedEvent = order.copy(id = orderId).toCreatedEvent()

        orderCreatedConsumer.takeOne()?.value() shouldBe expectedEvent
    }

    @Test
    fun `Should fail to create an order without authorization`() {
        // given
        val order = aOrder()

        // when
        val response = createOrderEndpoint.sendUnauthorized(order.toRequest())

        // then
        response.statusCode(401)
    }

    @Test
    fun `Should fail to create an order with incorrect role`() {
        // given
        val order = aOrder()

        // when
        val response = createOrderEndpoint.send(order.toRequest(), UserRole.COURIER)

        // then
        response.statusCode(403)
    }

    @Test
    fun `Should correctly change destination for an existing order`() {
        // given
        val order = aOrder()
            .`save to database`()
            .withId()
            .`save status to DB`(OrderStatus.CREATED)

        // when
        val response = changeDestinationEndpoint(order.id).send("new dest", UserRole.USER)

        // then
        response.statusCode(200)
    }

    @Test
    fun `Should fail to change destination if order in incorrect status`() {
        // given
        val order = aOrder()
            .`save to database`()
            .withId()
            .`save status to DB`(OrderStatus.AWAITING_PICKUP)

        // when
        val response = changeDestinationEndpoint(order.id).send("new dest", UserRole.USER)

        // then
        response.statusCode(500)
    }

    @Test
    fun `Should fail change destination for non-existing order`() {
        // when
        val response = changeDestinationEndpoint(1).send("new dest", UserRole.USER)

        // then
        response.statusCode(404)
    }

    private fun Order.withId(): Order {
        return this.copy(id = testRepository.lastInsertId!!)
    }

    private fun Order.`save status to DB`(status: OrderStatus): Order {
        OrderStatusEntry(this.id, status, userId()).`save to database`()
        return this
    }

}