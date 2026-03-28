package com.example.searchwithdebounceandcancellation


import com.google.gson.annotations.SerializedName

data class RecursiveData(
    @SerializedName("menuGroups")
    val menuGroups: List<MenuGroup>,
    @SerializedName("storeName")
    val storeName: String
)