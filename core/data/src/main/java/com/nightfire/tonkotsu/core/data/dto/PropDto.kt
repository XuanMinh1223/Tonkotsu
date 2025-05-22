package com.nightfire.tonkotsu.core.data.dto

import com.google.gson.annotations.SerializedName

data class PropDto(
    @SerializedName("from") val from: DatePartDto?,
    @SerializedName("to") val to: DatePartDto?,
    @SerializedName("string") val string: String? // Added this as per your JSON sample
)