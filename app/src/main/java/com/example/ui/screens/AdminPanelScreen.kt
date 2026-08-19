package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.model.Order
import com.example.data.model.Product
import com.example.ui.LuxeTab
import com.example.ui.components.LuxeFooter
import com.example.ui.theme.*
import java.util.Locale

enum class AdminSection {
    PRODUCTS,
    CATEGORIES,
    ORDERS
}

@Composable
fun AdminPanelScreen(
    isAuthenticated: Boolean,
    loginError: String?,
    allProducts: List<Product>,
    allOrders: List<Order>,
    onLogin: (String, String) -> Boolean,
    onLogout: () -> Unit,
    onSaveProduct: (Long, String, String, String, Double, Double?, String, String, String, Boolean, Boolean) -> Unit,
    onDeleteProduct: (Long) -> Unit,
    onUpdateOrderStatus: (String, String) -> Unit,
    onResetCatalog: () -> Unit,
    onNavigateTo: (LuxeTab) -> Unit,
    modifier: Modifier = Modifier
) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }

    var selectedAdminSection by remember { mutableStateOf(AdminSection.PRODUCTS) }
    var editingProduct by remember { mutableStateOf<Product?>(null) }
    var isAddProductDialogOpen by remember { mutableStateOf(false) }

    if (!isAuthenticated) {
        // Secure Login Form
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(LuxeCream)
                .padding(20.dp),
            contentAlignment = Alignment.Center
        ) {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = LuxeWhite),
                border = androidx.compose.foundation.BorderStroke(1.5.dp, LuxeGoldBorder),
                modifier = Modifier
                    .fillMaxWidth(0.95f)
                    .testTag("admin_login_card")
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .clip(CircleShape)
                            .background(LuxeGoldLight),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.AdminPanelSettings,
                            contentDescription = null,
                            tint = LuxeGoldDark,
                            modifier = Modifier.size(30.dp)
                        )
                    }

                    Text(
                        text = "LuxeAura Management",
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = LuxeCharcoal
                    )

                    Text(
                        text = "Authorized Personnel Portal",
                        fontSize = 12.sp,
                        color = LuxeTextMuted
                    )

                    if (loginError != null) {
                        Text(
                            text = loginError,
                            color = LuxeDiscountBadge,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    // Username field
                    OutlinedTextField(
                        value = username,
                        onValueChange = { username = it },
                        label = { Text("Username") },
                        placeholder = { Text("Enter admin username") },
                        singleLine = true,
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = null,
                                tint = LuxeGoldDark
                            )
                        },
                        shape = RoundedCornerShape(10.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = LuxeGold,
                            unfocusedBorderColor = LuxeGoldBorder
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("admin_username_field")
                    )

                    // Password field
                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Password") },
                        placeholder = { Text("Enter secure password") },
                        singleLine = true,
                        visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = null,
                                tint = LuxeGoldDark
                            )
                        },
                        trailingIcon = {
                            IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                                Icon(
                                    imageVector = if (isPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                    contentDescription = "Toggle Password"
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
                            .testTag("admin_password_field")
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Button(
                        onClick = {
                            onLogin(username, password)
                        },
                        enabled = username.isNotBlank() && password.isNotBlank(),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = LuxeGold,
                            contentColor = LuxeWhite
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("admin_login_submit_btn")
                    ) {
                        Text("Access Dashboard", fontSize = 13.5.sp, fontWeight = FontWeight.Bold)
                    }

                    TextButton(
                        onClick = { onNavigateTo(LuxeTab.HOME) }
                    ) {
                        Text("Return to Public Boutique", fontSize = 12.sp, color = LuxeGoldDark)
                    }
                }
            }
        }
    } else {
        // Authenticated Admin Dashboard
        LazyColumn(
            contentPadding = PaddingValues(bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
            modifier = modifier
                .fillMaxSize()
                .background(LuxeWhite)
                .testTag("admin_dashboard_view")
        ) {
            // Admin Top Header
            item {
                Surface(
                    color = LuxeCharcoal,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "LuxeAura.pk Admin",
                                fontFamily = FontFamily.Serif,
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                color = LuxeGoldLight
                            )
                            Text(
                                text = "Logged in as Store Administrator",
                                fontSize = 11.sp,
                                color = LuxeTextLight
                            )
                        }

                        Button(
                            onClick = onLogout,
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = LuxeGoldDark,
                                contentColor = LuxeWhite
                            ),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Logout,
                                contentDescription = null,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Logout", fontSize = 11.5.sp, fontWeight = FontWeight.SemiBold)
                        }
                    }
                }
            }

            // Navigation Sections (Products, Categories, Orders)
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    AdminSectionTab(
                        label = "Products (${allProducts.size})",
                        isSelected = selectedAdminSection == AdminSection.PRODUCTS,
                        onClick = { selectedAdminSection = AdminSection.PRODUCTS },
                        modifier = Modifier.weight(1f)
                    )
                    AdminSectionTab(
                        label = "Categories",
                        isSelected = selectedAdminSection == AdminSection.CATEGORIES,
                        onClick = { selectedAdminSection = AdminSection.CATEGORIES },
                        modifier = Modifier.weight(1f)
                    )
                    AdminSectionTab(
                        label = "Orders (${allOrders.size})",
                        isSelected = selectedAdminSection == AdminSection.ORDERS,
                        onClick = { selectedAdminSection = AdminSection.ORDERS },
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            when (selectedAdminSection) {
                AdminSection.PRODUCTS -> {
                    // Action Bar: Add Product & Reset catalog
                    item {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Button(
                                onClick = {
                                    editingProduct = null
                                    isAddProductDialogOpen = true
                                },
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = LuxeGold,
                                    contentColor = LuxeWhite
                                ),
                                modifier = Modifier.testTag("admin_add_product_btn")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Add New Product", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }

                            OutlinedButton(
                                onClick = onResetCatalog,
                                shape = RoundedCornerShape(10.dp),
                                border = androidx.compose.foundation.BorderStroke(1.dp, LuxeGoldBorder)
                            ) {
                                Text("Reset Defaults", fontSize = 11.sp, color = LuxeTextDark)
                            }
                        }
                    }

                    // Products list
                    items(
                        items = allProducts,
                        key = { it.id }
                    ) { prod ->
                        AdminProductRow(
                            product = prod,
                            onEdit = {
                                editingProduct = prod
                                isAddProductDialogOpen = true
                            },
                            onDelete = { onDeleteProduct(prod.id) },
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                    }
                }

                AdminSection.CATEGORIES -> {
                    item {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Text(
                                text = "STORE CATEGORIES OVERVIEW",
                                fontFamily = FontFamily.Default,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp,
                                letterSpacing = 1.5.sp,
                                color = LuxeGoldDark
                            )

                            // Jewellery Category Card
                            CategorySummaryCard(
                                title = "Jewellery",
                                subtitle = "Solitaires, Necklaces, Chokers, Bangles, Rings",
                                count = allProducts.count { it.category == "Jewellery" }
                            )

                            // Watches Parent Category Card
                            CategorySummaryCard(
                                title = "Watches - All",
                                subtitle = "Luxury horology timepieces",
                                count = allProducts.count { it.category == "Watches" }
                            )

                            // Subcategories
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                SubCategoryCard(
                                    title = "Women",
                                    count = allProducts.count { it.category == "Watches" && it.subCategory == "Women" },
                                    modifier = Modifier.weight(1f)
                                )
                                SubCategoryCard(
                                    title = "Men",
                                    count = allProducts.count { it.category == "Watches" && it.subCategory == "Men" },
                                    modifier = Modifier.weight(1f)
                                )
                                SubCategoryCard(
                                    title = "Unisex",
                                    count = allProducts.count { it.category == "Watches" && it.subCategory == "Unisex" },
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }
                }

                AdminSection.ORDERS -> {
                    if (allOrders.isEmpty()) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(32.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "No customer orders have been placed yet.",
                                    fontSize = 13.sp,
                                    color = LuxeTextMuted,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    } else {
                        items(
                            items = allOrders,
                            key = { it.id }
                        ) { order ->
                            AdminOrderCard(
                                order = order,
                                onUpdateStatus = { newStatus ->
                                    onUpdateOrderStatus(order.id, newStatus)
                                },
                                modifier = Modifier.padding(horizontal = 16.dp)
                            )
                        }
                    }
                }
            }

            // Footer
            item {
                LuxeFooter(
                    onNavigateTo = onNavigateTo,
                    onOpenAdminLogin = {},
                    modifier = Modifier.padding(top = 20.dp)
                )
            }
        }

        // Add / Edit Product Dialog
        if (isAddProductDialogOpen) {
            ProductEditModalDialog(
                productToEdit = editingProduct,
                onDismiss = { isAddProductDialogOpen = false },
                onSave = { id, name, cat, subCat, price, discount, img, desc, opts, inStock, featured ->
                    onSaveProduct(id, name, cat, subCat, price, discount, img, desc, opts, inStock, featured)
                    isAddProductDialogOpen = false
                }
            )
        }
    }
}

