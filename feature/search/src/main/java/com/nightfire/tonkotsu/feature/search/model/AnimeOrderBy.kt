package com.nightfire.tonkotsu.feature.search.model

enum class AnimeOrderBy(val apiName: String, val label: String) {
    POPULARITY("popularity", "Popularity"),
    SCORE("score", "Score"),
    TITLE("title", "Title"),
    START_DATE("start_date", "Start Date"),
    END_DATE("end_date", "End Date"),
    EPISODES("episodes", "Episodes"),
    RANK("rank", "Rank"),
    MEMBERS("members", "Members"),
    FAVORITES("favorites", "Favorites");
}

