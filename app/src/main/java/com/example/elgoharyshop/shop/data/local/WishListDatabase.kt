package com.example.elgoharyshop.shop.data.local

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(
    entities = [WishListEntity::class],
    version = 1,
    exportSchema = false
)
abstract class WishListDatabase: RoomDatabase() {
    abstract val dao: WishListDao
}