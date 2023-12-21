package ru.kpfu.itis.galeev.android.myapplication.utils

import android.util.Base64
import java.security.MessageDigest

object EncryptionUtil {
//    @JvmStatic
//    fun main(args : Array<String>) {
//        val authTime = System.currentTimeMillis()
//        val sessionString = "$authTime/2"
//        with(EncryptionUtil) {
//            println("TEST TAG - ${decodeData(encodeData(sessionString))}")
//        }
//    }
    fun getPasswordHash(password : String) : String {
        val messageDigest : MessageDigest = MessageDigest.getInstance("SHA-256")
        messageDigest.update(password.toByteArray())

        val bytes : ByteArray = messageDigest.digest()
        return String(bytes)
    }

    fun encodeData(data: String): String {
        return Base64.encodeToString(data.toByteArray(), Base64.DEFAULT)
    }

    fun decodeData(encodedString : String): String {
        return String(Base64.decode(encodedString.toByteArray(), Base64.DEFAULT))
    }
}