package ru.kpfu.itis.galeev.android.myapplication.utils

import androidx.viewpager2.widget.ViewPager2

object EnableCircularPager {
    fun enableCircularPager(viewPager : ViewPager2) {
        with(viewPager) {
            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
//                    val itemCount = adapter!!.itemCount
//                    if (position == itemCount - 1) {
//                        setCurrentItem(1, false)
//                    } else if (position == 0) {
//                        setCurrentItem(itemCount - 2, false)
//                    } else {
//                        super.onPageSelected(position)
//                    }

                    println("DEBUG 24.10 position - $position, state - ${scrollState}")
                }

                override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                    super.onPageScrolled(position, positionOffset, positionOffsetPixels)
                    println("DEBUG 24.10 position - $position, positionOffset - $positionOffset, positionOffsetPixels - $positionOffsetPixels")
                    if (position == 0 && positionOffset.toDouble() == 0.0 && positionOffsetPixels == 0) {
                        setCurrentItem(adapter!!.itemCount - 2, false)
                        RecyclerViewViewPagerAdapter.viewPagerPosition = adapter!!.itemCount - 2
                    } else if (position == adapter!!.itemCount -1 && positionOffset.toDouble() == 0.0 && positionOffsetPixels == 0) {
                        setCurrentItem(1, false)
                        RecyclerViewViewPagerAdapter.viewPagerPosition = 1
                    }
                }
            })
        }
    }
}