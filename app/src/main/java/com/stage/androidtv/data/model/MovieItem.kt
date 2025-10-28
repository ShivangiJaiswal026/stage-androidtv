package com.stage.androidtv.data.model

data class MovieItem(
    val id: String,
    val title: String,
    val description: String,
    val year: String,
    val genre: String,
    val posterUrl: String,
    val videoUrl: String
)