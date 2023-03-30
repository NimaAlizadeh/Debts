package com.example.debts.models.entity


import com.google.gson.annotations.SerializedName

data class NewEntityResponse(
    @SerializedName("data")
    val `data`: Data?,
    @SerializedName("message")
    val message: String? // New Entity Created
) {
    data class Data(
        @SerializedName("id")
        val id: String? // 31297
    )
}