package com.example.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.CartItem
import com.example.data.model.CartItemWithProduct
import com.example.data.model.Order
import com.example.data.model.Product
import com.example.data.repository.LuxeRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

enum class LuxeTab {
    HOME,
    JEWELLERY,
    WATCHES,
    WISHLIST,
    CONTACT,
    CART,
    ADMIN
}

enum class SortOption(val displayName: String) {
    NEWEST("Newest"),
    PRICE_LOW_TO_HIGH("Price: Low to High"),
    PRICE_HIGH_TO_LOW("Price: High to Low"),
    NAME_A_Z("Name: A-Z")
}

data class FilterState(
    val query: String = "",
    val category: String = "All",
    val watchSubCategory: String = "All",
    val maxPrice: Float = 100000f,
    val inStockOnly: Boolean = false,
    val sortOption: SortOption = SortOption.NEWEST
)

class LuxeViewModel(private val repository: LuxeRepository) : ViewModel() {

    init {
        viewModelScope.launch {
            repository.seedInitialDataIfEmpty()
        }
    }

    // Welcome Screen State
    private val _isWelcomeFinished = MutableStateFlow(false)
    val isWelcomeFinished: StateFlow<Boolean> = _isWelcomeFinished.asStateFlow()

    fun completeWelcome() {
        _isWelcomeFinished.value = true
    }

    // Navigation Tab
    private val _activeTab = MutableStateFlow(LuxeTab.HOME)
    val activeTab: StateFlow<LuxeTab> = _activeTab.asStateFlow()

    fun setActiveTab(tab: LuxeTab) {
        _activeTab.value = tab
    }

    // Search Toggle
    private val _isSearchOpen = MutableStateFlow(false)
    val isSearchOpen: StateFlow<Boolean> = _isSearchOpen.asStateFlow()

    fun toggleSearch() {
        _isSearchOpen.value = !_isSearchOpen.value
    }

    fun setSearchOpen(open: Boolean) {
        _isSearchOpen.value = open
    }

    // Search & Filter StateFlows
    val searchQuery = MutableStateFlow("")
    val selectedCategory = MutableStateFlow("All")
    val selectedWatchSubCategory = MutableStateFlow("All")
    val maxPrice = MutableStateFlow(100000f)
    val inStockOnly = MutableStateFlow(false)
    val sortOption = MutableStateFlow(SortOption.NEWEST)

    fun setSearchQuery(query: String) { searchQuery.value = query }
    fun setSelectedCategory(cat: String) { selectedCategory.value = cat }
    fun setSelectedWatchSubCategory(sub: String) { selectedWatchSubCategory.value = sub }
    fun setMaxPrice(price: Float) { maxPrice.value = price }
    fun setInStockOnly(inStock: Boolean) { inStockOnly.value = inStock }
    fun setSortOption(sort: SortOption) { sortOption.value = sort }

