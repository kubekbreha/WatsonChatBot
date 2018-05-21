package com.kubekbreha.watsonchatbot.authentication

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageButton
import com.kubekbreha.watsonchatbot.R

class SettingsActivity : AppCompatActivity() {

    private var btnGoBack: ImageButton? = null
    private var btnSave: Button? = null

    private val RC_SELECT_IMAGE = 2


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        btnGoBack = findViewById<View>(R.id.act_settings_btn_back_from_login) as ImageButton
        btnSave = findViewById<View>(R.id.act_settings_save) as Button

        //show transparent activity tab
        val w = window // in Activity's onCreate() for instance
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)

        btnGoBack!!.setOnClickListener{
            onBackPressed()
        }

    }
}
