package com.example.orderservice.model

import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import java.time.LocalDateTime

@Entity
data class OrderModel(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    val productName: String,
    val quantity: Int,
    val price: Double,
    val customerId: Long,  // Stores Customer ID

    @CreationTimestamp
    val createdAt: LocalDateTime = LocalDateTime.now()
)