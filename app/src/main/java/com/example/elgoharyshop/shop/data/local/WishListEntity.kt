package com.example.elgoharyshop.shop.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class WishListEntity(
    @PrimaryKey
    val id: String,
    val title: String,
    val imageUrl: String,
    val price: String,
)