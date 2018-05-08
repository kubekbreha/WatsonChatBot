package com.kubekbreha.watsonchatbot

import android.os.Bundle
import android.support.design.widget.TabLayout

import android.support.v7.app.AppCompatActivity
import com.ebookfrenzy.tablayoutdemo.TabPagerAdapter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        configureTabLayout()

    }

    private fun configureTabLayout() {

        tab_layout.addTab(tab_layout.newTab().setIcon(R.drawable.ic_peoples_white))
        tab_layout.addTab(tab_layout.newTab().setIcon(R.drawable.ic_profile_white))


        val adapter = TabPagerAdapter(supportFragmentManager, tab_layout.tabCount)
        pager.adapter = adapter

        pager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tab_layout))

        tab_layout.addOnTabSelectedListener(object :
                TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                pager.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}

            override fun onTabReselected(tab: TabLayout.Tab) {}

        })

    }


}
