package com.example.searchwithdebounceandcancellation


import com.google.gson.annotations.SerializedName

data class Item(
    @SerializedName("id")
    val id: String,
    @SerializedName("inventory")
    val inventory: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("price")
    val price: Double? // Must be nullable to support inheritance
)