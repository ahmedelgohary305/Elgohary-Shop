package com.example.elgoharyshop.shop.data

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.Optional
import com.example.elgoharyshop.CartCreateMutation
import com.example.elgoharyshop.CartLinesAddMutation
import com.example.elgoharyshop.CartLinesRemoveMutation
import com.example.elgoharyshop.CartLinesUpdateMutation
import com.example.elgoharyshop.GetCartQuery
import com.example.elgoharyshop.GetProductByIdQuery
import com.example.elgoharyshop.GetProductsByCollectionQuery
import com.example.elgoharyshop.GetProductsQuery
import com.example.elgoharyshop.SearchProductsQuery
import com.example.elgoharyshop.shop.data.local.WishListDatabase
import com.example.elgoharyshop.shop.data.mappers.toDetailProduct
import com.example.elgoharyshop.shop.data.mappers.toDomain
import com.example.elgoharyshop.shop.data.mappers.toUiProduct
import com.example.elgoharyshop.shop.data.mappers.toWishListEntity
import com.example.elgoharyshop.shop.domain.ApiResult
import com.example.elgoharyshop.shop.domain.Cart
import com.example.elgoharyshop.shop.domain.DetailProduct
import com.example.elgoharyshop.shop.domain.ShopRepo
import com.example.elgoharyshop.shop.domain.UiProduct
import com.example.elgoharyshop.type.CartBuyerIdentityInput
import com.example.elgoharyshop.type.CartInput
import com.example.elgoharyshop.type.CartLineInput
import com.example.elgoharyshop.type.CartLineUpdateInput
import com.example.elgoharyshop.utils.safeApiCall
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

@OptIn(DelicateCoroutinesApi::class)
class ShopRepoImpl(
    private val apolloClient: ApolloClient,
    private val appDataStore: AppDataStore,
    private val wishListDatabase: WishListDatabase
) : ShopRepo {

    override suspend fun getProducts(first: Int): ApiResult<List<UiProduct>> = safeApiCall {
        val response = apolloClient.query(GetProductsQuery(first)).execute()
        response.data?.products?.edges?.map { it.node.toUiProduct() } ?: emptyList()
    }

    override suspend fun getProductById(id: String): ApiResult<DetailProduct> = safeApiCall {
        val response = apolloClient.query(GetProductByIdQuery(id)).execute()
        response.data?.toDetailProduct() ?: throw Exception("Product not found")
    }

    override suspend fun searchProducts(query: String): ApiResult<List<UiProduct>> = safeApiCall {
        val response = apolloClient.query(SearchProductsQuery(query)).execute()
        response.data?.products?.edges?.map { it.node.toUiProduct() } ?: emptyList()
    }

    override suspend fun getProductsByCollection(handle: String, first: Int): ApiResult<List<UiProduct>> = safeApiCall {
        val response = apolloClient.query(GetProductsByCollectionQuery(handle, first)).execute()
        response.data?.collection?.products?.edges?.mapNotNull { it.node.toUiProduct() } ?: emptyList()
    }

    override suspend fun createCart(input: CartInput): ApiResult<Cart> = safeApiCall {
        val response = apolloClient.mutation(CartCreateMutation(input)).execute()
        val cart = response.data?.cartCreate?.cart?.toDomain() ?: throw Exception("Failed to create cart")
        response.data?.cartCreate?.cart?.id?.let { appDataStore.saveCartId(it) }
        cart
    }

    override suspend fun getOrCreateCart(input: CartInput): ApiResult<Cart> = safeApiCall {
        val existingId = appDataStore.getCartId()
        try {
            if (existingId != null) {
                val existingCartResult = fetchCart(existingId)
                if (existingCartResult is ApiResult.Success) {
                    return@safeApiCall existingCartResult.data
                }
            }
            val createCartResult = createCart(input)
            if (createCartResult is ApiResult.Success) {
                return@safeApiCall createCartResult.data
            }
            throw Exception("Failed to get or create cart")
        } catch (e: Exception) {
            throw Exception("Failed to get or create cart: ${e.message}")
        }
    }

    override suspend fun addItemToCart(
        cartId: String,
        merchandiseId: String,
        quantity: Int
    ): ApiResult<Cart> = safeApiCall {
        val input = CartLineInput(merchandiseId = merchandiseId, quantity = Optional.Present(quantity))
        val response = apolloClient.mutation(CartLinesAddMutation(cartId, listOf(input))).execute()
        response.data?.cartLinesAdd?.cart?.toDomain() ?: throw Exception("Failed to add item to cart")
    }

    override suspend fun updateCartLine(
        cartId: String,
        lineId: String,
        quantity: Int
    ): ApiResult<Cart> = safeApiCall {
        val input = CartLineUpdateInput(id = lineId, quantity = Optional.Present(quantity))
        val response = apolloClient.mutation(CartLinesUpdateMutation(cartId, listOf(input))).execute()
        response.data?.cartLinesUpdate?.cart?.toDomain() ?: throw Exception("Failed to update cart")
    }

    override suspend fun removeCartLine(
        cartId: String,
        lineId: String
    ): ApiResult<Cart> = safeApiCall {
        val response = apolloClient.mutation(CartLinesRemoveMutation(cartId, listOf(lineId))).execute()
        response.data?.cartLinesRemove?.cart?.toDomain() ?: throw Exception("Failed to remove cart item")
    }

    override suspend fun fetchCart(cartId: String): ApiResult<Cart> = safeApiCall {
        val response = apolloClient.query(GetCartQuery(cartId)).execute()
        response.data?.cart?.toDomain() ?: throw Exception("Cart not found")
    }

    override suspend fun buyNow(variantId: String): ApiResult<String> = safeApiCall {
        val response = apolloClient.mutation(
            CartCreateMutation(
                CartInput(
                    lines = Optional.Present(
                        listOf(
                            CartLineInput(
                                merchandiseId = variantId,
                                quantity = Optional.Present(1)
                            )
                        )
                    )
                )
            )
        ).execute()
        (response.data?.cartCreate?.cart?.checkoutUrl ?: throw Exception("Failed to create checkout URL")).toString()
    }

    override fun getWishLists(): Flow<List<UiProduct>> {
        return wishListDatabase.dao.getAllWishLists().map { entities ->
            entities.map { it.toUiProduct() }
        }
    }

    override suspend fun deleteWishList(id: String) {
        wishListDatabase.dao.deleteWishList(id)
    }

    override suspend fun insertWishList(wishList: UiProduct) {
        wishListDatabase.dao.insertWishList(wishList.toWishListEntity())
    }
}