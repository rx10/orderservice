package com.example.orderservice.service

import com.example.orderservice.model.CustomerModel
import com.example.orderservice.model.OrderModel
import com.example.orderservice.repository.OrderRepository
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.springframework.http.ResponseEntity
import org.springframework.http.HttpStatus

@Service
class OrderService(
    private val repository: OrderRepository,
    private val restTemplate: RestTemplate
) {
    private val customerServiceUrl = "http://customerservice.railway.internal:8081/api/customers"

    // Check if customer exists in Customer Service
    private fun doesCustomerExist(customerId: Long): Boolean {
        val response: ResponseEntity<CustomerModel> =
            restTemplate.getForEntity("$customerServiceUrl/$customerId", CustomerModel::class.java)
        return response.statusCode == HttpStatus.OK
    }

    fun createOrder(customerId: Long, order: OrderModel): OrderModel {
        // Check if customer exists before creating an order
        if (!doesCustomerExist(customerId)) {
            throw RuntimeException("Customer not found")  // This triggers the exception handler
        }
        return repository.save(order.copy(customerId = customerId))
    }

    fun getAllOrders(): List<OrderModel> = repository.findAll()

    fun getOrderById(orderId: Long): OrderModel =
        repository.findById(orderId).orElseThrow { RuntimeException("Order not found") }

    fun updateOrder(orderId: Long, updatedOrder: OrderModel): OrderModel {
        val existingOrder = getOrderById(orderId)
        val updatedEntity = existingOrder.copy(
            productName = updatedOrder.productName,
            quantity = updatedOrder.quantity,
            price = updatedOrder.price
        )
        return repository.save(updatedEntity)
    }

    fun deleteOrder(orderId: Long) {
        if (!repository.existsById(orderId)) {
            throw RuntimeException("Order not found")
        }
        repository.deleteById(orderId)
    }
}
