package com.stage.androidtv.data.model

data class MovieItem( //for network
    val id: String,
    val title: String,
    val description: String,
    val year: String,
    val genre: String,
    val posterUrl: String,
    val videoUrl: String
)