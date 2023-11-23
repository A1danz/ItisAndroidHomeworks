package ru.kpfu.itis.galeev.android.myapplication.utils

import android.content.Context
import android.util.Log
import kotlinx.coroutines.delay
import ru.kpfu.itis.galeev.android.myapplication.base.BaseActivity
import kotlin.random.Random

object CoroutinesConfig {
    suspend fun doWork(num : Int) {
        delay(Random.nextLong(2000))
        Log.e("TEST TAG", "Finished - $num")
    }
}