package com.nightfire.tonkotsu.core.data.dto

import com.google.gson.annotations.SerializedName

data class PaginationDto(
    @SerializedName("last_visible_page") val lastVisiblePage: Int,
    @SerializedName("has_next_page") val hasNextPage: Boolean,
    @SerializedName("current_page") val currentPage: Int,
    @SerializedName("items") val items: ItemsDto?
)