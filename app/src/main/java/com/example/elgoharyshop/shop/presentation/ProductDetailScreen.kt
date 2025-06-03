package com.example.elgoharyshop.shop.presentation

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.elgoharyshop.R
import com.example.elgoharyshop.Routes
import com.example.elgoharyshop.shop.domain.DetailProduct
import com.example.elgoharyshop.shop.presentation.utils.ErrorUi
import com.example.elgoharyshop.shop.presentation.utils.ProductCard
import com.example.elgoharyshop.shop.presentation.viewmodels.ProductViewModel
import kotlinx.coroutines.launch
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Composable
fun ProductDetailScreen(
    productId: String,
    productViewModel: ProductViewModel,
    navController: NavController
) {
    val currentProduct = productViewModel.currentProduct.collectAsStateWithLifecycle().value
    val isLoading by productViewModel.isLoading.collectAsStateWithLifecycle()

    LaunchedEffect(productId) {
        productViewModel.getProductById(productId)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        when {
            isLoading ->{
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = MaterialTheme.colorScheme.primary
                )
            }

            currentProduct != null && productId == currentProduct.id -> {
                ProductDetail(
                    product = currentProduct,
                    navController = navController,
                    productViewModel = productViewModel
                )
            }

            else -> {
                ErrorUi(productViewModel) {
                    productViewModel.getProductById(productId)
                }
            }
        }
    }
}

@Composable
fun ProductDetail(
    product: DetailProduct,
    navController: NavController,
    productViewModel: ProductViewModel
) {
    val images = product.images
    val isLoading by productViewModel.isLoading.collectAsStateWithLifecycle()

    // Extract variant information
    val firstVariant = product.variants.edges.firstOrNull()?.node

    // Get price from the first variant
    val price = firstVariant?.price?.amount?.let {
        when (it) {
            is Double -> it
            is String -> it.toDoubleOrNull()
            else -> null
        }
    }

    // Get currency code
    val currencyCode = firstVariant?.price?.currencyCode ?: "USD"

    val variantId = firstVariant?.id

    val latestProducts = productViewModel.products.collectAsStateWithLifecycle().value

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(top = 24.dp, start = 8.dp, end = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
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

            IconButton(
                onClick = { navController.navigate(Routes.CartScreen.route) },
                modifier = Modifier.padding(end = 8.dp),
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(0.7f),
                    contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                )
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.cart),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
        }

        if (images.size == 1) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 250.dp, max = 400.dp),
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = images.first(),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth(0.95f)
                        .aspectRatio(1f)
                        .clip(RoundedCornerShape(12.dp))
                )
            }
        } else {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
            ) {
                items(images) { imageUrl ->
                    AsyncImage(
                        model = imageUrl,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .width(250.dp)
                            .clip(RoundedCornerShape(12.dp))
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = product.title,
            style = MaterialTheme.typography.headlineSmall,
            fontFamily = FontFamily(Font(R.font.poppins_bold)),
            color = MaterialTheme.colorScheme.secondary
        )

        Spacer(modifier = Modifier.height(8.dp))

        price?.let {
            Text(
                text = "$currencyCode ${"%.2f".format(it)}",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary,
                fontFamily = FontFamily(Font(R.font.poppins_bold))
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            "Description",
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily(Font(R.font.poppins_regular)),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.secondary
        )
        Spacer(modifier = Modifier.height(4.dp))
        ExpandableText(product.description)

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            "Latest Products",
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily(Font(R.font.poppins_regular)),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.secondary
        )

        if (latestProducts.isEmpty()) {
            Text(
                text = "No related products available",
                style = MaterialTheme.typography.bodyMedium,
                fontFamily = FontFamily(Font(R.font.poppins_regular)),
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                modifier = Modifier.padding(vertical = 16.dp)
            )
        } else {
            LazyRow {
                items(latestProducts) { product ->
                    ProductCard(product = product,
                        onFavoriteClick = {
                            productViewModel.toggleWishList(product)
                        }
                    ) {
                        val encodedId =
                            URLEncoder.encode(product.id, StandardCharsets.UTF_8.toString())
                        navController.popBackStack()
                        navController.navigate(Routes.ProductDetailsScreen.route + "/$encodedId")
                    }
                }
            }
        }

        BottomActionBar(
            productViewModel = productViewModel,
            variantId = variantId,
            context = LocalContext.current,
            isLoading = isLoading
        )
    }
}

@Composable
fun BottomActionBar(
    productViewModel: ProductViewModel,
    variantId: String?,
    context: Context,
    isLoading: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Transparent)
            .padding(8.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Button(
            onClick = {
                variantId?.let {
                    productViewModel.buyNow(it) { checkoutUrl ->
                        checkoutUrl?.let { url ->
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                            context.startActivity(intent)
                        } ?: Toast.makeText(context, "Checkout failed", Toast.LENGTH_SHORT).show()
                    }
                }
            },
            modifier = Modifier.weight(1f),
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .height(20.dp)
                        .width(20.dp),
                    color = MaterialTheme.colorScheme.onPrimary,
                    strokeWidth = 2.dp
                )
            } else {
                Text("Buy Now", fontFamily = FontFamily(Font(R.font.poppins_regular)))
            }
        }

        val context = LocalContext.current
        val coroutineScope = rememberCoroutineScope()

        OutlinedButton(
            onClick = {
                variantId?.let { id ->
                    coroutineScope.launch {
                        productViewModel.addProductToCart(id)
                        Toast.makeText(context, "Added to cart", Toast.LENGTH_SHORT).show()
                    }
                }
            },
            modifier = Modifier.weight(1f),
        ) {
            Text("Add to Cart", fontFamily = FontFamily(Font(R.font.poppins_regular)))
        }
    }
}

@Composable
fun ExpandableText(text: String, minLines: Int = 3) {
    var expanded by remember { mutableStateOf(false) }
    Column {
        Text(
            text = text,
            maxLines = if (expanded) Int.MAX_VALUE else minLines,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.bodyMedium,
            fontFamily = FontFamily(Font(R.font.poppins_regular)),
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
        )
        if (text.length > 150) {
            TextButton(onClick = { expanded = !expanded }) {
                Text(
                    if (expanded) "Show less" else "Read more",
                    fontFamily = FontFamily(Font(R.font.poppins_regular)),
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}