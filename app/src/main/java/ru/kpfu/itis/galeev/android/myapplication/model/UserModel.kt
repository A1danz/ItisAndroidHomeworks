package ru.kpfu.itis.galeev.android.myapplication.model

import androidx.room.ColumnInfo
import androidx.room.PrimaryKey

data class UserModel (
    var id : Int,
    val name : String,
    var phoneNumber : String,
    val email : String
)