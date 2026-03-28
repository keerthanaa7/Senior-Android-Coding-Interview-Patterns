package com.example.searchwithdebounceandcancellation


import com.google.gson.annotations.SerializedName

data class MenuGroup(
    @SerializedName("groupId")
    val groupId: String,
    @SerializedName("groupName")
    val groupName: String,
    @SerializedName("items")
    val items: List<ItemX> = emptyList(),
    @SerializedName("startingPrice")
    val startingPrice: Double,
    @SerializedName("subGroups")
    val subGroups: List<MenuGroup> = emptyList(),
)