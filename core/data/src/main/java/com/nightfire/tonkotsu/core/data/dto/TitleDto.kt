package com.nightfire.tonkotsu.core.data.dto

import com.google.gson.annotations.SerializedName

data class TitleDto(
    @SerializedName("type") val type: String?,
    @SerializedName("title") val title: String?
)