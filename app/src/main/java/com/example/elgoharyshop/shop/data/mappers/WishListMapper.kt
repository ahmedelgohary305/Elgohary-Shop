package com.example.elgoharyshop.shop.data.mappers

import com.example.elgoharyshop.shop.data.local.WishListEntity
import com.example.elgoharyshop.shop.domain.UiProduct

fun WishListEntity.toUiProduct(): UiProduct {
    return UiProduct(
        id = id,
        title = title,
        imageUrl = imageUrl,
        price = price
    )
}

fun UiProduct.toWishListEntity(): WishListEntity {
    return WishListEntity(
        id = id,
        title = title,
        imageUrl = imageUrl,
        price = price
    )
}