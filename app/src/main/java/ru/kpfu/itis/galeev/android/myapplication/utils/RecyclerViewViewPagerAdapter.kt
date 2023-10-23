package ru.kpfu.itis.galeev.android.myapplication.utils

object RecyclerViewViewPagerAdapter {
    var viewPagerPosition = 0
    var chosenAnswerInViewPagerFragments : MutableList<Int> = mutableListOf()
    var fragmentsAnswers : MutableList<List<String>> = mutableListOf()
}