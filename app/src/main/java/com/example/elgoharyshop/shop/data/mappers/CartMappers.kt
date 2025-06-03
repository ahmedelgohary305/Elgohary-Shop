package com.example.elgoharyshop.shop.data.mappers

import com.example.elgoharyshop.CartCreateMutation
import com.example.elgoharyshop.CartLinesAddMutation
import com.example.elgoharyshop.CartLinesRemoveMutation
import com.example.elgoharyshop.CartLinesUpdateMutation
import com.example.elgoharyshop.GetCartQuery
import com.example.elgoharyshop.shop.domain.Cart
import com.example.elgoharyshop.shop.domain.CartItem

fun CartCreateMutation.Cart.toDomain(): Cart {
    return Cart(
        id = id,
        checkoutUrl = checkoutUrl.toString(),
        totalPrice = (cost.subtotalAmount.amount as? String) ?: "0.00",
        items = lines.edges.mapNotNull { it.node.toDomain() }
    )
}

fun CartCreateMutation.Node.toDomain(): CartItem? {
    val variant = merchandise.onProductVariant ?: return null
    return CartItem(
        id = id,
        title = variant.product.title,
        imageUrl = (variant.image?.url as? String) ?: "",
        price = (variant.price.amount as? String) ?: "0.00",
        quantity = quantity
    )
}


fun GetCartQuery.Cart.toDomain(): Cart {
    return Cart(
        id = id,
        checkoutUrl = checkoutUrl.toString(),
        totalPrice = (cost.subtotalAmount.amount as? String) ?: "0.00",
        items = lines.edges.mapNotNull { it.node.toDomain() }
    )
}

fun GetCartQuery.Node.toDomain(): CartItem? {
    val variant = merchandise.onProductVariant ?: return null
    return CartItem(
        id = id,
        title = variant.product.title,
        imageUrl = (variant.image?.url as? String) ?: "",
        price = (variant.price.amount as? String) ?: "0.00",
        quantity = quantity
    )
}


fun CartLinesAddMutation.Cart.toDomain(): Cart {
    return Cart(
        id = id,
        checkoutUrl = checkoutUrl.toString(),
        totalPrice = (cost.subtotalAmount.amount as? String) ?: "0.00",
        items = lines.edges.mapNotNull { it.node.toDomain() }
    )
}

fun CartLinesAddMutation.Node.toDomain(): CartItem? {
    val variant = merchandise.onProductVariant ?: return null
    return CartItem(
        id = id,
        title = variant.product.title,
        imageUrl = (variant.image?.url as? String) ?: "",
        price = (variant.price.amount as? String) ?: "0.00",
        quantity = quantity
    )
}


fun CartLinesUpdateMutation.Cart.toDomain(): Cart {
    return Cart(
        id = id,
        checkoutUrl = checkoutUrl.toString(),
        totalPrice = (cost.subtotalAmount.amount as? String) ?: "0.00",
        items = lines.edges.mapNotNull { it.node.toDomain() }
    )
}

fun CartLinesUpdateMutation.Node.toDomain(): CartItem? {
    val variant = merchandise.onProductVariant ?: return null
    return CartItem(
        id = id,
        title = variant.product.title,
        imageUrl = (variant.image?.url as? String) ?: "",
        price = (variant.price.amount as? String) ?: "0.00",
        quantity = quantity
    )
}


fun CartLinesRemoveMutation.Cart.toDomain(): Cart {
    return Cart(
        id = id,
        checkoutUrl = checkoutUrl.toString(),
        totalPrice = (cost.subtotalAmount.amount as? String) ?: "0.00",
        items = lines.edges.mapNotNull { it.node.toDomain() }
    )
}

fun CartLinesRemoveMutation.Node.toDomain(): CartItem? {
    val variant = merchandise.onProductVariant ?: return null
    return CartItem(
        id = id,
        title = variant.product.title,
        imageUrl = (variant.image?.url as? String) ?: "",
        price = (variant.price.amount as? String) ?: "0.00",
        quantity = quantity
    )
}



