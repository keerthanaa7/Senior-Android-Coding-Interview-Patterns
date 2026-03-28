package com.example.searchwithdebounceandcancellation


import com.google.gson.annotations.SerializedName

data class ItemX(
    @SerializedName("cost")
    val cost: Double,
    @SerializedName("id")
    val id: String,
    @SerializedName("label")
    val label: String,
    @SerializedName("stock")
    val stock: Int
)