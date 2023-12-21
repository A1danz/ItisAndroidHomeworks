package ru.kpfu.itis.galeev.android.myapplication.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import ru.kpfu.itis.galeev.android.myapplication.data.db.entity.SongEntity

@Dao
interface SongDao {
    @Insert
    fun saveSong(song : SongEntity)

    @Query("SELECT * FROM song")
    fun getAll() : List<SongEntity>

    @Query("SELECT * FROM song WHERE id=:id")
    fun getSongById(id : Int) : List<SongEntity>
}