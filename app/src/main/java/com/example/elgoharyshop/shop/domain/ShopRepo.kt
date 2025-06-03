package com.example.elgoharyshop.shop.domain


import com.example.elgoharyshop.shop.data.local.WishListEntity
import com.example.elgoharyshop.type.CartInput
import kotlinx.coroutines.flow.Flow

interface ShopRepo {
    suspend fun getProducts(first: Int): ApiResult<List<UiProduct>>
    suspend fun getProductById(id: String): ApiResult<DetailProduct>
    suspend fun searchProducts(query: String): ApiResult<List<UiProduct>>
    suspend fun getProductsByCollection(handle: String, first: Int): ApiResult<List<UiProduct>>
    suspend fun createCart(input: CartInput): ApiResult<Cart>
    suspend fun addItemToCart(cartId: String, merchandiseId: String, quantity: Int): ApiResult<Cart>
    suspend fun updateCartLine(cartId: String, lineId: String, quantity: Int): ApiResult<Cart>
    suspend fun removeCartLine(cartId: String, lineId: String): ApiResult<Cart>
    suspend fun fetchCart(cartId: String): ApiResult<Cart>
    suspend fun getOrCreateCart(input: CartInput): ApiResult<Cart>
    suspend fun buyNow(variantId: String): ApiResult<String>
    fun getWishLists(): Flow<List<UiProduct>>
    suspend fun deleteWishList(id: String)
    suspend fun insertWishList(wishList: UiProduct)

}