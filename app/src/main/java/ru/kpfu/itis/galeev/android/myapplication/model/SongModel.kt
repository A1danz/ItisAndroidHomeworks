package ru.kpfu.itis.galeev.android.myapplication.model

data class SongModel(
    val id : Int,
    val title : String,
    val author : String,
    val duration : Int,
    val text : String,
    var isFavorite: Boolean = false
)
