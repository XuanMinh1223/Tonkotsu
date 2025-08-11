package com.nightfire.tonkotsu.core.data.remote.dto

import com.google.gson.annotations.SerializedName

data class AiredDto(
    @SerializedName("from") val from: String?,
    @SerializedName("to") val to: String?,
    @SerializedName("prop") val prop: PropDto?,
    @SerializedName("string") val string: String?
)