package ru.kpfu.itis.galeev.android.myapplication.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.kpfu.itis.galeev.android.myapplication.data.db.dao.UserDao
import ru.kpfu.itis.galeev.android.myapplication.data.db.entity.UserEntity

@Database(
    entities = [UserEntity::class],
    version = 1)
abstract class ApplicationDatabase : RoomDatabase() {
    abstract val userDao : UserDao
}