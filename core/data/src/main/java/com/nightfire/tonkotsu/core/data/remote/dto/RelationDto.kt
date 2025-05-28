package com.nightfire.tonkotsu.core.data.remote.dto

import com.google.gson.annotations.SerializedName

data class RelationDto(
    @SerializedName("relation") val relation: String?, // e.g., "Prequel", "Sequel", "Side Story"
    @SerializedName("entry") val entry: List<RelationEntryDto>?
)