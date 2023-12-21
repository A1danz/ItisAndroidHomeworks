package ru.kpfu.itis.galeev.android.myapplication.utils

import ru.kpfu.itis.galeev.android.myapplication.data.db.dao.UserDao
import ru.kpfu.itis.galeev.android.myapplication.data.db.entity.UserEntity
import ru.kpfu.itis.galeev.android.myapplication.db.service.UserService
import ru.kpfu.itis.galeev.android.myapplication.di.ServiceLocator
import ru.kpfu.itis.galeev.android.myapplication.model.UserModel

class AuthorizationUtil(
    private val userDao : UserDao,
    private val userService : UserService = UserService(userDao)
) {
    fun authorizeUser(email : String, password : String) : Enum<UXMessages> {
        val authUser : UserModel? = userService.authorizeUser(email, password)
        if (authUser != null) {
            ServiceLocator.authorizeUser(authUser.id)
            return UXMessages.SUCCESS_AUTH
        }
        return UXMessages.FAILURE_AUTH
    }
}