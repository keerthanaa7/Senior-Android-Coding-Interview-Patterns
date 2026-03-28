package com.example.searchwithdebounceandcancellation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

//Menu Hierarchy Inheritance: Calculate item prices by traversing a nested Category/Item tree
// (if an item's price is null, inherit from the parent).
sealed interface MenuState {
    object Loading : MenuState
    data class Success(val data: Toast) : MenuState
    data class Error(val message: String) : MenuState
}

class MenuViewModel(private val repository: ToastRepository) : ViewModel() {
    private val _uiState = MutableStateFlow<MenuState>(MenuState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        loadMenu()
    }

    private fun loadMenu() {
        viewModelScope.launch {
            try {
                val menu = repository.getMenu()
                _uiState.value = MenuState.Success(menu)
            } catch (e: Exception) {
                _uiState.value = MenuState.Error("Could not load menu")
            }
        }
    }

    // Helper function for the UI to get the inherited price
    fun getEffectivePrice(category: Category, item: Item): Double {
        return item.price ?: category.basePrice
    }
}