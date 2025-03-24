package com.example.orderservice.controller

import com.example.orderservice.model.OrderModel
import com.example.orderservice.service.OrderService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.http.HttpStatus

@RestController
@RequestMapping("/api/orders")
class OrderController(private val service: OrderService) {

    @PostMapping
    fun createOrder(@RequestBody orderRequest: OrderRequest): ResponseEntity<Any> {
        return try {
            val order = OrderModel(
                productName = orderRequest.productName,
                quantity = orderRequest.quantity,
                price = orderRequest.price,
                customerId = orderRequest.customerId
            )
            // Try to create the order and return 200 if successful
            ResponseEntity.ok(service.createOrder(orderRequest.customerId, order))
        } catch (e: RuntimeException) {
            // Return 404 if customer not found
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ErrorResponse("Customer not found"))
        }
    }

    @GetMapping
    fun getAllOrders(): ResponseEntity<List<OrderModel>> = ResponseEntity.ok(service.getAllOrders())

    @GetMapping("/{orderId}")
    fun getOrderById(@PathVariable orderId: Long): ResponseEntity<OrderModel> =
        ResponseEntity.ok(service.getOrderById(orderId))

    @PutMapping("/{orderId}")
    fun updateOrder(@PathVariable orderId: Long, @RequestBody updatedOrder: OrderModel): ResponseEntity<OrderModel> =
        ResponseEntity.ok(service.updateOrder(orderId, updatedOrder))

    @DeleteMapping("/{orderId}")
    fun deleteOrder(@PathVariable orderId: Long): ResponseEntity<String> {
        service.deleteOrder(orderId)
        return ResponseEntity.ok("Order deleted successfully")
    }

}

data class OrderRequest(
    val productName: String,
    val quantity: Int,
    val price: Double,
    val customerId: Long  // Explicitly include the customerId
)

data class ErrorResponse(
    val message: String
)