@Composable
private fun AdminSectionTab(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(if (isSelected) LuxeGoldDark else LuxeOffWhite)
            .border(
                1.dp,
                if (isSelected) LuxeGoldDark else LuxeGoldBorder,
                RoundedCornerShape(10.dp)
            )
            .clickable { onClick() }
            .padding(vertical = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            fontSize = 12.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            color = if (isSelected) LuxeWhite else LuxeCharcoal
        )
    }
}

@Composable
private fun AdminProductRow(
    product: Product,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = LuxeWhite),
        border = androidx.compose.foundation.BorderStroke(1.dp, LuxeGoldBorder.copy(alpha = 0.8f)),
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = "[#${product.id}] ${product.category}",
                        fontSize = 10.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = LuxeGoldDark
                    )
                    if (product.category == "Watches") {
                        Text(
                            text = "(${product.subCategory})",
                            fontSize = 10.5.sp,
                            color = LuxeTextMuted
                        )
                    }
                    if (!product.isAvailable) {
                        Text(
                            text = "• OUT OF STOCK",
                            fontSize = 9.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = LuxeDiscountBadge
                        )
                    }
                }

                Text(
                    text = product.name,
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = LuxeCharcoal
                )

                Text(
                    text = "Price: PKR ${String.format(Locale.US, "%,.0f", product.price)}" +
                            if (product.discountPrice != null) " (Sale: PKR ${String.format(Locale.US, "%,.0f", product.discountPrice)})" else "",
                    fontSize = 12.sp,
                    color = LuxeTextDark
                )
            }

            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                IconButton(
                    onClick = onEdit,
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit",
                        tint = LuxeGoldDark,
                        modifier = Modifier.size(18.dp)
                    )
                }

                IconButton(
                    onClick = onDelete,
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = LuxeDiscountBadge,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun CategorySummaryCard(
    title: String,
    subtitle: String,
    count: Int
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = LuxeOffWhite),
        border = androidx.compose.foundation.BorderStroke(1.dp, LuxeGoldBorder),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = title,
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = LuxeCharcoal
                )
                Text(
                    text = subtitle,
                    fontSize = 11.5.sp,
                    color = LuxeTextMuted
                )
            }

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(LuxeGoldLight)
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text(
                    text = "$count Items",
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    color = LuxeGoldDark
                )
            }
        }
    }
}

