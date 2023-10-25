package ru.kpfu.itis.galeev.android.myapplication.utils

import androidx.viewpager2.widget.ViewPager2

object EnableCircularPager {
    fun enableCircularPager(viewPager : ViewPager2) {
        with(viewPager) {
            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                    super.onPageScrolled(position, positionOffset, positionOffsetPixels)
                    // if the user has moved from first to last
                    if (position == 0 && positionOffset.toDouble() == 0.0 && positionOffsetPixels == 0) {
                        setCurrentItem(adapter!!.itemCount - 2, false)
                        RecyclerViewViewPagerAdapter.viewPagerPosition = adapter!!.itemCount - 2
                    // if the user has moved from last to first
                    } else if (position == adapter!!.itemCount -1 && positionOffset.toDouble() == 0.0 && positionOffsetPixels == 0) {
                        setCurrentItem(1, false)
                        RecyclerViewViewPagerAdapter.viewPagerPosition = 1
                    }
                }
            })
        }
    }
}