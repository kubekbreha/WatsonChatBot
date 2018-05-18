package com.kubekbreha.watsonchatbot.authentication.fragments


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth

import com.kubekbreha.watsonchatbot.R


class AuthenticationFragment : Fragment() {


    private var TAG : String = "AuthenticationFragment"

    //UI elements
    private var textLogIn: TextView? = null
    private var textSignUp: TextView? = null
    private var buttonGoogle: Button? = null

    private var mAuth: FirebaseAuth? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        val view = inflater.inflate(R.layout.fragment_authentification, container, false)
        initialise(view)
        return view
    }

    private fun initialise(view: View) {

        mAuth = FirebaseAuth.getInstance()

        textLogIn = view.findViewById<View>(R.id.frag_authentication_login) as TextView
        textSignUp = view.findViewById<View>(R.id.frag_authentication_sign_up_with_email) as TextView
        buttonGoogle = view.findViewById<View>(R.id.frag_authentication_btn_google_sign_in) as Button

        textLogIn!!.setOnClickListener{

            val newFragment = LoginFragment()
            val transaction = fragmentManager!!.beginTransaction()

            transaction.replace(R.id.act_authentication_authentication_frame, newFragment)
            transaction.addToBackStack(null)

            transaction.commit()
        }

        textSignUp!!.setOnClickListener{

            val newFragment = RegisterFragment()
            val transaction = fragmentManager!!.beginTransaction()

            transaction.replace(R.id.act_authentication_authentication_frame, newFragment)
            transaction.addToBackStack(null)

            transaction.commit()
        }

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
