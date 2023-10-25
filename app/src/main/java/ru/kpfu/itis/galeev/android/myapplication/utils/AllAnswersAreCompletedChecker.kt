package ru.kpfu.itis.galeev.android.myapplication.utils

object AllAnswersAreCompletedChecker {
    fun check(list : MutableList<Int>) : Boolean {
        var allCompleted : Boolean = true
        for (index in 0 until list.size) {
            allCompleted = allCompleted && (list[index] != -1)
            if (!allCompleted) break
        }
        return allCompleted
    }
}