package com.kubekbreha.watsonchatbot.authentication

import android.app.FragmentManager
import android.app.FragmentTransaction
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.WindowManager
import com.facebook.CallbackManager
import com.kubekbreha.watsonchatbot.R
import com.kubekbreha.watsonchatbot.authentication.fragments.SplashFragment


class AuthenticationActivity : AppCompatActivity() {

    private val TAG = "AuthenticationActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authentication)

        //show transparent activity tab
        val w = window
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)

        val splash = SplashFragment()
        addFragment(splash, R.id.act_authentication_authentication_frame)
    }


    inline fun FragmentManager.inTransaction(func: FragmentTransaction.() -> Unit) {
        val fragmentTransaction = beginTransaction()
        fragmentTransaction.func()
        fragmentTransaction.commit()
    }


    fun android.support.v4.app.FragmentManager.inTransaction(func: android.support.v4.app.FragmentTransaction.() -> android.support.v4.app.FragmentTransaction) {
        beginTransaction().func().commit()
    }


    fun AppCompatActivity.addFragment(fragment: Fragment, frameId: Int) {
        supportFragmentManager.inTransaction { add(frameId, fragment) }
    }


    fun AppCompatActivity.replaceFragment(fragment: Fragment, frameId: Int) {
        supportFragmentManager.inTransaction { replace(frameId, fragment) }
    }

}
