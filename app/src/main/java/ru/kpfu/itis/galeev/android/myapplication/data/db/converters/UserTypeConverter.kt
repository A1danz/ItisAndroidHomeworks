package ru.kpfu.itis.galeev.android.myapplication.data.db.converters

import androidx.room.TypeConverter
import ru.kpfu.itis.galeev.android.myapplication.data.db.entity.UserEntity
import ru.kpfu.itis.galeev.android.myapplication.model.UserModel


class UserTypeConverter {
    @TypeConverter
    fun fromUserEntityToUserModel(user : UserEntity) : UserModel {
        return UserModel(
            user.id!!,
            user.name,
            user.phoneNumber,
            user.email
        )
    }
}