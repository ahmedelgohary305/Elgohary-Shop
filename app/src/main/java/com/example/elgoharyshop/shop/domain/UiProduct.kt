package com.example.elgoharyshop.shop.domain

data class UiProduct(
    val id: String,
    val title: String,
    val imageUrl: String,
    val price: String,
    val clicked: Boolean = false
)
