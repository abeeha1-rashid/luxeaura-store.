package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cart_items")
data class CartItem(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val productId: Long,
    val quantity: Int = 1,
    val selectedOption: String = "Standard",
    val addedTimestamp: Long = System.currentTimeMillis()
)

data class CartItemWithProduct(
    val cartItem: CartItem,
    val product: Product
)
