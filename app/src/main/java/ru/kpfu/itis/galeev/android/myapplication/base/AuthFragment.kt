package ru.kpfu.itis.galeev.android.myapplication.base

import android.content.Context
import android.content.SharedPreferences
import androidx.annotation.LayoutRes
import ru.kpfu.itis.galeev.android.myapplication.R
import ru.kpfu.itis.galeev.android.myapplication.di.ServiceLocator
import ru.kpfu.itis.galeev.android.myapplication.utils.AuthorizationUtil

abstract class AuthFragment(@LayoutRes fragLayoutRes: Int) : BaseFragment(fragLayoutRes) {
    protected fun initSession(userId : Int, authUtil : AuthorizationUtil) {
        val authTime = System.currentTimeMillis()
        val encodedSessionData = authUtil.getSessionData(authTime, userId)

        val sp : SharedPreferences = requireActivity().getPreferences(Context.MODE_PRIVATE)
        with(sp.edit()) {
            putString(getString(R.string.session_arg_name), encodedSessionData)
            commit()
        }
    }

    protected fun getUserIdFromSession(authUtil : AuthorizationUtil) : Int? {
        val sp : SharedPreferences = requireActivity().getPreferences(Context.MODE_PRIVATE)
        with(sp) {
            val encodedSessionData = getString(this@AuthFragment.getString(R.string.session_arg_name), null)
            if (encodedSessionData != null) {
                return authUtil.getIdBySessionData(encodedSessionData).toInt()
            }
            return encodedSessionData
        }
    }
}