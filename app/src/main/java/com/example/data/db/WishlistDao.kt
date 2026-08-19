package com.example.data.db

import androidx.room.*
import com.example.data.model.WishlistItem
import kotlinx.coroutines.flow.Flow

@Dao
interface WishlistDao {
    @Query("SELECT * FROM wishlist ORDER BY addedTimestamp DESC")
    fun getAllWishlistItems(): Flow<List<WishlistItem>>

    @Query("SELECT EXISTS(SELECT 1 FROM wishlist WHERE productId = :productId)")
    fun isWishlisted(productId: Long): Flow<Boolean>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addToWishlist(item: WishlistItem)

    @Query("DELETE FROM wishlist WHERE productId = :productId")
    suspend fun removeFromWishlist(productId: Long)

    @Query("DELETE FROM wishlist")
    suspend fun clearWishlist()
}
