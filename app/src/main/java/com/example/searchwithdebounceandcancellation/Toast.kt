package com.example.searchwithdebounceandcancellation


import com.google.gson.annotations.SerializedName

data class Toast(
    @SerializedName("categories")
    val categories: List<Category>,
    @SerializedName("restaurantName")
    val restaurantName: String
)