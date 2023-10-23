package ru.kpfu.itis.galeev.android.myapplication.utils

object AllAnswersAreCompletedChecker {
    fun check(list : MutableList<Int>) : Boolean {
        var allCompleted : Boolean = true
        println(" DEBUG TAG 23.10 $list checker")
        for (index in 0 until list.size) {
            println(" DEBUG TAG 23.10 ${list[index]} checker")
            allCompleted = allCompleted && (list[index] != -1)
            println(" DEBUG TAG 23.10 $allCompleted checker")
            if (!allCompleted) break
        }
        return allCompleted
    }
}