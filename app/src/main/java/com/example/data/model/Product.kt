package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products")
data class Product(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val category: String, // "Jewellery", "Watches"
    val subCategory: String = "General", // "Women", "Men", "Unisex", "General"
    val price: Double,
    val discountPrice: Double? = null,
    val imageUrl: String, // local drawable name or remote URL
    val description: String,
    val options: String = "Standard", // comma-separated options (e.g. "18K Yellow Gold, Rose Gold, Platinum")
    val isAvailable: Boolean = true,
    val isFeatured: Boolean = false,
    val rating: Double = 4.9,
    val reviewsCount: Int = 18,
    val createdTimestamp: Long = System.currentTimeMillis()
)
