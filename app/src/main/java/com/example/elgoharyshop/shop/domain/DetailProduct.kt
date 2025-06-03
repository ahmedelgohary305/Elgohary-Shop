package com.example.elgoharyshop.shop.domain

import com.example.elgoharyshop.GetProductByIdQuery.Variants
import java.math.BigDecimal

data class DetailProduct(
    val id: String,
    val title: String,
    val variants: Variants,
    val description: String,
    val images: List<String>,
    val price: Price
)

data class Price(
    val amount: BigDecimal,
    val currencyCode: String
)
