package com.example

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Chat
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.db.AppDatabase
import com.example.data.model.Product
import com.example.data.repository.LuxeRepository
import com.example.ui.LuxeTab
import com.example.ui.LuxeViewModel
import com.example.ui.components.*
import com.example.ui.screens.*
import com.example.ui.theme.LuxeAuraTheme
import com.example.ui.theme.LuxeSuccess
import com.example.ui.theme.LuxeWhite
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val database = AppDatabase.getDatabase(applicationContext)
        val repository = LuxeRepository(database)

        val viewModelFactory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return LuxeViewModel(repository) as T
            }
        }

        setContent {
            val viewModel: LuxeViewModel = viewModel(factory = viewModelFactory)
            LuxeAuraTheme {
                LuxeAuraApp(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun LuxeAuraApp(viewModel: LuxeViewModel) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    // State collections
    val isWelcomeFinished by viewModel.isWelcomeFinished.collectAsStateWithLifecycle()
    val activeTab by viewModel.activeTab.collectAsStateWithLifecycle()
    val isSearchOpen by viewModel.isSearchOpen.collectAsStateWithLifecycle()

    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val selectedCategory by viewModel.selectedCategory.collectAsStateWithLifecycle()
    val selectedWatchSubCategory by viewModel.selectedWatchSubCategory.collectAsStateWithLifecycle()
    val maxPrice by viewModel.maxPrice.collectAsStateWithLifecycle()
    val inStockOnly by viewModel.inStockOnly.collectAsStateWithLifecycle()
    val sortOption by viewModel.sortOption.collectAsStateWithLifecycle()

    val filteredProducts by viewModel.filteredProducts.collectAsStateWithLifecycle()
    val featuredProducts by viewModel.featuredProducts.collectAsStateWithLifecycle()
    val jewelleryProducts by viewModel.jewelleryProducts.collectAsStateWithLifecycle()
    val watchesProducts by viewModel.watchesProducts.collectAsStateWithLifecycle()
    val wishlistedProducts by viewModel.wishlistProducts.collectAsStateWithLifecycle()
    val wishlistIds by viewModel.wishlistIds.collectAsStateWithLifecycle()

    val cartItems by viewModel.cartItems.collectAsStateWithLifecycle()
    val lastPlacedOrder by viewModel.lastPlacedOrder.collectAsStateWithLifecycle()

    val isAdminAuthenticated by viewModel.isAdminAuthenticated.collectAsStateWithLifecycle()
    val adminLoginError by viewModel.adminLoginError.collectAsStateWithLifecycle()
    val allOrders by viewModel.allOrders.collectAsStateWithLifecycle()
    val allProducts by viewModel.allProducts.collectAsStateWithLifecycle()

    // Dialog state for viewing product details
    var selectedDetailProduct by remember { mutableStateOf<Product?>(null) }

    // 1. Opening Typewriter Welcome Animation
    if (!isWelcomeFinished) {
        OpeningWelcomeAnimation(
            onAnimationComplete = { viewModel.completeWelcome() }
        )
    } else {
        // Main App Layout
        Scaffold(
            snackbarHost = { SnackbarHost(snackbarHostState) },
            topBar = {
                LuxeNavbar(
                    activeTab = activeTab,
                    wishlistCount = wishlistIds.size,
                    cartCount = cartItems.sumOf { it.cartItem.quantity },
                    isSearchOpen = isSearchOpen,
                    onTabSelected = { tab ->
                        viewModel.setActiveTab(tab)
                    },
                    onToggleSearch = { viewModel.toggleSearch() },
                    onOpenAdminLogin = { viewModel.setActiveTab(LuxeTab.ADMIN) }
                )
            },
            floatingActionButton = {
                // Floating VIP WhatsApp Support button
                FloatingActionButton(
                    onClick = {
                        val url = "https://wa.me/923340634595?text=Hello%20LuxeAura.pk%2C%20I%20would%20like%20to%20inquire%20about%20your%20luxury%20collection."
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                        try {
                            context.startActivity(intent)
                        } catch (e: Exception) {
                            val dial = Intent(Intent.ACTION_DIAL, Uri.parse("tel:03340634595"))
                            context.startActivity(dial)
                        }
                    },
                    containerColor = LuxeSuccess,
                    contentColor = LuxeWhite,
                    shape = CircleShape,
                    modifier = Modifier.testTag("floating_whatsapp_btn")
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Chat,
                        contentDescription = "WhatsApp Concierge",
                        modifier = Modifier.size(24.dp)
                    )
                }
            },
            containerColor = LuxeWhite,
            modifier = Modifier.fillMaxSize()
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                // Search, Filter & Sort Bar (shown when search is opened or on Jewellery/Watches tabs)
                AnimatedVisibility(
                    visible = isSearchOpen || activeTab == LuxeTab.JEWELLERY || activeTab == LuxeTab.WATCHES,
                    enter = expandVertically() + fadeIn(),
                    exit = shrinkVertically() + fadeOut()
                ) {
                    SearchFilterSortBar(
                        searchQuery = searchQuery,
                        onSearchQueryChange = { viewModel.setSearchQuery(it) },
                        selectedCategory = selectedCategory,
                        onCategoryChange = { viewModel.setSelectedCategory(it) },
                        selectedWatchSubCategory = selectedWatchSubCategory,
                        onWatchSubCategoryChange = { viewModel.setSelectedWatchSubCategory(it) },
                        maxPrice = maxPrice,
                        onMaxPriceChange = { viewModel.setMaxPrice(it) },
                        inStockOnly = inStockOnly,
                        onInStockOnlyChange = { viewModel.setInStockOnly(it) },
                        sortOption = sortOption,
                        onSortOptionChange = { viewModel.setSortOption(it) }
                    )
                }

                // Main Tab Routing
                Box(modifier = Modifier.weight(1f)) {
                    when (activeTab) {
                        LuxeTab.HOME -> {
                            if (isSearchOpen && searchQuery.isNotBlank()) {
                                // If search bar is active with query, show filtered search results on home
                                JewelleryScreen(
                                    jewelleryProducts = filteredProducts,
                                    isWishlisted = { id -> wishlistIds.contains(id) },
                                    onWishlistToggle = { prod ->
                                        viewModel.toggleWishlist(prod.id)
                                    },
                                    onAddToCart = { prod ->
                                        viewModel.addToCart(prod, 1, "Standard")
                                        coroutineScope.launch {
                                            snackbarHostState.showSnackbar("Added ${prod.name} to Cart")
                                        }
                                    },
                                    onViewDetails = { prod -> selectedDetailProduct = prod },
                                    onNavigateTo = { tab -> viewModel.setActiveTab(tab) },
                                    onOpenAdminLogin = { viewModel.setActiveTab(LuxeTab.ADMIN) }
                                )
                            } else {
                                HomeScreen(
                                    featuredProducts = featuredProducts,
                                    isWishlisted = { id -> wishlistIds.contains(id) },
                                    onWishlistToggle = { prod ->
                                        viewModel.toggleWishlist(prod.id)
                                    },
                                    onAddToCart = { prod ->
                                        viewModel.addToCart(prod, 1, "Standard")
                                        coroutineScope.launch {
                                            snackbarHostState.showSnackbar("Added ${prod.name} to Cart")
                                        }
                                    },
                                    onViewDetails = { prod -> selectedDetailProduct = prod },
                                    onNavigateTo = { tab -> viewModel.setActiveTab(tab) },
                                    onOpenAdminLogin = { viewModel.setActiveTab(LuxeTab.ADMIN) }
                                )
                            }
                        }

                        LuxeTab.JEWELLERY -> {
                            JewelleryScreen(
                                jewelleryProducts = jewelleryProducts,
                                isWishlisted = { id -> wishlistIds.contains(id) },
                                onWishlistToggle = { prod ->
                                    viewModel.toggleWishlist(prod.id)
                                },
                                onAddToCart = { prod ->
                                    viewModel.addToCart(prod, 1, "Standard")
                                    coroutineScope.launch {
                                        snackbarHostState.showSnackbar("Added ${prod.name} to Cart")
                                    }
                                },
                                onViewDetails = { prod -> selectedDetailProduct = prod },
                                onNavigateTo = { tab -> viewModel.setActiveTab(tab) },
                                onOpenAdminLogin = { viewModel.setActiveTab(LuxeTab.ADMIN) }
                            )
                        }

                        LuxeTab.WATCHES -> {
                            WatchesScreen(
                                watchesProducts = watchesProducts,
                                selectedSubCategory = selectedWatchSubCategory,
                                onSelectSubCategory = { sub -> viewModel.setSelectedWatchSubCategory(sub) },
                                isWishlisted = { id -> wishlistIds.contains(id) },
                                onWishlistToggle = { prod ->
                                    viewModel.toggleWishlist(prod.id)
                                },
                                onAddToCart = { prod ->
                                    viewModel.addToCart(prod, 1, "Standard")
                                    coroutineScope.launch {
                                        snackbarHostState.showSnackbar("Added ${prod.name} to Cart")
                                    }
                                },
                                onViewDetails = { prod -> selectedDetailProduct = prod },
                                onNavigateTo = { tab -> viewModel.setActiveTab(tab) },
                                onOpenAdminLogin = { viewModel.setActiveTab(LuxeTab.ADMIN) }
                            )
                        }

                        LuxeTab.WISHLIST -> {
                            WishlistScreen(
                                wishlistedProducts = wishlistedProducts,
                                onWishlistToggle = { prod -> viewModel.toggleWishlist(prod.id) },
                                onAddToCart = { prod ->
                                    viewModel.addToCart(prod, 1, "Standard")
                                    coroutineScope.launch {
                                        snackbarHostState.showSnackbar("Added ${prod.name} to Cart")
                                    }
                                },
                                onViewDetails = { prod -> selectedDetailProduct = prod },
                                onNavigateTo = { tab -> viewModel.setActiveTab(tab) },
                                onOpenAdminLogin = { viewModel.setActiveTab(LuxeTab.ADMIN) }
                            )
                        }

                        LuxeTab.CART -> {
                            CartCheckoutScreen(
                                cartItems = cartItems,
                                lastPlacedOrder = lastPlacedOrder,
                                onUpdateQuantity = { item, delta ->
                                    viewModel.updateCartQuantity(item.cartItem, delta)
                                },
                                onRemoveCartItem = { item ->
                                    viewModel.removeFromCart(item.cartItem)
                                },
                                onClearCart = { viewModel.clearCart() },
                                onPlaceOrder = { name, phone, wa, addr, city, total ->
                                    viewModel.placeOrder(name, phone, wa, addr, city, total)
                                },
                                onDismissOrderConfirmation = {
                                    viewModel.dismissOrderConfirmation()
                                },
                                onNavigateTo = { tab -> viewModel.setActiveTab(tab) },
                                onOpenAdminLogin = { viewModel.setActiveTab(LuxeTab.ADMIN) }
                            )
                        }

                        LuxeTab.CONTACT -> {
                            ContactScreen(
                                onSendMessage = { name, email, msg ->
                                    coroutineScope.launch {
                                        snackbarHostState.showSnackbar("Thank you, $name. Message received!")
                                    }
                                },
                                onNavigateTo = { tab -> viewModel.setActiveTab(tab) },
                                onOpenAdminLogin = { viewModel.setActiveTab(LuxeTab.ADMIN) }
                            )
                        }

                        LuxeTab.ADMIN -> {
                            AdminPanelScreen(
                                isAuthenticated = isAdminAuthenticated,
                                loginError = adminLoginError,
                                allProducts = allProducts,
                                allOrders = allOrders,
                                onLogin = { u, p -> viewModel.loginAdmin(u, p) },
                                onLogout = { viewModel.logoutAdmin() },
                                onSaveProduct = { id, name, cat, subCat, price, discount, img, desc, opts, inStock, feat ->
                                    viewModel.saveProduct(id, name, cat, subCat, price, discount, img, desc, opts, inStock, feat)
                                    coroutineScope.launch {
                                        snackbarHostState.showSnackbar("Product saved successfully!")
                                    }
                                },
                                onDeleteProduct = { id ->
                                    viewModel.deleteProduct(id)
                                    coroutineScope.launch {
                                        snackbarHostState.showSnackbar("Product deleted.")
                                    }
                                },
                                onUpdateOrderStatus = { orderId, status ->
                                    viewModel.updateOrderStatus(orderId, status)
                                    coroutineScope.launch {
                                        snackbarHostState.showSnackbar("Order $orderId status updated to $status")
                                    }
                                },
                                onResetCatalog = {
                                    viewModel.resetCatalogToDefaults()
                                    coroutineScope.launch {
                                        snackbarHostState.showSnackbar("Catalog restored to factory defaults.")
                                    }
                                },
                                onNavigateTo = { tab -> viewModel.setActiveTab(tab) }
                            )
                        }
                    }
                }
            }

            // Product Detail Dialog
            if (selectedDetailProduct != null) {
                ProductDetailDialog(
                    product = selectedDetailProduct!!,
                    isWishlisted = wishlistIds.contains(selectedDetailProduct!!.id),
                    onDismiss = { selectedDetailProduct = null },
                    onWishlistToggle = { prod -> viewModel.toggleWishlist(prod.id) },
                    onAddToCart = { prod, qty, opt ->
                        viewModel.addToCart(prod, qty, opt)
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar("Added $qty x ${prod.name} to Cart")
                        }
                    }
                )
            }
        }
    }
}
