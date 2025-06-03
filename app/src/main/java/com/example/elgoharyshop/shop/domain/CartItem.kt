package com.example.elgoharyshop.shop.domain

data class CartItem(
    val id: String,
    val title: String,
    val imageUrl: String?,
    val price: String,
    val quantity: Int
)
