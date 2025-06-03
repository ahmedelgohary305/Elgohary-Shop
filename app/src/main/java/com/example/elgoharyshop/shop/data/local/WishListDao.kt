package com.example.elgoharyshop.shop.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow


@Dao
interface WishListDao {
    @Query("SELECT * FROM WishListEntity")
    fun getAllWishLists(): Flow<List<WishListEntity>>

    @Query("DELETE FROM WishListEntity WHERE id = :id")
    suspend fun deleteWishList(id: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWishList(wishList: WishListEntity)
}