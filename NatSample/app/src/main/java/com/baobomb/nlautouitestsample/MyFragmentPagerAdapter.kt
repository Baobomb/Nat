package com.baobomb.nlautouitestsample

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

class MyFragmentPagerAdapter(manager: FragmentManager) : FragmentPagerAdapter(manager) {

    private val fragmentList by lazy { arrayOf(FirstFragment(), SecondFragment(), ThirdFragment(), FourFragment()) }

    override fun getItem(potition: Int): Fragment {
        return fragmentList[potition]
    }

    override fun getCount(): Int = 4
}