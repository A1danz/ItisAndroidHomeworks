package ru.kpfu.itis.galeev.android.myapplication.utils

import java.security.MessageDigest

object EncryptionUtil {
//    @JvmStatic
//    fun main(args : Array<String>) {
//        println(getPasswordHash("12345").equals(getPasswordHash("12345")))
//    }
    fun getPasswordHash(password : String) : String {
        val messageDigest : MessageDigest = MessageDigest.getInstance("SHA-256")
        messageDigest.update(password.toByteArray())

        val bytes : ByteArray = messageDigest.digest()
        return String(bytes)
    }
}