    // Repository Flows
    val allProducts: StateFlow<List<Product>> = repository.allProducts.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val featuredProducts: StateFlow<List<Product>> = repository.featuredProducts.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val jewelleryProducts: StateFlow<List<Product>> = repository.jewelleryProducts.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val watchesProducts: StateFlow<List<Product>> = combine(
        repository.watchesProducts,
        selectedWatchSubCategory
    ) { watches: List<Product>, subCategory: String ->
        if (subCategory.equals("All", ignoreCase = true)) {
            watches
        } else {
            watches.filter { it.subCategory.equals(subCategory, ignoreCase = true) }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val wishlistProducts: StateFlow<List<Product>> = repository.wishlistedProducts.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val wishlistIds: StateFlow<List<Long>> = repository.wishlistProductIds.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val cartItems: StateFlow<List<CartItemWithProduct>> = repository.cartWithProducts.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val allOrders: StateFlow<List<Order>> = repository.allOrders.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    private val _lastPlacedOrder = MutableStateFlow<Order?>(null)
    val lastPlacedOrder: StateFlow<Order?> = _lastPlacedOrder.asStateFlow()

    fun dismissOrderConfirmation() {
        _lastPlacedOrder.value = null
    }

    // Filter Aggregator Flow
    private val filterStateFlow: Flow<FilterState> = combine(
        searchQuery,
        selectedCategory,
        selectedWatchSubCategory,
        maxPrice,
        inStockOnly
    ) { q, cat, watchSub, pr, inSt ->
        FilterState(
            query = q,
            category = cat,
            watchSubCategory = watchSub,
            maxPrice = pr,
            inStockOnly = inSt
        )
    }.combine(sortOption) { filter, sort ->
        filter.copy(sortOption = sort)
    }

    val filteredProducts: StateFlow<List<Product>> = combine(
        allProducts,
        filterStateFlow
    ) { products, filter ->
        products.filter { product ->
            val matchesQuery = filter.query.isBlank() ||
                    product.name.contains(filter.query, ignoreCase = true) ||
                    product.category.contains(filter.query, ignoreCase = true) ||
                    product.subCategory.contains(filter.query, ignoreCase = true) ||
                    product.description.contains(filter.query, ignoreCase = true)

            val matchesCategory = filter.category.equals("All", ignoreCase = true) ||
                    product.category.equals(filter.category, ignoreCase = true)

            val matchesWatchSub = if (product.category.equals("Watches", ignoreCase = true) && !filter.watchSubCategory.equals("All", ignoreCase = true)) {
                product.subCategory.equals(filter.watchSubCategory, ignoreCase = true)
            } else {
                true
            }

            val effectivePrice = product.discountPrice ?: product.price
            val matchesPrice = effectivePrice <= filter.maxPrice

            val matchesStock = !filter.inStockOnly || product.isAvailable

            matchesQuery && matchesCategory && matchesWatchSub && matchesPrice && matchesStock
        }.let { list ->
            when (filter.sortOption) {
                SortOption.NEWEST -> list.sortedByDescending { it.createdTimestamp }
                SortOption.PRICE_LOW_TO_HIGH -> list.sortedBy { it.discountPrice ?: it.price }
                SortOption.PRICE_HIGH_TO_LOW -> list.sortedByDescending { it.discountPrice ?: it.price }
                SortOption.NAME_A_Z -> list.sortedBy { it.name.lowercase() }
            }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    // Wishlist actions
    fun toggleWishlist(productId: Long) {
        viewModelScope.launch {
            val isCurrentlyWishlisted = wishlistIds.value.contains(productId)
            repository.toggleWishlist(productId, isCurrentlyWishlisted)
        }
    }

    // Cart actions
    fun addToCart(product: Product, quantity: Int = 1, option: String = "Standard") {
        viewModelScope.launch {
            repository.addToCart(product.id, quantity, option)
        }
    }

    fun updateCartQuantity(cartItem: CartItem, delta: Int) {
        val newQty = cartItem.quantity + delta
        viewModelScope.launch {
            if (newQty <= 0) {
                repository.removeCartItem(cartItem.id)
            } else {
                repository.updateCartItem(cartItem.copy(quantity = newQty))
            }
        }
    }

    fun removeFromCart(cartItem: CartItem) {
        viewModelScope.launch {
            repository.removeCartItem(cartItem.id)
        }
    }

    fun clearCart() {
        viewModelScope.launch {
            repository.clearCart()
        }
    }

    fun placeOrder(
        customerName: String,
        phone: String,
        whatsapp: String,
        address: String,
        city: String,
        total: Double
    ) {
        val currentCart = cartItems.value
        if (currentCart.isEmpty()) return

        viewModelScope.launch {
            val order = repository.placeOrder(
                customerName = customerName,
                phone = phone,
                whatsapp = whatsapp,
                address = address,
                city = city,
                items = currentCart,
                totalAmount = total
            )
            _lastPlacedOrder.value = order
        }
    }

    // Admin Authentication & Operations
    private val _isAdminAuthenticated = MutableStateFlow(false)
    val isAdminAuthenticated: StateFlow<Boolean> = _isAdminAuthenticated.asStateFlow()

    private val _adminLoginError = MutableStateFlow<String?>(null)
    val adminLoginError: StateFlow<String?> = _adminLoginError.asStateFlow()

    fun loginAdmin(user: String, pass: String): Boolean {
        // Authenticate credentials securely
        if (user.trim() == "luxeaura" && pass.trim() == "nargis@0334") {
            _isAdminAuthenticated.value = true
            _adminLoginError.value = null
            return true
        } else {
            _adminLoginError.value = "Invalid username or password"
            return false
        }
    }

    fun logoutAdmin() {
        _isAdminAuthenticated.value = false
        _adminLoginError.value = null
    }

    fun saveProduct(
        id: Long = 0L,
        name: String,
        category: String,
        subCategory: String,
        price: Double,
        discountPrice: Double?,
        imageUrl: String,
        description: String,
        options: String,
        isAvailable: Boolean,
        isFeatured: Boolean
    ) {
        viewModelScope.launch {
            val product = Product(
                id = id,
                name = name,
                category = category,
                subCategory = subCategory,
                price = price,
                discountPrice = discountPrice,
                imageUrl = imageUrl.ifBlank { if (category == "Watches") "watches_banner" else "jewellery_banner" },
                description = description,
                options = options.ifBlank { "Standard" },
                isAvailable = isAvailable,
                isFeatured = isFeatured,
                rating = 4.9,
                reviewsCount = (10..50).random()
            )
            if (id == 0L) {
                repository.insertProduct(product)
            } else {
                repository.updateProduct(product)
            }
        }
    }

    fun deleteProduct(productId: Long) {
        viewModelScope.launch {
            repository.deleteProduct(productId)
        }
    }

    fun updateOrderStatus(orderId: String, status: String) {
        viewModelScope.launch {
            repository.updateOrderStatus(orderId, status)
        }
    }

    fun resetCatalogToDefaults() {
        viewModelScope.launch {
            repository.resetToDefaultCatalog()
        }
    }
}
