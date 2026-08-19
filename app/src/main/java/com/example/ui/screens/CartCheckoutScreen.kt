package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Chat
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.ShoppingBag
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.R
import com.example.data.model.CartItemWithProduct
import com.example.data.model.Order
import com.example.ui.LuxeTab
import com.example.ui.components.LuxeFooter
import com.example.ui.theme.*
import java.util.Locale

@Composable
fun CartCheckoutScreen(
    cartItems: List<CartItemWithProduct>,
    lastPlacedOrder: Order?,
    onUpdateQuantity: (CartItemWithProduct, Int) -> Unit,
    onRemoveCartItem: (CartItemWithProduct) -> Unit,
    onClearCart: () -> Unit,
    onPlaceOrder: (String, String, String, String, String, Double) -> Unit,
    onDismissOrderConfirmation: () -> Unit,
    onNavigateTo: (LuxeTab) -> Unit,
    onOpenAdminLogin: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var isCheckingOut by remember { mutableStateOf(false) }

    // Checkout Form fields
    var customerName by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var whatsappNumber by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("Karachi") }
    var isCityMenuOpen by remember { mutableStateOf(false) }

    val majorPakistaniCities = listOf(
        "Karachi", "Lahore", "Islamabad", "Rawalpindi",
        "Faisalabad", "Multan", "Peshawar", "Quetta",
        "Sialkot", "Bahawalpur", "Gujranwala", "Hyderabad"
    )

    val subtotal = cartItems.sumOf {
        (it.product.discountPrice ?: it.product.price) * it.cartItem.quantity
    }
    val shippingFee = if (subtotal > 20000 || subtotal == 0.0) 0.0 else 500.0
    val totalAmount = subtotal + shippingFee

    LazyColumn(
        contentPadding = PaddingValues(bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
        modifier = modifier
            .fillMaxSize()
            .background(LuxeWhite)
            .testTag("cart_screen")
    ) {
        // Header
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, start = 16.dp, end = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "YOUR SHOPPING BAG",
                    fontFamily = FontFamily.Default,
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.sp,
                    letterSpacing = 2.sp,
                    color = LuxeGoldDark
                )
                Text(
                    text = "Shopping Cart 🛒",
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp,
                    color = LuxeCharcoal
                )
                Text(
                    text = "${cartItems.sumOf { it.cartItem.quantity }} items in your luxury bag",
                    fontSize = 12.sp,
                    color = LuxeTextMuted
                )
            }
        }

        // Empty Cart State or List
        if (cartItems.isEmpty()) {
            item {
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = LuxeOffWhite),
                    border = androidx.compose.foundation.BorderStroke(1.dp, LuxeGoldBorder),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 20.dp)
                        .testTag("empty_cart_card")
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .size(72.dp)
                                .clip(CircleShape)
                                .background(LuxeGoldGlow)
                                .border(1.dp, LuxeGoldBorder, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.ShoppingBag,
                                contentDescription = null,
                                tint = LuxeGoldDark,
                                modifier = Modifier.size(36.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "Your Cart is Currently Empty",
                            fontFamily = FontFamily.Serif,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = LuxeCharcoal
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "Add luxury jewellery or watches to begin your exquisite shopping journey with LuxeAura.",
                            fontSize = 12.5.sp,
                            lineHeight = 18.sp,
                            color = LuxeTextMuted,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        Button(
                            onClick = { onNavigateTo(LuxeTab.HOME) },
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = LuxeGold,
                                contentColor = LuxeWhite
                            )
                        ) {
                            Text("Start Shopping", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        } else {
            // Cart Items
            items(
                items = cartItems,
                key = { it.cartItem.id }
            ) { item ->
                CartItemRow(
                    item = item,
                    onUpdateQuantity = { delta -> onUpdateQuantity(item, delta) },
                    onRemove = { onRemoveCartItem(item) },
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }

            // Price Breakdown Summary Card
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = LuxeOffWhite),
                    border = androidx.compose.foundation.BorderStroke(1.dp, LuxeGoldBorder),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "ORDER SUMMARY",
                            fontFamily = FontFamily.Default,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp,
                            letterSpacing = 1.5.sp,
                            color = LuxeGoldDark
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "Subtotal", fontSize = 13.sp, color = LuxeTextDark)
                            Text(
                                text = "PKR ${String.format(Locale.US, "%,.0f", subtotal)}",
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 13.sp,
                                color = LuxeCharcoal
                            )
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "Insured Delivery", fontSize = 13.sp, color = LuxeTextDark)
                            Text(
                                text = if (shippingFee == 0.0) "FREE (Luxury Privilege)" else "PKR 500",
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 13.sp,
                                color = if (shippingFee == 0.0) LuxeSuccess else LuxeCharcoal
                            )
                        }

                        Divider(color = LuxeGoldBorder.copy(alpha = 0.6f))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Total Amount",
                                fontFamily = FontFamily.Serif,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = LuxeCharcoal
                            )
                            Text(
                                text = "PKR ${String.format(Locale.US, "%,.0f", totalAmount)}",
                                fontFamily = FontFamily.Default,
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                color = LuxeGoldDark
                            )
                        }
                    }
                }
            }

            // Checkout Form Toggle or Section
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = LuxeWhite),
                    border = androidx.compose.foundation.BorderStroke(1.dp, LuxeGoldBorder),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .testTag("checkout_form_card")
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(18.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "DELIVERY & CHECKOUT",
                                fontFamily = FontFamily.Default,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp,
                                letterSpacing = 1.5.sp,
                                color = LuxeGoldDark
                            )

                            Text(
                                text = "Cash on Delivery Available",
                                fontSize = 10.5.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = LuxeSuccess
                            )
                        }

                        // Customer Name Field
                        OutlinedTextField(
                            value = customerName,
                            onValueChange = { customerName = it },
                            label = { Text("Customer Full Name *") },
                            placeholder = { Text("e.g. Fatima Ali") },
                            singleLine = true,
                            shape = RoundedCornerShape(10.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = LuxeGold,
                                unfocusedBorderColor = LuxeGoldBorder
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("checkout_name_input")
                        )

                        // Phone Number
                        OutlinedTextField(
                            value = phone,
                            onValueChange = { phone = it },
                            label = { Text("Phone Number *") },
                            placeholder = { Text("03001234567") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                            singleLine = true,
                            shape = RoundedCornerShape(10.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = LuxeGold,
                                unfocusedBorderColor = LuxeGoldBorder
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("checkout_phone_input")
                        )

                        // WhatsApp Number
                        OutlinedTextField(
                            value = whatsappNumber,
                            onValueChange = { whatsappNumber = it },
                            label = { Text("WhatsApp Number (for Order Tracking) *") },
                            placeholder = { Text("03340634595") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                            singleLine = true,
                            shape = RoundedCornerShape(10.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = LuxeGold,
                                unfocusedBorderColor = LuxeGoldBorder
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("checkout_whatsapp_input")
                        )

                        // City Selection Dropdown
                        Box(modifier = Modifier.fillMaxWidth()) {
                            OutlinedTextField(
                                value = city,
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("City *") },
                                trailingIcon = {
                                    IconButton(onClick = { isCityMenuOpen = true }) {
                                        Icon(
                                            imageVector = Icons.Default.ArrowDropDown,
                                            contentDescription = "Select City",
                                            tint = LuxeCharcoal
                                        )
                                    }
                                },
                                shape = RoundedCornerShape(10.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = LuxeGold,
                                    unfocusedBorderColor = LuxeGoldBorder
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { isCityMenuOpen = true }
                            )

                            DropdownMenu(
                                expanded = isCityMenuOpen,
                                onDismissRequest = { isCityMenuOpen = false },
                                modifier = Modifier.background(LuxeWhite)
                            ) {
                                majorPakistaniCities.forEach { cityName ->
                                    DropdownMenuItem(
                                        text = { Text(cityName, fontSize = 13.sp) },
                                        onClick = {
                                            city = cityName
                                            isCityMenuOpen = false
                                        }
                                    )
                                }
                            }
                        }

                        // Complete Address
                        OutlinedTextField(
                            value = address,
                            onValueChange = { address = it },
                            label = { Text("Complete Delivery Address *") },
                            placeholder = { Text("House / Apartment #, Street, Area, Sector...") },
                            maxLines = 3,
                            shape = RoundedCornerShape(10.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = LuxeGold,
                                unfocusedBorderColor = LuxeGoldBorder
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("checkout_address_input")
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        // Place Order Button
                        val isFormValid = customerName.isNotBlank() &&
                                phone.isNotBlank() &&
                                address.isNotBlank()

                        Button(
                            onClick = {
                                onPlaceOrder(
                                    customerName.trim(),
                                    phone.trim(),
                                    if (whatsappNumber.isBlank()) phone.trim() else whatsappNumber.trim(),
                                    address.trim(),
                                    city.trim(),
                                    totalAmount
                                )
                            },
                            enabled = isFormValid,
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = LuxeGold,
                                contentColor = LuxeWhite,
                                disabledContainerColor = LuxeTextLight.copy(alpha = 0.3f)
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .testTag("place_order_button")
                        ) {
                            Text(
                                text = "Confirm & Place Luxury Order (PKR ${String.format(Locale.US, "%,.0f", totalAmount)})",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }

        // Footer
        item {
            LuxeFooter(
                onNavigateTo = onNavigateTo,
                onOpenAdminLogin = onOpenAdminLogin,
                modifier = Modifier.padding(top = 16.dp)
            )
        }
    }

    // Beautiful Order Confirmation Dialog
    if (lastPlacedOrder != null) {
        Dialog(
            onDismissRequest = onDismissOrderConfirmation,
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            Surface(
                shape = RoundedCornerShape(24.dp),
                color = LuxeWhite,
                modifier = Modifier
                    .fillMaxWidth(0.92f)
                    .border(1.dp, LuxeGoldBorder, RoundedCornerShape(24.dp))
                    .padding(4.dp)
                    .testTag("order_confirmation_dialog")
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .clip(CircleShape)
                            .background(LuxeGoldLight),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = LuxeSuccess,
                            modifier = Modifier.size(36.dp)
                        )
                    }

                    Text(
                        text = "Order Placed Successfully!",
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = LuxeCharcoal,
                        textAlign = TextAlign.Center
                    )

                    Text(
                        text = "Thank you, ${lastPlacedOrder.customerName}. Your luxury order has been received and is being prepared with exquisite care.",
                        fontSize = 12.5.sp,
                        lineHeight = 18.sp,
                        color = LuxeTextDark,
                        textAlign = TextAlign.Center
                    )

                    // Order Details Box
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = LuxeOffWhite),
                        border = androidx.compose.foundation.BorderStroke(1.dp, LuxeGoldBorder),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                text = "ORDER ID: ${lastPlacedOrder.id}",
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                color = LuxeGoldDark
                            )
                            Text(
                                text = "Date: ${lastPlacedOrder.orderDate}",
                                fontSize = 11.5.sp,
                                color = LuxeTextMuted
                            )
                            Text(
                                text = "Delivery to: ${lastPlacedOrder.address}, ${lastPlacedOrder.city}",
                                fontSize = 11.5.sp,
                                color = LuxeTextDark
                            )
                            Divider(color = LuxeGoldBorder.copy(alpha = 0.5f), modifier = Modifier.padding(vertical = 4.dp))
                            Text(
                                text = "Total (Cash on Delivery): PKR ${String.format(Locale.US, "%,.0f", lastPlacedOrder.totalAmount)}",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = LuxeCharcoal
                            )
                        }
                    }

                    // Direct WhatsApp Confirmation Button
                    Button(
                        onClick = {
                            val msg = "Hello LuxeAura.pk! I have placed Order ${lastPlacedOrder.id} for PKR ${String.format(Locale.US, "%,.0f", lastPlacedOrder.totalAmount)}. Customer: ${lastPlacedOrder.customerName}, Address: ${lastPlacedOrder.address}, ${lastPlacedOrder.city}."
                            val url = "https://wa.me/923340634595?text=${Uri.encode(msg)}"
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                            try {
                                context.startActivity(intent)
                            } catch (e: Exception) {
                                // fallback
                            }
                        },
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = LuxeSuccess,
                            contentColor = LuxeWhite
                        ),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Chat,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Track & Chat on WhatsApp", fontSize = 12.5.sp, fontWeight = FontWeight.Bold)
                    }

                    OutlinedButton(
                        onClick = {
                            onDismissOrderConfirmation()
                            onNavigateTo(LuxeTab.HOME)
                        },
                        shape = RoundedCornerShape(10.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, LuxeGoldBorder),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Continue Exploring LuxeAura", fontSize = 12.sp, color = LuxeCharcoal)
                    }
                }
            }
        }
    }
}

