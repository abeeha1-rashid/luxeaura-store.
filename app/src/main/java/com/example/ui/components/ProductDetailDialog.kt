package com.example.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.R
import com.example.data.model.Product
import com.example.ui.theme.*
import java.util.Locale

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ProductDetailDialog(
    product: Product,
    isWishlisted: Boolean,
    onDismiss: () -> Unit,
    onWishlistToggle: (Product) -> Unit,
    onAddToCart: (Product, Int, String) -> Unit
) {
    val context = LocalContext.current
    var selectedQuantity by remember { mutableIntStateOf(1) }

    val optionsList = remember(product.options) {
        product.options.split(",").map { it.trim() }.filter { it.isNotEmpty() }
    }
    var selectedOption by remember {
        mutableStateOf(optionsList.firstOrNull() ?: "Standard")
    }

    val hasDiscount = product.discountPrice != null && product.discountPrice < product.price
    val displayPrice = product.discountPrice ?: product.price
    val discountPercent = if (hasDiscount) {
        (((product.price - product.discountPrice!!) / product.price) * 100).toInt()
    } else 0

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = LuxeWhite,
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .fillMaxHeight(0.88f)
                .border(1.dp, LuxeGoldBorder, RoundedCornerShape(24.dp))
                .shadow(16.dp, RoundedCornerShape(24.dp))
                .testTag("product_detail_dialog")
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                // Header Bar with Close & Wishlist
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.size(36.dp).testTag("close_detail_dialog")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = LuxeCharcoal
                        )
                    }

                    Text(
                        text = "LUXURY SPECIFICATION",
                        fontFamily = FontFamily.Default,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 11.sp,
                        letterSpacing = 1.5.sp,
                        color = LuxeGoldDark
                    )

                    IconButton(
                        onClick = { onWishlistToggle(product) },
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(LuxeGoldGlow)
                    ) {
                        Icon(
                            imageVector = if (isWishlisted) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                            contentDescription = "Wishlist",
                            tint = if (isWishlisted) LuxeHeartRed else LuxeCharcoal,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                Divider(color = LuxeGoldBorder.copy(alpha = 0.4f))

                // Scrollable Content
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                        .padding(20.dp)
                ) {
                    // Large Hero Image
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(240.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(LuxeCream)
                    ) {
                        val drawableRes = when (product.imageUrl) {
                            "jewellery_banner" -> R.drawable.jewellery_banner
                            "watches_banner" -> R.drawable.watches_banner
                            "hero_showcase" -> R.drawable.hero_showcase
                            "luxeaura_logo" -> R.drawable.luxeaura_logo
                            else -> null
                        }

                        if (drawableRes != null) {
                            Image(
                                painter = painterResource(id = drawableRes),
                                contentDescription = product.name,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                        } else if (product.imageUrl.startsWith("http")) {
                            AsyncImage(
                                model = ImageRequest.Builder(context)
                                    .data(product.imageUrl)
                                    .crossfade(true)
                                    .build(),
                                placeholder = painterResource(id = R.drawable.luxeaura_logo),
                                error = painterResource(id = R.drawable.jewellery_banner),
                                contentDescription = product.name,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                        } else {
                            Image(
                                painter = painterResource(
                                    id = if (product.category == "Watches") R.drawable.watches_banner else R.drawable.jewellery_banner
                                ),
                                contentDescription = product.name,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                        }

                        if (hasDiscount) {
                            Box(
                                modifier = Modifier
                                    .align(Alignment.TopStart)
                                    .padding(12.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(LuxeDiscountBadge)
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = "SAVE $discountPercent%",
                                    color = LuxeWhite,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Category Tag & Rating
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "${product.category.uppercase()}${if (product.subCategory != "General") " • " + product.subCategory.uppercase() else ""}",
                            fontFamily = FontFamily.Default,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp,
                            letterSpacing = 1.5.sp,
                            color = LuxeGoldDark
                        )

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null,
                                tint = LuxeGold,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = "${product.rating} (${product.reviewsCount} reviews)",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = LuxeTextDark
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Title
                    Text(
                        text = product.name,
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        lineHeight = 26.sp,
                        color = LuxeCharcoal
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Price Section
                    Row(
                        verticalAlignment = Alignment.Bottom,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "PKR ${String.format(Locale.US, "%,.0f", displayPrice)}",
                            fontFamily = FontFamily.Default,
                            fontWeight = FontWeight.Bold,
                            fontSize = 22.sp,
                            color = LuxeGoldDark
                        )

                        if (hasDiscount) {
                            Text(
                                text = "PKR ${String.format(Locale.US, "%,.0f", product.price)}",
                                fontFamily = FontFamily.Default,
                                fontWeight = FontWeight.Normal,
                                fontSize = 15.sp,
                                color = LuxeTextLight,
                                textDecoration = TextDecoration.LineThrough
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Description
                    Text(
                        text = "Description",
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp,
                        color = LuxeCharcoal
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = product.description,
                        fontFamily = FontFamily.Default,
                        fontSize = 13.5.sp,
                        lineHeight = 20.sp,
                        color = LuxeTextDark
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Available Options Selection
                    if (optionsList.isNotEmpty()) {
                        Text(
                            text = "Select Edition / Option",
                            fontFamily = FontFamily.Serif,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 14.sp,
                            color = LuxeCharcoal
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            optionsList.forEach { opt ->
                                val isSelected = selectedOption == opt
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(if (isSelected) LuxeGoldLight else LuxeOffWhite)
                                        .border(
                                            width = 1.dp,
                                            color = if (isSelected) LuxeGoldDark else LuxeGoldBorder,
                                            shape = RoundedCornerShape(8.dp)
                                        )
                                        .clickable { selectedOption = opt }
                                        .padding(horizontal = 12.dp, vertical = 8.dp)
                                ) {
                                    Text(
                                        text = opt,
                                        fontSize = 12.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                        color = if (isSelected) LuxeCharcoal else LuxeTextDark
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))
                    }

                    // Quantity Selector
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Quantity",
                            fontFamily = FontFamily.Serif,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 14.sp,
                            color = LuxeCharcoal
                        )

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(LuxeCream)
                                .border(1.dp, LuxeGoldBorder, RoundedCornerShape(8.dp))
                                .padding(4.dp)
                        ) {
                            IconButton(
                                onClick = { if (selectedQuantity > 1) selectedQuantity-- },
                                modifier = Modifier.size(28.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Remove,
                                    contentDescription = "Decrease",
                                    tint = LuxeCharcoal,
                                    modifier = Modifier.size(14.dp)
                                )
                            }

                            Text(
                                text = selectedQuantity.toString(),
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = LuxeCharcoal,
                                modifier = Modifier.padding(horizontal = 12.dp)
                            )

                            IconButton(
                                onClick = { selectedQuantity++ },
                                modifier = Modifier.size(28.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = "Increase",
                                    tint = LuxeCharcoal,
                                    modifier = Modifier.size(14.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Stock info card
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(LuxeGoldGlow)
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = if (product.isAvailable) Icons.Default.CheckCircle else Icons.Default.Info,
                            contentDescription = null,
                            tint = if (product.isAvailable) LuxeSuccess else LuxeDiscountBadge,
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = if (product.isAvailable)
                                "In Stock & Ready to Ship (2-4 Business Days across Pakistan)"
                            else
                                "Currently Sold Out — Join Waitlist via WhatsApp",
                            fontSize = 12.sp,
                            color = LuxeTextDark
                        )
                    }
                }

                // Footer CTA Buttons
                Surface(
                    color = LuxeWhite,
                    shadowElevation = 8.dp,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // Wishlist secondary button
                        OutlinedButton(
                            onClick = { onWishlistToggle(product) },
                            shape = RoundedCornerShape(12.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, LuxeGold),
                            modifier = Modifier
                                .weight(0.35f)
                                .height(48.dp)
                        ) {
                            Icon(
                                imageVector = if (isWishlisted) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                                contentDescription = null,
                                tint = if (isWishlisted) LuxeHeartRed else LuxeGoldDark,
                                modifier = Modifier.size(18.dp)
                            )
                        }

                        // Add to Cart primary button
                        Button(
                            onClick = {
                                onAddToCart(product, selectedQuantity, selectedOption)
                                onDismiss()
                            },
                            enabled = product.isAvailable,
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = LuxeGold,
                                contentColor = LuxeWhite
                            ),
                            modifier = Modifier
                                .weight(0.65f)
                                .height(48.dp)
                                .testTag("modal_add_to_cart_button")
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ShoppingBag,
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Add to Cart • PKR ${String.format(Locale.US, "%,.0f", displayPrice * selectedQuantity)}",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
