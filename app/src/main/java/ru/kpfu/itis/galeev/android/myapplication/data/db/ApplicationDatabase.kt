package ru.kpfu.itis.galeev.android.myapplication.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.kpfu.itis.galeev.android.myapplication.data.db.dao.FavoriteSongsDao
import ru.kpfu.itis.galeev.android.myapplication.data.db.dao.SongDao
import ru.kpfu.itis.galeev.android.myapplication.data.db.dao.UserDao
import ru.kpfu.itis.galeev.android.myapplication.data.db.dao.VotesDao
import ru.kpfu.itis.galeev.android.myapplication.data.db.entity.FavoriteSongsEntity
import ru.kpfu.itis.galeev.android.myapplication.data.db.entity.SongEntity
import ru.kpfu.itis.galeev.android.myapplication.data.db.entity.UserEntity
import ru.kpfu.itis.galeev.android.myapplication.data.db.entity.VotesEntity

@Database(
    entities = [UserEntity::class, SongEntity::class, FavoriteSongsEntity::class, VotesEntity::class],
    version = 1)
abstract class ApplicationDatabase : RoomDatabase() {
    abstract val userDao : UserDao
    abstract val songDao : SongDao
    abstract val favoriteSongsDao : FavoriteSongsDao
    abstract val votesDao : VotesDao
}