@Composable
private fun SubCategoryCard(
    title: String,
    count: Int,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = LuxeOffWhite),
        border = androidx.compose.foundation.BorderStroke(1.dp, LuxeGoldBorder),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                color = LuxeCharcoal
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "$count watches",
                fontSize = 11.sp,
                color = LuxeGoldDark
            )
        }
    }
}

@Composable
private fun AdminOrderCard(
    order: Order,
    onUpdateStatus: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var isStatusMenuOpen by remember { mutableStateOf(false) }
    val statuses = listOf("Pending", "Confirmed", "Shipped", "Delivered", "Cancelled")

    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = LuxeWhite),
        border = androidx.compose.foundation.BorderStroke(1.dp, LuxeGoldBorder),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "ORDER #${order.id}",
                    fontFamily = FontFamily.Default,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = LuxeGoldDark
                )

                // Status Badge & Dropdown
                Box {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(
                                when (order.status) {
                                    "Delivered" -> LuxeSuccess.copy(alpha = 0.15f)
                                    "Shipped" -> LuxeGoldLight
                                    "Confirmed" -> LuxeGoldGlow
                                    "Cancelled" -> LuxeDiscountBadge.copy(alpha = 0.15f)
                                    else -> LuxeCream
                                }
                            )
                            .clickable { isStatusMenuOpen = true }
                            .padding(horizontal = 10.dp, vertical = 5.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Status: ${order.status} ▾",
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.5.sp,
                                color = when (order.status) {
                                    "Delivered" -> LuxeSuccess
                                    "Cancelled" -> LuxeDiscountBadge
                                    else -> LuxeCharcoal
                                }
                            )
                        }
                    }

                    DropdownMenu(
                        expanded = isStatusMenuOpen,
                        onDismissRequest = { isStatusMenuOpen = false },
                        modifier = Modifier.background(LuxeWhite)
                    ) {
                        statuses.forEach { st ->
                            DropdownMenuItem(
                                text = { Text(st, fontSize = 12.5.sp) },
                                onClick = {
                                    onUpdateStatus(st)
                                    isStatusMenuOpen = false
                                }
                            )
                        }
                    }
                }
            }

            Text(
                text = "Customer: ${order.customerName}",
                fontWeight = FontWeight.SemiBold,
                fontSize = 13.sp,
                color = LuxeCharcoal
            )

            Text(
                text = "Phone: ${order.phone} • WhatsApp: ${order.whatsapp}",
                fontSize = 12.sp,
                color = LuxeTextDark
            )

            Text(
                text = "Address: ${order.address}, ${order.city}",
                fontSize = 12.sp,
                color = LuxeTextMuted
            )

            Text(
                text = "Date: ${order.orderDate}",
                fontSize = 11.5.sp,
                color = LuxeTextLight
            )

            Divider(color = LuxeGoldBorder.copy(alpha = 0.5f))

            Text(
                text = "Items:\n${order.itemsSummary}",
                fontSize = 12.sp,
                lineHeight = 16.sp,
                color = LuxeTextDark
            )

            Divider(color = LuxeGoldBorder.copy(alpha = 0.5f))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Total Payable:", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                Text(
                    text = "PKR ${String.format(Locale.US, "%,.0f", order.totalAmount)}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = LuxeGoldDark
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ProductEditModalDialog(
    productToEdit: Product?,
    onDismiss: () -> Unit,
    onSave: (Long, String, String, String, Double, Double?, String, String, String, Boolean, Boolean) -> Unit
) {
    var name by remember { mutableStateOf(productToEdit?.name ?: "") }
    var category by remember { mutableStateOf(productToEdit?.category ?: "Jewellery") }
    var subCategory by remember { mutableStateOf(productToEdit?.subCategory ?: "Women") }
    var priceText by remember { mutableStateOf(productToEdit?.price?.toInt()?.toString() ?: "") }
    var discountText by remember { mutableStateOf(productToEdit?.discountPrice?.toInt()?.toString() ?: "") }
    var imageUrl by remember { mutableStateOf(productToEdit?.imageUrl ?: "") }
    var description by remember { mutableStateOf(productToEdit?.description ?: "") }
    var options by remember { mutableStateOf(productToEdit?.options ?: "Standard") }
    var isAvailable by remember { mutableStateOf(productToEdit?.isAvailable ?: true) }
    var isFeatured by remember { mutableStateOf(productToEdit?.isFeatured ?: false) }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = LuxeWhite,
            modifier = Modifier
                .fillMaxWidth(0.94f)
                .fillMaxHeight(0.9f)
                .border(1.dp, LuxeGoldBorder, RoundedCornerShape(20.dp))
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                // Modal Header
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (productToEdit == null) "Add Luxury Product" else "Edit Product #${productToEdit.id}",
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = LuxeCharcoal
                    )

                    IconButton(onClick = onDismiss) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
                    }
                }

                Divider(color = LuxeGoldBorder.copy(alpha = 0.5f))

                // Form
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    item {
                        OutlinedTextField(
                            value = name,
                            onValueChange = { name = it },
                            label = { Text("Product Title *") },
                            placeholder = { Text("e.g. Royal Emerald Pendant") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    // Category Selection
                    item {
                        Text(text = "Category *", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            listOf("Jewellery", "Watches").forEach { cat ->
                                val isSelected = category == cat
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(if (isSelected) LuxeGoldDark else LuxeOffWhite)
                                        .clickable { category = cat }
                                        .padding(vertical = 8.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = cat,
                                        color = if (isSelected) LuxeWhite else LuxeCharcoal,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp
                                    )
                                }
                            }
                        }
                    }

                    // Watch Subcategory (if category is Watches)
                    if (category == "Watches") {
                        item {
                            Text(text = "Watch Subcategory *", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                listOf("Women", "Men", "Unisex").forEach { sub ->
                                    val isSelected = subCategory == sub
                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(if (isSelected) LuxeGold else LuxeOffWhite)
                                        .clickable { subCategory = sub }
                                        .padding(vertical = 8.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = sub,
                                            color = if (isSelected) LuxeWhite else LuxeCharcoal,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 12.sp
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // Prices
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedTextField(
                                value = priceText,
                                onValueChange = { priceText = it },
                                label = { Text("Price (PKR) *") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                singleLine = true,
                                modifier = Modifier.weight(1f)
                            )

                            OutlinedTextField(
                                value = discountText,
                                onValueChange = { discountText = it },
                                label = { Text("Sale Price (PKR)") },
                                placeholder = { Text("Optional") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                singleLine = true,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }

                    // Image URL or preset
                    item {
                        OutlinedTextField(
                            value = imageUrl,
                            onValueChange = { imageUrl = it },
                            label = { Text("Image URL or Preset Name") },
                            placeholder = { Text("jewellery_banner / watches_banner / https://...") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    // Options / Variations
                    item {
                        OutlinedTextField(
                            value = options,
                            onValueChange = { options = it },
                            label = { Text("Available Options (comma separated)") },
                            placeholder = { Text("18K Gold, Rose Gold, Platinum") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    // Description
                    item {
                        OutlinedTextField(
                            value = description,
                            onValueChange = { description = it },
                            label = { Text("Product Description *") },
                            placeholder = { Text("Luxury details, materials, gemstones...") },
                            maxLines = 4,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    // Switches: In Stock & Featured
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = "In Stock & Available", fontSize = 12.5.sp)
                            Switch(checked = isAvailable, onCheckedChange = { isAvailable = it })
                        }
                    }

                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = "Feature on Homepage", fontSize = 12.5.sp)
                            Switch(checked = isFeatured, onCheckedChange = { isFeatured = it })
                        }
                    }
                }

                // Footer CTA
                Surface(
                    color = LuxeWhite,
                    shadowElevation = 8.dp,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(
                        onClick = {
                            val priceVal = priceText.toDoubleOrNull() ?: 0.0
                            val discountVal = discountText.toDoubleOrNull()
                            onSave(
                                productToEdit?.id ?: 0L,
                                name.trim(),
                                category,
                                if (category == "Watches") subCategory else "General",
                                priceVal,
                                discountVal,
                                imageUrl.trim(),
                                description.trim(),
                                options.trim(),
                                isAvailable,
                                isFeatured
                            )
                        },
                        enabled = name.isNotBlank() && priceText.isNotBlank() && description.isNotBlank(),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = LuxeGold,
                            contentColor = LuxeWhite
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .height(48.dp)
                    ) {
                        Text(
                            text = if (productToEdit == null) "Publish Product" else "Update Product",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.5.sp
                        )
                    }
                }
            }
        }
    }
}
