package com.example.searchwithdebounceandcancellation


import com.google.gson.annotations.SerializedName

data class Category(
    @SerializedName("basePrice")
    val basePrice: Double,
    @SerializedName("id")
    val id: String,
    @SerializedName("items")
    val items: List<Item>,
    @SerializedName("name")
    val name: String
)