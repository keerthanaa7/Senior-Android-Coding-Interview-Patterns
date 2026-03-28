package com.example.searchwithdebounceandcancellation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RecursiveSearchViewModel(private val repository: MenuRepository) : ViewModel() {

    private val _searchResult = MutableStateFlow<ItemX?>(null)
    val searchResult = _searchResult.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private val menuData = repository.getMenu()

    fun onSearchQueryChanged(newQuery: String) {
        _searchQuery.value = newQuery
        
        if (newQuery.isBlank()) {
            _searchResult.value = null
            return
        }

        // Trigger the recursive search
        viewModelScope.launch {
            val result = repository.findItemRecursively(menuData.menuGroups, newQuery)
            _searchResult.value = result
        }
    }
}