package ru.kpfu.itis.galeev.android.myapplication.base

import androidx.appcompat.app.AppCompatActivity

abstract class BaseActivity : AppCompatActivity() {
    protected abstract val fragmentContainerId : Int
}