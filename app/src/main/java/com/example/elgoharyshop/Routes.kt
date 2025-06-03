package com.example.elgoharyshop

sealed class Routes(val route: String) {
    object AuthScreens : Routes("auth_screens")
    object MainScreens : Routes("main_screens")

    object SignUpScreen : Routes("signup_screen")
    object LogInScreen : Routes("login_screen")
    object HomeScreen : Routes("home_screen")
    object CategoryProductsScreen : Routes("category_products_screen")
    object ProductDetailsScreen : Routes("product_details_screen")
    object CartScreen : Routes("cart_screen")
    object WishListScreen : Routes("wishlist_screen")
    object SearchScreen : Routes("search_screen")
}