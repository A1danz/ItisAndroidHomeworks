package ru.kpfu.itis.galeev.android.myapplication.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import ru.kpfu.itis.galeev.android.myapplication.data.db.entity.UserEntity

@Dao
interface UserDao {
    @Query("SELECT * FROM usr WHERE id=:id")
    fun getUserById(id : Int) : List<UserEntity>

    @Query("SELECT id FROM usr WHERE phone_number=:phoneNumber")
    fun getUserByPhone(phoneNumber : String) : List<Int>

    @Query("SELECT id FROM usr WHERE id=:id AND password_hash=:passwordHash")
    fun getUserByPhoneAndPassword(id : Int, passwordHash : String) : List<Int>

    @Query("DELETE FROM usr WHERE id=:id")
    fun removeUser(id : Int)

    @Query("SELECT id FROM usr WHERE phone_number=:phoneNumber OR email=:email")
    fun getUserByPhoneOrEmail(phoneNumber : String, email : String) : List<Int>

    @Insert
    fun saveUser(user : UserEntity) : Long

    @Query("UPDATE usr SET phone_number=:newPhoneNumber WHERE id=:userId")
    fun updatePhoneNumber(userId : Int, newPhoneNumber : String)

    @Query("UPDATE usr SET password_hash=:newPasswordHash WHERE id=:userId")
    fun updatePassword(userId : Int, newPasswordHash : String)

    @Query("SELECT * FROM usr WHERE email LIKE :email AND password_hash LIKE :passwordHash")
    fun getUserByEmailAndPassword(email : String, passwordHash: String) : List<UserEntity>
}