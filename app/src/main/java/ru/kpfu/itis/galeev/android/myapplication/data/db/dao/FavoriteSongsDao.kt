package ru.kpfu.itis.galeev.android.myapplication.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import ru.kpfu.itis.galeev.android.myapplication.data.db.entity.FavoriteSongsEntity
import ru.kpfu.itis.galeev.android.myapplication.data.db.entity.SongEntity
import ru.kpfu.itis.galeev.android.myapplication.data.db.relation.UserSongs

@Dao
interface FavoriteSongsDao {
    @Insert
    fun addSongToFavorite(favoriteSongsEntity: FavoriteSongsEntity)

    @Query("SELECT id, title, author, duration, text " +
            "FROM favorite_songs, song " +
            "WHERE id == song_id AND user_id=:userId")
    fun getUserSongs(userId : Int) : List<SongEntity>

    @Delete
    fun deleteSongsFromFavorite(favoriteSongsEntity: FavoriteSongsEntity)
}