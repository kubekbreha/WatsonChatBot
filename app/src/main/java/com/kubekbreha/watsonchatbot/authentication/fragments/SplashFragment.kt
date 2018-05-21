package com.kubekbreha.watsonchatbot.authentication.fragments

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth
import com.kubekbreha.watsonchatbot.R
import com.kubekbreha.watsonchatbot.main.MainActivity


class SplashFragment : Fragment() {

    private var mAuth: FirebaseAuth? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        mAuth = FirebaseAuth.getInstance()

        if (mAuth!!.currentUser != null) {
            val intent = Intent(activity, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
            activity!!.finish()
        }else{
            val newFragment = AuthenticationFragment()
            val transaction = fragmentManager!!.beginTransaction()
            transaction.replace(R.id.act_authentication_authentication_frame, newFragment)
            transaction.commit()
        }


        return inflater.inflate(R.layout.fragment_splash, container, false)
    }


}
