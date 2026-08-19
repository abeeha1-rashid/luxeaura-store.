package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "wishlist")
data class WishlistItem(
    @PrimaryKey
    val productId: Long,
    val addedTimestamp: Long = System.currentTimeMillis()
)
