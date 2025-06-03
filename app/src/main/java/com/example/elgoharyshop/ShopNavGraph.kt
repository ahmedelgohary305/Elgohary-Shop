package com.example.elgoharyshop


import CartScreen
import android.content.Context
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.example.elgoharyshop.auth.presentation.AuthViewModel
import com.example.elgoharyshop.auth.presentation.LoginScreen
import com.example.elgoharyshop.auth.presentation.SignUpScreen
import com.example.elgoharyshop.shop.presentation.CategoryProductsScreen
import com.example.elgoharyshop.shop.presentation.ProductDetailScreen
import com.example.elgoharyshop.shop.presentation.ProductListScreen
import com.example.elgoharyshop.shop.presentation.viewmodels.ProductViewModel
import com.example.elgoharyshop.shop.presentation.SearchScreen
import com.example.elgoharyshop.shop.presentation.viewmodels.ThemeViewModel
import com.example.elgoharyshop.shop.presentation.WishListScreen
import java.net.URLDecoder
import java.nio.charset.StandardCharsets

@Composable
fun ShopNavGraph(context: Context, themeViewModel: ThemeViewModel) {
    val navController = rememberNavController()
    val authViewModel = hiltViewModel<AuthViewModel>()
    val productViewModel = hiltViewModel<ProductViewModel>()


    NavHost(
        navController = navController,
        startDestination = Routes.AuthScreens.route
    ){
        navigation(
            route = Routes.AuthScreens.route,
            startDestination = Routes.SignUpScreen.route
        ){
            composable(Routes.SignUpScreen.route) {
                SignUpScreen(
                    viewModel = authViewModel,
                    onNavigateToLogin = { navController.navigate(Routes.LogInScreen.route) },
                )
            }
            composable(Routes.LogInScreen.route) {
                LoginScreen(
                    viewModel = authViewModel,
                    onNavigateToSignUp = { navController.navigate(Routes.SignUpScreen.route) },
                    onLoginSuccess = {
                        navController.navigate(Routes.MainScreens.route) {
                            popUpTo(Routes.AuthScreens.route) { inclusive = true }
                        }
                    }
                )
            }
        }

        navigation(
            route = Routes.MainScreens.route,
            startDestination = Routes.HomeScreen.route
        ){
            composable(Routes.HomeScreen.route){
                ProductListScreen(
                    productViewModel,
                    authViewModel,
                    navController,
                    onLogout = {
                        authViewModel.logout()
                        navController.navigate(Routes.AuthScreens.route) {
                            popUpTo(Routes.MainScreens.route) { inclusive = true }
                        }
                    },
                    onToggleTheme = { themeViewModel.toggleTheme() },
                    onManageAccount = {}
                )
            }

            composable(Routes.CategoryProductsScreen.route + "/{categoryName}" + "/{categoryHandle}"){
                val categoryName = it.arguments?.getString("categoryName") ?: ""
                val categoryHandle = it.arguments?.getString("categoryHandle") ?: ""
                CategoryProductsScreen(categoryName, categoryHandle, productViewModel, navController)
            }

            composable(Routes.ProductDetailsScreen.route + "/{productId}") {
                val encodedId = it.arguments?.getString("productId") ?: ""
                val productId = URLDecoder.decode(encodedId, StandardCharsets.UTF_8.toString())
                ProductDetailScreen(productId, productViewModel, navController)
            }

            composable(Routes.SearchScreen.route){
                SearchScreen(productViewModel, navController)
            }
            composable(Routes.CartScreen.route){
                CartScreen(
                    productViewModel = productViewModel,
                    navController = navController,
                    context = context
                )
            }
            composable(Routes.WishListScreen.route){
                WishListScreen(
                    productViewModel = productViewModel,
                    navController = navController
                )
            }
        }
    }
}