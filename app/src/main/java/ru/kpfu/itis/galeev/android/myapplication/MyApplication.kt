package ru.kpfu.itis.galeev.android.myapplication

import android.app.Application
import android.util.Log
import ru.kpfu.itis.galeev.android.myapplication.di.ServiceLocator

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        ServiceLocator.initDataDependencies()
        ServiceLocator.initDomainDependencies()
        Log.e("TEST TAG", "TEST TAG - app created")
    }
}