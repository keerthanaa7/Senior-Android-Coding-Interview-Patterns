package com.example.searchwithdebounceandcancellation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

//Order Cart State: A list of items with "+" and "-" buttons that updates a persistent
// "Total Amount" at the bottom of the screen.
sealed interface MenuStateOrderCart {
    object Loading : MenuStateOrderCart
    data class Success(val data: Toast) : MenuStateOrderCart
    data class Error(val message: String) : MenuStateOrderCart
}

class MenuViewModelOrderCartState(private val repository: ToastRepository) : ViewModel() {

    private val _uiState = MutableStateFlow<MenuStateOrderCart>(MenuStateOrderCart.Loading)
    val uiState = _uiState.asStateFlow()

    private val _cart = MutableStateFlow<Map<String, Int>>(emptyMap())
    val cart = _cart.asStateFlow()

    // 1. Tip Logic: State for the percentage
    private val _tipRate = MutableStateFlow(0.20) // Default 20%
    val tipRate = _tipRate.asStateFlow()

    init {
        loadMenu()
    }

    // 2. Subtotal: Calculated from Cart
    val subtotal: StateFlow<Double> = combine(uiState, cart) { state, currentCart ->
        if (state is MenuStateOrderCart.Success) {
            var sum = 0.0
            state.data.categories.forEach { category ->
                category.items.forEach { item ->
                    val qty = currentCart[item.id] ?: 0
                    if (qty > 0) {
                        sum += (getEffectivePrice(category, item) * qty)
                    }
                }
            }
            sum
        } else { 0.0 }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    // 3. Grand Total: Subtotal + Tip
    val grandTotal: StateFlow<Double> = combine(subtotal, tipRate) { bill, rate ->
        bill + (bill * rate)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    private fun loadMenu() {
        viewModelScope.launch {
            try {
                val menu = repository.getMenu()
                _uiState.value = MenuStateOrderCart.Success(menu)
            } catch (e: Exception) {
                _uiState.value = MenuStateOrderCart.Error("Could not load menu")
            }
        }
    }

    fun updateQuantity(itemId: String, delta: Int) {
        val currentQty = _cart.value[itemId] ?: 0
        val newQty = (currentQty + delta).coerceAtLeast(0)
        _cart.value = _cart.value + (itemId to newQty)
    }

    fun updateTip(rate: Double) {
        _tipRate.value = rate
    }

    fun getEffectivePrice(category: Category, item: Item): Double {
        return item.price ?: category.basePrice
    }
}