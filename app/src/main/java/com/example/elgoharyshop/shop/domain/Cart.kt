package com.example.elgoharyshop.shop.domain

data class Cart(
    val id: String,
    val checkoutUrl: String,
    val items: List<CartItem>,
    val totalPrice: String
)