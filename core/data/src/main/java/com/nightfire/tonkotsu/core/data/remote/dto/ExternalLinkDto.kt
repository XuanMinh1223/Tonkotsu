package com.nightfire.tonkotsu.core.data.remote.dto

import com.google.gson.annotations.SerializedName

data class ExternalLinkDto(
    @SerializedName("name") val name: String?,
    @SerializedName("url") val url: String?
)