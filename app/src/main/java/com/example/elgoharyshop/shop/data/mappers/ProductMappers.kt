package com.example.elgoharyshop.shop.data.mappers

import com.example.elgoharyshop.GetProductByIdQuery
import com.example.elgoharyshop.GetProductsByCollectionQuery
import com.example.elgoharyshop.GetProductsQuery
import com.example.elgoharyshop.SearchProductsQuery
import com.example.elgoharyshop.shop.domain.DetailProduct
import com.example.elgoharyshop.shop.domain.Price
import com.example.elgoharyshop.shop.domain.UiProduct
import java.math.BigDecimal

fun GetProductsQuery.Node.toUiProduct(): UiProduct {
    val imageUrl = images.edges.firstOrNull()?.node?.url
    val price = variants.edges.firstOrNull()?.node?.price
    return UiProduct(
        id = id,
        title = title,
        imageUrl = imageUrl.toString(),
        price = "${price?.amount ?: ""} ${price?.currencyCode ?: ""}"
    )
}

fun GetProductsByCollectionQuery.Node.toUiProduct(): UiProduct {
    val imageUrl = images.edges.firstOrNull()?.node?.url
    val price = variants.edges.firstOrNull()?.node?.price
    return UiProduct(
        id = id,
        title = title,
        imageUrl = imageUrl.toString(),
        price = "${price?.amount ?: ""} ${price?.currencyCode ?: ""}"
    )
}

fun SearchProductsQuery.Node.toUiProduct(): UiProduct{
    val imageUrl = images.edges.firstOrNull()?.node?.url
    val price = variants.edges.firstOrNull()?.node?.price

    return UiProduct(
        id = id,
        title = title,
        imageUrl = imageUrl.toString(),
        price = "${price?.amount ?: ""} ${price?.currencyCode ?: ""}"
    )
}

fun GetProductByIdQuery.Data.toDetailProduct(): DetailProduct? {
    val productNode = node?.onProduct ?: return null

    return DetailProduct(
        id = productNode.id,
        title = productNode.title,
        description = productNode.description,
        images = extractImageUrls(productNode.images),
        variants = productNode.variants,
        price = extractPrice(productNode.variants)
    )
}

private fun extractImageUrls(images: GetProductByIdQuery.Images): List<String> {
    return images.edges.map { edge ->
        edge.node.url.toString()
    }
}


private fun extractPrice(variants: GetProductByIdQuery.Variants): Price {
    val firstVariant = variants.edges.firstOrNull()?.node

    return Price(
        amount = firstVariant?.price?.amount?.toString()?.toBigDecimalOrNull() ?: BigDecimal.ZERO,
        currencyCode = firstVariant?.price?.currencyCode?.name ?: "USD"
    )
}
