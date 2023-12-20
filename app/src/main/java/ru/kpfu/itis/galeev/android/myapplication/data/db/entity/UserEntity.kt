package ru.kpfu.itis.galeev.android.myapplication.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "usr")
data class UserEntity (
    @PrimaryKey
    var id : Int,
    val name : String,
    @ColumnInfo(name = "phone_number")
    var phoneNumber : String,
    val email : String,
    @ColumnInfo(name = "password_hash")
    var passwordHash : String
)