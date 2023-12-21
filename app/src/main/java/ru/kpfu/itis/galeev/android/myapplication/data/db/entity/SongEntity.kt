package ru.kpfu.itis.galeev.android.myapplication.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("song")
data class SongEntity (
    @PrimaryKey(autoGenerate = true)
    var id : Int? = null,
    val title : String,
    val author : String,
    val duration : Int,
    val text : String
    )