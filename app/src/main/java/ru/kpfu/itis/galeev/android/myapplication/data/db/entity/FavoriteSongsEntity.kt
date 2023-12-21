package ru.kpfu.itis.galeev.android.myapplication.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = "favorite_songs", foreignKeys = [
        ForeignKey(
            UserEntity::class,
            parentColumns = ["id"],
            childColumns = ["user_id"],
            ForeignKey.CASCADE
        ), ForeignKey (
            SongEntity::class,
            parentColumns = ["id"],
            childColumns = ["song_id"],
            ForeignKey.CASCADE
        )
    ]
)
data class FavoriteSongsEntity(
    @ColumnInfo(name = "user_id")
    val userId : Int,
    @ColumnInfo(name = "song_id")
    val songId : Int
)