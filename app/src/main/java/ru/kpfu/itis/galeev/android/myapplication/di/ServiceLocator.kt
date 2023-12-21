package ru.kpfu.itis.galeev.android.myapplication.di

import android.content.Context
import androidx.room.Room
import ru.kpfu.itis.galeev.android.myapplication.data.db.ApplicationDatabase
import ru.kpfu.itis.galeev.android.myapplication.db.service.UserService

object ServiceLocator {
    private var dbInstance : ApplicationDatabase? = null
    private var userId : Int? = null

    private fun createDatabase(ctx : Context) {
        ctx.deleteDatabase("application.db")
        dbInstance = Room.databaseBuilder(ctx, ApplicationDatabase::class.java, "application.db")
            .build()
    }

    fun getDbInstance(ctx: Context) : ApplicationDatabase {
        if (dbInstance == null) {
            createDatabase(ctx)
        }
        return dbInstance!!
    }

    fun authorizeUser(userId : Int) {
        this.userId = userId
    }

    fun terminateUserAuth() {
        userId = null
    }

}