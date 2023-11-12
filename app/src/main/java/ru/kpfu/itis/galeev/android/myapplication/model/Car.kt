package ru.kpfu.itis.galeev.android.myapplication.model

import androidx.annotation.DrawableRes
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import ru.kpfu.itis.galeev.android.myapplication.R

data class Car (
    @StringRes val name : Int,
    @DrawableRes val img : Int,
    val price : Int,
    var isFavorite : Boolean = false,
    @StringRes val description : Int = R.string.lorem_ipsum_150,
) {
}