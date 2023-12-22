package ru.kpfu.itis.galeev.android.myapplication.db.service

import android.content.Context
import ru.kpfu.itis.galeev.android.myapplication.data.db.converters.UserTypeConverter
import ru.kpfu.itis.galeev.android.myapplication.data.db.dao.UserDao
import ru.kpfu.itis.galeev.android.myapplication.data.db.entity.UserEntity
import ru.kpfu.itis.galeev.android.myapplication.di.ServiceLocator
import ru.kpfu.itis.galeev.android.myapplication.model.UserModel
import ru.kpfu.itis.galeev.android.myapplication.utils.EncryptionUtil
import ru.kpfu.itis.galeev.android.myapplication.utils.UXMessages
import java.lang.RuntimeException

class UserService(
    private val userDao : UserDao,
    private val userTypeConverter : UserTypeConverter = UserTypeConverter()
) {
    fun checkUserExists(email : String, phoneNumber : String) : Boolean {
        val users : List<Int> = userDao.getUserByPhoneOrEmail(email = email, phoneNumber = phoneNumber)
        return users.isEmpty()
    }

    fun authorizeUser(email : String, password : String) : UserModel? {
        val users : List<UserEntity> = userDao.getUserByEmailAndPassword(
            email,
            EncryptionUtil.getPasswordHash(password)
        )

        if (users.size > 1) println("TEST TAG - equals users more than 1 :)")
        with(userTypeConverter) {
            return if (users.isEmpty()) null else fromUserEntityToUserModel(users[0])
        }
    }

    fun getUserById(id : Int) : UserModel {
        val users = userDao.getUserById(id)
        if (users.isNotEmpty()) {
            return userTypeConverter.fromUserEntityToUserModel(users[0])
        } else throw RuntimeException("[userService.getUserById] can't find user with id - $id")
    }

    fun changeUserPhoneNumber(id : Int, newPhone : String) : Enum<UXMessages> {
        val newPhoneUsers = userDao.getUserByPhone(newPhone)
        if (newPhoneUsers.isNotEmpty()) {
            return UXMessages.PHONE_ALREADY_EXISTS
        }
        userDao.updatePhoneNumber(id, newPhone)
        return UXMessages.SUCCESS_CHANGE_PHONE
    }

    fun changeUserPassword(id : Int, currentPassword : String, newPassword : String) : Enum<UXMessages> {
        val passwordCorrect = (userDao.getUserByIdAndPassword(
            id,
            EncryptionUtil.getPasswordHash(currentPassword)
        )).size == 1

        if (!passwordCorrect) return UXMessages.PASSWORD_NOT_CORRECT

        userDao.updatePassword(id, EncryptionUtil.getPasswordHash(newPassword))
        return UXMessages.SUCCESS_CHANGE_PASSWORD
    }

    fun deleteUser(id : Int) {
        userDao.deleteUser(id)
    }
}