package com.princemaurya.robokalam.data.model

import com.google.gson.annotations.SerializedName

data class Quote(
    @SerializedName("q")
    val quote: String,
    @SerializedName("a")
    val author: String,
    @SerializedName("h")
    val html: String
) 