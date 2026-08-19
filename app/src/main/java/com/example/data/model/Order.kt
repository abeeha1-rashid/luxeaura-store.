package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "customer_orders")
data class Order(
    @PrimaryKey
    val id: String, // e.g. "LXA-1082"
    val customerName: String,
    val phone: String,
    val whatsapp: String,
    val address: String,
    val city: String,
    val itemsSummary: String, // formatted text summary of products and quantities
    val totalAmount: Double,
    val orderDate: String,
    val status: String = "Pending", // "Pending", "Confirmed", "Shipped", "Delivered", "Cancelled"
    val timestamp: Long = System.currentTimeMillis()
)
