package ru.kpfu.itis.galeev.android.myapplication.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey


@Entity(
    tableName = "votes",
    foreignKeys = [
        ForeignKey(
            UserEntity::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("user_id"),
            onDelete = ForeignKey.CASCADE
        ), ForeignKey(
            SongEntity::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("song_id"),
            onDelete = ForeignKey.CASCADE
        )]
)
data class VotesEntity (
    @ColumnInfo(name = "user_id")
    val userId : Int,
    @ColumnInfo(name = "song_id")
    val songId : Int,
    val rate : Int
)