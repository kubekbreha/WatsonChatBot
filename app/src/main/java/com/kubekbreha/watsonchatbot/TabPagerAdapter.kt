package com.ebookfrenzy.tablayoutdemo

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.kubekbreha.watsonchatbot.fragments.PeoplesFragment
import com.kubekbreha.watsonchatbot.fragments.ProfileFragment

class TabPagerAdapter(fm: FragmentManager, private var tabCount: Int) :
        FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment? {

        when (position) {
            0 -> return PeoplesFragment()
            1 -> return ProfileFragment()
            else -> return null
        }
    }

    override fun getCount(): Int {
        return tabCount
    }
}