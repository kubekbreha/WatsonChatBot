package com.example.bottomnavigation.ui

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
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


class ProfileFragment : Fragment(){

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

        btnSettings!!.setOnClickListener{
            logOutUser()
        }
    }


    fun logOutUser(){
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