package ru.kpfu.itis.galeev.android.myapplication.di

import android.content.Context
import androidx.room.Room
import ru.kpfu.itis.galeev.android.myapplication.data.db.ApplicationDatabase

object ServiceLocator {
    private var dbInstance : ApplicationDatabase? = null

    private fun createDatabase(ctx : Context) {
        dbInstance = Room.databaseBuilder(ctx, ApplicationDatabase::class.java, "application.db")
            .build()
    }

    fun getDbInstance(ctx: Context) : ApplicationDatabase {
        if (dbInstance == null) {
            createDatabase(ctx)
        }
        return dbInstance!!
    }
}