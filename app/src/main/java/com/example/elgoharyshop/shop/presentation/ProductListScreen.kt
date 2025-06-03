package com.example.elgoharyshop.shop.presentation

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.elgoharyshop.GetCustomerQuery.Customer
import com.example.elgoharyshop.R
import com.example.elgoharyshop.Routes
import com.example.elgoharyshop.auth.presentation.AuthViewModel
import com.example.elgoharyshop.shop.presentation.utils.CategoryChip
import com.example.elgoharyshop.shop.presentation.utils.ProductCard
import com.example.elgoharyshop.shop.presentation.utils.TopBar
import com.example.elgoharyshop.shop.presentation.viewmodels.ProductViewModel
import kotlinx.coroutines.launch
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductListScreen(
    productViewModel: ProductViewModel,
    authViewModel: AuthViewModel,
    navController: NavController,
    onLogout: () -> Unit,
    onManageAccount: () -> Unit,
    onToggleTheme: () -> Unit
) {
    val products by productViewModel.products.collectAsStateWithLifecycle()
    val searchQuery by productViewModel.searchQuery.collectAsStateWithLifecycle()
    val isLoading by productViewModel.isLoading.collectAsStateWithLifecycle()
    val customer by authViewModel.customer.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        authViewModel.loadCustomer()
    }

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val categories = listOf(
        CategoryItem("Jewelry", R.drawable.jewelry),
        CategoryItem("Home & Garden", R.drawable.home___garden, "homeandgarden"),
        CategoryItem("Clothing", R.drawable.clothing),
        CategoryItem("Snowboards", R.drawable.snowboards)
    )

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                modifier = Modifier.width(260.dp),
                drawerContainerColor = MaterialTheme.colorScheme.secondaryContainer
            ) {
                Text(
                    text = "Account Settings",
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(16.dp),
                    fontFamily = FontFamily(Font(R.font.poppins_bold))
                )
                HorizontalDivider(
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
                NavigationDrawerItem(
                    label = {
                        Text(
                            "Change Theme",
                            modifier = Modifier.clickable{ onToggleTheme() },
                            color = MaterialTheme.colorScheme.onSecondaryContainer,
                            style = MaterialTheme.typography.bodyLarge,
                            fontFamily = FontFamily(Font(R.font.poppins_regular))
                        )
                    },
                    colors = NavigationDrawerItemDefaults.colors(
                        unselectedContainerColor = Color.Transparent,
                        selectedContainerColor = Color.Transparent
                    ),
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        onToggleTheme()
                    },
                    icon = {
                        Icon(
                            painter = painterResource(R.drawable.theme),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                )
                NavigationDrawerItem(
                    label = {
                        Text(
                            "Manage Account",
                            color = MaterialTheme.colorScheme.onSecondaryContainer,
                            style = MaterialTheme.typography.bodyLarge,
                            fontFamily = FontFamily(Font(R.font.poppins_regular))
                        )
                    },
                    colors = NavigationDrawerItemDefaults.colors(
                        unselectedContainerColor = Color.Transparent,
                        selectedContainerColor = Color.Transparent
                    ),
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        onManageAccount()
                    },
                    icon = {
                        Icon(
                            painter = painterResource(R.drawable.account),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                )
                NavigationDrawerItem(
                    label = {
                        Text(
                            "Log Out",
                            color = MaterialTheme.colorScheme.onSecondaryContainer,
                            style = MaterialTheme.typography.bodyLarge,
                            fontFamily = FontFamily(Font(R.font.poppins_regular))
                        )
                    },
                    colors = NavigationDrawerItemDefaults.colors(
                        unselectedContainerColor = Color.Transparent,
                        selectedContainerColor = Color.Transparent
                    ),
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        onLogout()
                    },
                    icon = {
                        Icon(
                            painter = painterResource(R.drawable.logout),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                )
            }
        }
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp)
            ) {
                customer?.let {
                    UserDataSection(customer = it)
                }

                TopBar(
                    searchQuery = searchQuery,
                    productViewModel = productViewModel,
                    navController = navController,
                    onDrawerClick = {
                        scope.launch { drawerState.open() }
                    }
                )

                Text(
                    text = "Categories",
                    style = MaterialTheme.typography.bodyLarge,
                    fontFamily = FontFamily(Font(R.font.poppins_bold)),
                    color = MaterialTheme.colorScheme.onBackground
                )
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(vertical = 8.dp)
                ) {
                    items(categories) { category ->
                        CategoryChip(
                            label = category.label,
                            icon = category.icon,
                            onClick = {
                                productViewModel.getProductsByCollection(category.handle, 20)
                                navController.navigate(
                                    Routes.CategoryProductsScreen.route + "/${category.label}" + "/${category.handle}"
                                )
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Latest Products",
                    style = MaterialTheme.typography.bodyLarge,
                    fontFamily = FontFamily(Font(R.font.poppins_bold)),
                    color = MaterialTheme.colorScheme.onBackground
                )

                if (isLoading) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                } else if (products.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "No products available, Check Your Internet",
                                style = MaterialTheme.typography.bodyLarge,
                                fontFamily = FontFamily(Font(R.font.poppins_regular)),
                                textAlign = TextAlign.Center,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            IconButton(
                                onClick = { productViewModel.fetchProducts() },
                                modifier = Modifier.size(32.dp)
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.refresh),
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.fillMaxSize()
                                )
                            }
                        }
                    }
                } else {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.padding(top = 8.dp)
                    ) {
                        items(products) { product ->
                            ProductCard(
                                product = product,
                                onFavoriteClick = {
                                    productViewModel.toggleWishList(product)
                                }
                            ) {
                                val encodedId = URLEncoder.encode(
                                    product.id,
                                    StandardCharsets.UTF_8.toString()
                                )
                                navController.navigate(Routes.ProductDetailsScreen.route + "/$encodedId")
                            }
                        }
                    }
                }
            }
        }
    }
}

data class CategoryItem(
    val label: String,
    @DrawableRes val icon: Int,
    val handle: String = label.lowercase().replace("&", "and").replace(" ", "-")
)

@Composable
fun UserDataSection(
    customer: Customer,
) {
        Row(
            modifier = Modifier
                .padding(top = 24.dp, start = 8.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.user__1_),
                contentDescription = "User Image",
                modifier = Modifier
                    .size(38.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = "${customer.firstName.orEmpty()} ${customer.lastName.orEmpty()}",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontFamily = FontFamily(Font(R.font.poppins_bold)),
                )
                Text(
                    text = customer.email.orEmpty(),
                    style = MaterialTheme.typography.bodyMedium,
                    fontFamily = FontFamily(Font(R.font.poppins_regular)),
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }
        }
}
