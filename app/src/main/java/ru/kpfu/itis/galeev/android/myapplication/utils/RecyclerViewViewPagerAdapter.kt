package ru.kpfu.itis.galeev.android.myapplication.utils


// This adapter is used to support data exchange between RecyclerView and ViewPager
object RecyclerViewViewPagerAdapter {
    var viewPagerPosition = 0
    var chosenAnswerInViewPagerFragments : MutableList<Int> = mutableListOf()

    fun clearVariables() {
        viewPagerPosition = 0
        chosenAnswerInViewPagerFragments = mutableListOf()
    }
}