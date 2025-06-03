package com.example.elgoharyshop.shop.presentation

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.elgoharyshop.R
import com.example.elgoharyshop.Routes
import com.example.elgoharyshop.shop.domain.UiProduct
import com.example.elgoharyshop.shop.presentation.utils.ProductCard
import com.example.elgoharyshop.shop.presentation.viewmodels.ProductViewModel
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun WishListScreen(
    productViewModel: ProductViewModel,
    navController: NavController
) {
    val wishLists by productViewModel.wishLists.collectAsStateWithLifecycle()

    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier.padding(top = 24.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { navController.popBackStack() }
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }

            Text(
                text = "My Wishlist",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.primary,
                fontFamily = FontFamily(Font(R.font.poppins_bold)),
            )
        }

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            modifier = Modifier
                .fillMaxSize()
        ) {
            items(wishLists, key = { it.id }) { product ->
                WishlistProductCard(
                    product = product,
                    modifier = Modifier.animateItemPlacement(),
                    productViewModel = productViewModel,
                    navController = navController
                )
            }
        }
    }
}

@Composable
fun WishlistProductCard(
    product: UiProduct,
    modifier: Modifier,
    productViewModel: ProductViewModel,
    navController: NavController
) {
    Box(
        modifier = modifier
    ) {
        ProductCard(
            product = product,
            onFavoriteClick = {
                productViewModel.toggleWishList(product)
            }
        ) {
            val encodedId =
                URLEncoder.encode(product.id, StandardCharsets.UTF_8.toString())
            navController.navigate(Routes.ProductDetailsScreen.route + "/$encodedId")
        }

        IconButton(
            onClick = {
                productViewModel.deleteWishList(product.id)
            },
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(12.dp)
                .size(32.dp),
            colors = IconButtonDefaults.iconButtonColors(
                containerColor = MaterialTheme.colorScheme.errorContainer.copy(0.5f),
                contentColor = MaterialTheme.colorScheme.onErrorContainer
            )
        ) {
            Icon(
                painter = painterResource(id = R.drawable.delete),
                contentDescription = null,
            )
        }
    }
}
