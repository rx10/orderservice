package com.example.orderservice.repository

import com.example.orderservice.model.OrderModel
import org.springframework.data.jpa.repository.JpaRepository

interface OrderRepository : JpaRepository<OrderModel, Long>