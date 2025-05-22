package com.nightfire.tonkotsu.core.data.dto

import com.google.gson.annotations.SerializedName

data class BroadcastDto(
    @SerializedName("day") val day: String?,
    @SerializedName("time") val time: String?,
    @SerializedName("timezone") val timezone: String?,
    @SerializedName("string") val string: String?
)