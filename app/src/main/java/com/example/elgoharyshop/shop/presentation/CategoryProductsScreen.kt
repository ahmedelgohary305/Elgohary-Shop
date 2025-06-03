package com.example.elgoharyshop.shop.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.elgoharyshop.R
import com.example.elgoharyshop.Routes
import com.example.elgoharyshop.shop.presentation.utils.ErrorUi
import com.example.elgoharyshop.shop.presentation.utils.ProductCard
import com.example.elgoharyshop.shop.presentation.viewmodels.ProductViewModel
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Composable
fun CategoryProductsScreen(
    categoryName: String,
    categoryHandle: String,
    productViewModel: ProductViewModel,
    navController: NavController
) {
    val products = productViewModel.productsByCollection.collectAsStateWithLifecycle().value
    val isLoading by productViewModel.isLoading.collectAsStateWithLifecycle()
    val errorMessage by productViewModel.errorMessage.collectAsStateWithLifecycle()

    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(top = 16.dp, start = 4.dp, end = 4.dp)
        ) {
            IconButton(
                onClick = { navController.popBackStack() }
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }

            Text(
                text = categoryName,
                style = MaterialTheme.typography.headlineMedium,
                fontFamily = FontFamily(Font(R.font.poppins_bold)),
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        Box(modifier = Modifier.fillMaxSize()) {
            when {
                isLoading -> {
                    // Loading UI
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                errorMessage != null -> {
                    // Error UI
                    ErrorUi(productViewModel) {
                        productViewModel.getProductsByCollection(categoryHandle, 20)
                    }
                }
                products.isEmpty() -> {
                    // Empty state UI
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            "No products found in this category",
                            fontFamily = FontFamily(Font(R.font.poppins_semi_bold)),
                            fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(horizontal = 32.dp)
                        )
                    }
                }
                else -> {
                    // Products UI
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.padding(8.dp)
                    ) {
                        items(products) { product ->
                            ProductCard(product = product,
                                onFavoriteClick = {
                                    productViewModel.toggleWishList(product)
                                }
                            ) {
                                val encodedId = URLEncoder.encode(product.id, StandardCharsets.UTF_8.toString())
                                navController.navigate(Routes.ProductDetailsScreen.route + "/$encodedId")
                            }
                        }
                    }
                }
            }
        }
    }
}