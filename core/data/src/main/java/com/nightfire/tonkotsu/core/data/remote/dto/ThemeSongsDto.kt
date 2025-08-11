package com.nightfire.tonkotsu.core.data.remote.dto

import com.google.gson.annotations.SerializedName

data class ThemeSongsDto(
    @SerializedName("openings") val openings: List<String>?,
    @SerializedName("endings") val endings: List<String>?
)