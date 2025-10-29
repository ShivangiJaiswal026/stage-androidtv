package com.stage.androidtv.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movies") //for local
data class MovieEntity(
    @PrimaryKey val id: String,
    val title: String,
    val description: String,
    val year: String,
    val genre: String,
    val posterUrl: String,
    val videoUrl: String
)