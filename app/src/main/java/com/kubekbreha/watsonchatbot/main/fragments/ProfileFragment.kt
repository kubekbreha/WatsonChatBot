package com.example.bottomnavigation.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v7.widget.PopupMenu
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.GoogleApiClient
import com.google.firebase.auth.FirebaseAuth
import com.kubekbreha.watsonchatbot.R
import com.kubekbreha.watsonchatbot.authentication.AuthenticationActivity
import com.kubekbreha.watsonchatbot.authentication.SettingsActivity
import com.kubekbreha.watsonchatbot.main.MainActivity
import kotlin.math.log


class ProfileFragment : Fragment() {

    private var mAuth: FirebaseAuth? = null
    private var mGoogleApiClient: GoogleApiClient? = null
    private var btnSettings: ImageButton? = null

    companion object {
        val TAG: String = ProfileFragment::class.java.simpleName
        fun newInstance() = ProfileFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        initialize(view)

        return view
    }

    private fun initialize(view: View) {
        btnSettings = view.findViewById<View>(R.id.frag_profile_settings_button) as ImageButton

        mAuth = FirebaseAuth.getInstance()

        val googleSignInOpt = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build()

        mGoogleApiClient = GoogleApiClient.Builder(context!!)
                .addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOpt)
                .build()

        mGoogleApiClient!!.connect()

        btnSettings!!.setOnClickListener {
        val popupMenu = PopupMenu(activity!!, it, Gravity.RIGHT)
            popupMenu.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.settings -> {
                        val intent = Intent(activity, SettingsActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        startActivity(intent)
                        true
                    }
                    R.id.help -> {
                        Toast.makeText(activity, "Help clicked.", Toast.LENGTH_SHORT).show()
                        true
                    }
                    R.id.logout -> {
                        logOutUser()
                        true
                    }
                    else -> false
                }
            }

            popupMenu.inflate(R.menu.menu_settengs_drop)

            try {
                val fieldMPopup = PopupMenu::class.java.getDeclaredField("mPopup")
                fieldMPopup.isAccessible = true
                val mPopup = fieldMPopup.get(popupMenu)
                mPopup.javaClass.getDeclaredMethod("setForceShowIcon", Boolean::class.java)
                        .invoke(mPopup, true)
            } catch (e: Exception) {
                Log.e("Main", "Error showing icon menu, ", e)
            } finally {
                popupMenu.show()
            }
            popupMenu.show()
        }

    }


    fun logOutUser() {
        mAuth!!.signOut()
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback { updateUI() }
    }


    private fun updateUI() {
        Toast.makeText(activity, "You have been logged out", Toast.LENGTH_SHORT).show()
        val accountIntent = Intent(activity, AuthenticationActivity::class.java)
        startActivity(accountIntent)
        activity!!.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        activity!!.finish()
    }

}