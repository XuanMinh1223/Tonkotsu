package com.nightfire.tonkotsu.core.data.dto


import com.google.gson.annotations.SerializedName

data class DatePartDto(
    @SerializedName("day") val day: Int?,
    @SerializedName("month") val month: Int?,
    @SerializedName("year") val year: Int?
)