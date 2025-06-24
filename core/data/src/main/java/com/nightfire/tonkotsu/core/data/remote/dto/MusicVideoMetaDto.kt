package com.nightfire.tonkotsu.core.data.remote.dto

import com.google.gson.annotations.SerializedName

data class MusicVideoMetaDto(
    @SerializedName("title") val title: String?,
    @SerializedName("author") val author: String?
)