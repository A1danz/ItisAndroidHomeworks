package ru.kpfu.itis.galeev.android.myapplication.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.kpfu.itis.galeev.android.myapplication.data.db.entity.VotesEntity

@Dao
interface VotesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun rateSong(votesEntity: VotesEntity) : Long

    @Query("SELECT * FROM votes WHERE user_id=:userId AND song_id=:songId")
    fun getRate(userId : Int, songId : Int) : List<VotesEntity>

    @Query("SELECT * FROM votes WHERE song_id=:songId")
    fun getRatings(songId: Int) : List<VotesEntity>
}