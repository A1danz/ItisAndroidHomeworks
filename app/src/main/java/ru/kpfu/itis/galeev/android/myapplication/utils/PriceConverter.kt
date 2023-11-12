package ru.kpfu.itis.galeev.android.myapplication.utils

import java.lang.StringBuilder

object PriceConverter {
    @JvmStatic
    fun main(args: Array<String>) {
        convert(4000000)
    }
    fun convert(price : Int) : String {
        var strPrice = price.toString()

        val newPriceSb = StringBuilder()

        for (index in strPrice.length - 1 downTo 0 step 3) {
            if (index >= 2) {
                newPriceSb.append(strPrice.substring(index - 2, index + 1).reversed())
            } else {
                newPriceSb.append(strPrice.substring(0, index + 1).reversed())
            }
            newPriceSb.append(" ")
        }
        return newPriceSb.reverse().trim().toString() + " ₽"
    }
}