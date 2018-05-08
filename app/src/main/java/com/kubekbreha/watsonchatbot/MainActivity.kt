package com.kubekbreha.watsonchatbot

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment

import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.widget.FrameLayout
import com.kubekbreha.watsonchatbot.fragments.PeoplesFragment
import com.kubekbreha.watsonchatbot.fragments.ProfileFragment

class MainActivity : AppCompatActivity() {


    private var content: FrameLayout? = null

    private val mOnNavigationItemSelectedListener = object : BottomNavigationView.OnNavigationItemSelectedListener {

        override fun onNavigationItemSelected(item: MenuItem): Boolean {
            when (item.itemId) {
                R.id.navigation_people -> {
                    val fragment = PeoplesFragment.newInstance()
                    addFragment(fragment)
                    return true
                }

                R.id.navigation_profile -> {
                    val fragment = ProfileFragment.newInstance()
                    addFragment(fragment)
                    return true
                }

            }
            return false
        }

    }

    /**
     * add/replace fragment in container [framelayout]
     */
    private fun addFragment(fragment: Fragment) {
        supportFragmentManager
                .beginTransaction()
                .setCustomAnimations(R.anim.left_to_right, R.anim.right_to_left)
                .replace(R.id.main_frame, fragment, fragment.javaClass.getSimpleName())
                .addToBackStack(fragment.javaClass.getSimpleName())
                .commit()
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        content = findViewById(R.id.main_frame)
        val navigation = findViewById<BottomNavigationView>(R.id.main_navigation)
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)


        val fragment = PeoplesFragment.newInstance()
        addFragment(fragment)


    }
}
