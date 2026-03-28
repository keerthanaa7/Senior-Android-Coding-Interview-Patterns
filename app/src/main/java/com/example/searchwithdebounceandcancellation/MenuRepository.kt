package com.example.searchwithdebounceandcancellation

import com.google.gson.Gson
import kotlinx.coroutines.delay

class MenuRepository {
    // This is the recursive "Engine"
    fun findItemRecursively(groups: List<MenuGroup>, query: String): ItemX? {
        for (group in groups) {
            // 1. Search items in the current group
            val match = group.items.find { it.label.contains(query, ignoreCase = true) }
            if (match != null) return match

            // 2. Dive into sub-groups (Recursion)
            val nestedMatch = findItemRecursively(group.subGroups, query)
            if (nestedMatch != null) return nestedMatch
        }
        return null
    }

     fun getMenu(): RecursiveData {
        return Gson().fromJson(RECURSIVE_MENU_JSON, RecursiveData::class.java)
    }

}