@Composable
private fun CartItemRow(
    item: CartItemWithProduct,
    onUpdateQuantity: (Int) -> Unit,
    onRemove: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val product = item.product
    val effectivePrice = product.discountPrice ?: product.price

    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = LuxeWhite),
        border = androidx.compose.foundation.BorderStroke(1.dp, LuxeGoldBorder.copy(alpha = 0.7f)),
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Thumbnail Image
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .clip(RoundedCornerShape(10.dp))
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
            }

            // Info & Quantity
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = product.name,
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 13.5.sp,
                    maxLines = 1,
                    color = LuxeCharcoal
                )

                Text(
                    text = "Option: ${item.cartItem.selectedOption}",
                    fontSize = 11.sp,
                    color = LuxeGoldDark
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "PKR ${String.format(Locale.US, "%,.0f", effectivePrice * item.cartItem.quantity)}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = LuxeCharcoal
                )
            }

            // Quantity buttons & Delete
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                IconButton(
                    onClick = onRemove,
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Delete,
                        contentDescription = "Remove",
                        tint = LuxeDiscountBadge,
                        modifier = Modifier.size(16.dp)
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(LuxeCream)
                        .padding(horizontal = 4.dp, vertical = 2.dp)
                ) {
                    IconButton(
                        onClick = { onUpdateQuantity(-1) },
                        modifier = Modifier.size(22.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Remove,
                            contentDescription = "Decrease",
                            tint = LuxeCharcoal,
                            modifier = Modifier.size(12.dp)
                        )
                    }

                    Text(
                        text = item.cartItem.quantity.toString(),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = LuxeCharcoal,
                        modifier = Modifier.padding(horizontal = 6.dp)
                    )

                    IconButton(
                        onClick = { onUpdateQuantity(1) },
                        modifier = Modifier.size(22.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Increase",
                            tint = LuxeCharcoal,
                            modifier = Modifier.size(12.dp)
                        )
                    }
                }
            }
        }
    }
}
