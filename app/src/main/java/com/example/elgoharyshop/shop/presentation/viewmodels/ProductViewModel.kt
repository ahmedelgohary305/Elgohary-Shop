package com.example.elgoharyshop.shop.presentation.viewmodels


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.elgoharyshop.shop.domain.ApiResult
import com.example.elgoharyshop.shop.domain.Cart
import com.example.elgoharyshop.shop.domain.DetailProduct
import com.example.elgoharyshop.shop.domain.ShopRepo
import com.example.elgoharyshop.shop.domain.UiProduct
import com.example.elgoharyshop.type.CartInput
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val shopRepo: ShopRepo
) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    var products = MutableStateFlow<List<UiProduct>>(emptyList())
        private set

    var currentProduct = MutableStateFlow<DetailProduct?>(null)
        private set

    var searchedProducts = MutableStateFlow<List<UiProduct>>(emptyList())
        private set

    var productsByCollection = MutableStateFlow<List<UiProduct>>(emptyList())
        private set

    var searchQuery = MutableStateFlow("")
        private set

    var wishLists = MutableStateFlow<List<UiProduct>>(emptyList())
        private set

    var clicked = MutableStateFlow(false)
        private set

    private val _cart = MutableStateFlow<Cart?>(null)
    val cart: StateFlow<Cart?> = _cart.asStateFlow()

    // Keep track of wishlist IDs for consistency
    private var wishlistIds = mutableSetOf<String>()

    init {
        fetchProducts()
        initializeCart()
        getWishLists()
    }

    fun fetchProducts(first: Int = 20) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            when (val result = shopRepo.getProducts(first)) {
                is ApiResult.Success -> {
                    products.value = applyWishlistStatus(result.data)
                    _isLoading.value = false
                }
                is ApiResult.Error -> {
                    _errorMessage.value = result.message
                    _isLoading.value = false
                }
                is ApiResult.Loading -> {
                    _isLoading.value = true
                }
            }
        }
    }

    fun initializeCart() {
        _isLoading.value = true
        _errorMessage.value = null

        viewModelScope.launch {
            when (val result = shopRepo.getOrCreateCart(CartInput())) {
                is ApiResult.Success -> {
                    _cart.value = result.data
                    _isLoading.value = false
                }

                is ApiResult.Error -> {
                    _errorMessage.value = result.message
                    _isLoading.value = false
                }

                is ApiResult.Loading -> {
                    _isLoading.value = true
                }
            }
        }
    }

    fun setSearchQuery(query: String) {
        searchQuery.value = query
    }

    fun getProductById(id: String) = viewModelScope.launch {
        _isLoading.value = true
        _errorMessage.value = null

        when (val result = shopRepo.getProductById(id)) {
            is ApiResult.Success -> {
                // Apply wishlist status to the detailed product if it has products
                currentProduct.value = result.data
                _isLoading.value = false
            }
            is ApiResult.Error -> {
                _errorMessage.value = result.message
                _isLoading.value = false
            }
            is ApiResult.Loading -> {
                _isLoading.value = true
            }
        }
    }

    fun searchProducts(query: String) = viewModelScope.launch {
        _isLoading.value = true
        _errorMessage.value = null

        when (val result = shopRepo.searchProducts(query)) {
            is ApiResult.Success -> {
                searchedProducts.value = applyWishlistStatus(result.data)
                _isLoading.value = false
            }
            is ApiResult.Error -> {
                _errorMessage.value = result.message
                _isLoading.value = false
            }
            is ApiResult.Loading -> {
                _isLoading.value = true
            }
        }
    }

    fun getProductsByCollection(handle: String, first: Int) = viewModelScope.launch {
        _isLoading.value = true
        _errorMessage.value = null

        when (val result = shopRepo.getProductsByCollection(handle, first)) {
            is ApiResult.Success -> {
                productsByCollection.value = applyWishlistStatus(result.data)
                _isLoading.value = false
            }
            is ApiResult.Error -> {
                _errorMessage.value = result.message
                _isLoading.value = false
            }
            is ApiResult.Loading -> {
                _isLoading.value = true
            }
        }
    }

    fun addProductToCart(variantId: String, quantity: Int = 1) = viewModelScope.launch {
        _errorMessage.value = null

        val cartId = _cart.value?.id
        if (cartId != null) {
            when (val result = shopRepo.addItemToCart(cartId, variantId, quantity)) {
                is ApiResult.Success -> {
                    _cart.value = result.data
                }
                is ApiResult.Error -> {
                    _errorMessage.value = result.message
                }
                is ApiResult.Loading -> {}
            }
        } else {
            when (val result = shopRepo.getOrCreateCart(CartInput())) {
                is ApiResult.Success -> {
                    val newCartId = result.data.id
                    _cart.value = result.data

                    // Now add the item to the new cart
                    when (val addResult = shopRepo.addItemToCart(newCartId, variantId, quantity)) {
                        is ApiResult.Success -> {
                            _cart.value = addResult.data
                        }
                        is ApiResult.Error -> {
                            _errorMessage.value = addResult.message
                        }
                        is ApiResult.Loading -> {}
                    }
                }
                is ApiResult.Error -> {
                    _errorMessage.value = result.message
                    _isLoading.value = false
                }
                is ApiResult.Loading -> {
                    _isLoading.value = true
                }
            }
        }
    }

    fun updateItem(lineId: String, quantity: Int) = viewModelScope.launch {
        _errorMessage.value = null

        _cart.value?.let { cart ->
            when (val result = shopRepo.updateCartLine(cart.id, lineId, quantity)) {
                is ApiResult.Success -> {
                    _cart.value = result.data
                }
                is ApiResult.Error -> {
                    _errorMessage.value = result.message
                }
                is ApiResult.Loading -> {}
            }
        } ?: run {
            _errorMessage.value = "Cart not found"
        }
    }

    fun removeItem(lineId: String) = viewModelScope.launch {
        _errorMessage.value = null

        _cart.value?.let { cart ->
            when (val result = shopRepo.removeCartLine(cart.id, lineId)) {
                is ApiResult.Success -> {
                    _cart.value = result.data
                }
                is ApiResult.Error -> {
                    _errorMessage.value = result.message
                }
                is ApiResult.Loading -> {}
            }
        } ?: run {
            _errorMessage.value = "Cart not found"
        }
    }

    fun buyNow(variantId: String, onResult: (String?) -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            when (val result = shopRepo.buyNow(variantId)) {
                is ApiResult.Success -> {
                    onResult(result.data)
                    _isLoading.value = false
                }
                is ApiResult.Error -> {
                    _errorMessage.value = result.message
                    onResult(null)
                    _isLoading.value = false
                }
                is ApiResult.Loading -> {
                    _isLoading.value = true
                }
            }
        }
    }

    fun getWishLists() {
        viewModelScope.launch {
            shopRepo.getWishLists().collect { wishListProducts ->
                wishLists.value = wishListProducts

                // Update the wishlist IDs set
                wishlistIds.clear()
                wishlistIds.addAll(wishListProducts.map { it.id })

                // Update all product lists with current wishlist status
                updateAllProductListsWithWishlistStatus()
            }
        }
    }

    fun deleteWishList(id: String) {
        viewModelScope.launch {
            shopRepo.deleteWishList(id)
            // The wishlist flow will automatically update and trigger getWishLists()
        }
    }

    private fun insertWishList(wishList: UiProduct) {
        viewModelScope.launch {
            shopRepo.insertWishList(wishList)
            // The wishlist flow will automatically update and trigger getWishLists()
        }
    }

    fun toggleWishList(product: UiProduct) {
        val isWishListed = !product.clicked

        if (isWishListed) {
            insertWishList(product.copy(clicked = true))
        } else {
            deleteWishList(product.id)
        }
    }

    // Helper function to apply wishlist status to a list of products
    private fun applyWishlistStatus(productList: List<UiProduct>): List<UiProduct> {
        return productList.map { product ->
            product.copy(clicked = product.id in wishlistIds)
        }
    }

    // Update all product lists with current wishlist status
    private fun updateAllProductListsWithWishlistStatus() {
        products.value = applyWishlistStatus(products.value)
        searchedProducts.value = applyWishlistStatus(searchedProducts.value)
        productsByCollection.value = applyWishlistStatus(productsByCollection.value)
    }
}