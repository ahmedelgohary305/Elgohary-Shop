import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.elgoharyshop.shop.domain.CartItem
import com.example.elgoharyshop.shop.presentation.viewmodels.ProductViewModel
import com.example.elgoharyshop.R
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.elgoharyshop.shop.presentation.utils.ErrorUi

val poppinsBold = FontFamily(Font(R.font.poppins_bold))
val poppinsRegular = FontFamily(Font(R.font.poppins_regular))
val poppinsSemiBold = FontFamily(Font(R.font.poppins_semi_bold))


@Composable
fun CartScreen(productViewModel: ProductViewModel, navController: NavController, context: Context) {
    val cart by productViewModel.cart.collectAsState()
    val isLoading by productViewModel.isLoading.collectAsStateWithLifecycle()
    val errorMessage by productViewModel.errorMessage.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, start = 8.dp)
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Default.ArrowBack,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
            Text(
                text = "Your Cart",
                fontFamily = poppinsBold,
                fontSize = 26.sp,
                color = MaterialTheme.colorScheme.primary
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
                        productViewModel.initializeCart()
                    }
                }
                cart?.items?.isEmpty() != false -> {
                    // Empty cart UI
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.ShoppingCart,
                            contentDescription = null,
                            modifier = Modifier
                                .size(100.dp)
                                .alpha(0.3f),
                            tint = Color.LightGray,
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            "Your cart is empty.",
                            fontFamily = poppinsBold,
                            fontSize = 18.sp,
                            color = Color.Gray
                        )
                    }
                }
                else -> {
                    // Cart content UI
                    Column {
                        LazyColumn(
                            modifier = Modifier.weight(1f)
                        ) {
                            items(cart!!.items) { item ->
                                CartItemRow(
                                    item = item,
                                    onRemove = { productViewModel.removeItem(item.id) },
                                    onQuantityChange = { newQty -> productViewModel.updateItem(item.id, newQty) }
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "Total: ${cart!!.totalPrice} EGP",
                            fontFamily = poppinsSemiBold,
                            fontSize = 20.sp,
                            modifier = Modifier
                                .padding(end = 16.dp)
                                .align(Alignment.End),
                            color = MaterialTheme.colorScheme.primary
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = {
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(cart!!.checkoutUrl))
                                context.startActivity(intent)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 16.dp, end = 16.dp, bottom = 24.dp)
                                .height(50.dp),
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                        ) {
                            Text(
                                "Proceed to Checkout",
                                fontFamily = poppinsSemiBold,
                                fontSize = 16.sp,
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CartItemRow(
    item: CartItem,
    onRemove: () -> Unit,
    onQuantityChange: (Int) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (!item.imageUrl.isNullOrEmpty()) {
                AsyncImage(
                    model = item.imageUrl,
                    contentDescription = null,
                    modifier = Modifier
                        .size(80.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    item.title,
                    fontFamily = poppinsSemiBold,
                    fontSize = 16.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    "Price: ${item.price} EGP",
                    color = MaterialTheme.colorScheme.onSurface.copy(0.5f),
                    fontFamily = poppinsRegular,
                    fontSize = 14.sp
                )
                QuantityChanger(
                    quantity = item.quantity,
                    onQuantityChange = onQuantityChange
                )
            }

            IconButton(onClick = onRemove) {
                Icon(
                    painter = painterResource(R.drawable.delete),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.error.copy(0.7f),
                    modifier = Modifier.size(32.dp)
                )
            }
        }
    }
}

@Composable
fun QuantityChanger(
    quantity: Int,
    onQuantityChange: (Int) -> Unit
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        IconButton(onClick = { if (quantity > 1) onQuantityChange(quantity - 1) }) {
            Icon(
                painter = painterResource(R.drawable.remove),
                contentDescription = null
            )
        }

        Text(
            text = quantity.toString(),
            fontFamily = poppinsBold,
            fontSize = 16.sp,
            modifier = Modifier.padding(horizontal = 4.dp)
        )

        IconButton(onClick = { onQuantityChange(quantity + 1) }) {
            Icon(
                painter = painterResource(R.drawable.add),
                contentDescription = null
            )
        }
    }
}