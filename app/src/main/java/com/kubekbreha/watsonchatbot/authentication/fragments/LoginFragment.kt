package com.kubekbreha.watsonchatbot.authentication.fragments


import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.google.firebase.auth.FirebaseAuth

import com.kubekbreha.watsonchatbot.R
import com.kubekbreha.watsonchatbot.main.MainActivity


class LoginFragment : Fragment(){

    private var TAG: String = "LoginFragment"

    //global variables
    private var email: String? = null
    private var password: String? = null

    //UI elements
    private var goToSignUp: RelativeLayout? = null
    private var btnTextGoToSignUp: Button? = null
    private var btntGoToSignUp: Button? = null
    private var textForgotPassword: TextView? = null
    private var editEmail: EditText? = null
    private var editPassword: EditText? = null
    private var btnLogin: Button? = null
    private var btnBack: ImageButton? = null
    private var mProgressBar: ProgressDialog? = null

    private var mAuth: FirebaseAuth? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        val view = inflater.inflate(R.layout.fragment_login, container, false)
        initialise(view)
        return view
    }


    private fun initialise(view: View) {
        goToSignUp = view.findViewById<View>(R.id.frag_login_goto_sign_up) as RelativeLayout
        btnTextGoToSignUp = view.findViewById<View>(R.id.frag_login_goto_signin_text) as Button
        btntGoToSignUp = view.findViewById<View>(R.id.frag_login_goto_signin_button) as Button

        textForgotPassword = view.findViewById<View>(R.id.frag_login_forgot) as TextView
        editEmail = view.findViewById<View>(R.id.frag_login_edit_email) as EditText
        editPassword = view.findViewById<View>(R.id.frag_login_edit_password) as EditText
        btnLogin = view.findViewById<View>(R.id.frag_login_btn_login) as Button
        btnBack = view.findViewById<View>(R.id.frag_login_btn_back_from_login) as ImageButton
        mProgressBar = ProgressDialog(activity!!)
        mAuth = FirebaseAuth.getInstance()

        textForgotPassword!!.setOnClickListener {
            val newFragment = ForgotFragment()
            val transaction = fragmentManager!!.beginTransaction()

            transaction.replace(R.id.act_authentication_authentication_frame, newFragment)
            transaction.addToBackStack(null)

            transaction.commit()
        }

        btnTextGoToSignUp!!.setOnClickListener{

            val newFragment = RegisterFragment()
            val transaction = fragmentManager!!.beginTransaction()

            transaction.replace(R.id.act_authentication_authentication_frame, newFragment)
            transaction.addToBackStack(null)

            transaction.commit()
        }

        btntGoToSignUp!!.setOnClickListener{

            val newFragment = RegisterFragment()
            val transaction = fragmentManager!!.beginTransaction()

            transaction.replace(R.id.act_authentication_authentication_frame, newFragment)
            transaction.addToBackStack(null)

            transaction.commit()
        }

        btnLogin!!.setOnClickListener { loginUser() }

        btnBack!!.setOnClickListener { activity!!.onBackPressed() }
    }


    private fun loginUser() {
        email = editEmail?.text.toString()
        password = editPassword?.text.toString()
        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
            mProgressBar!!.setMessage("Singing user...")
            mProgressBar!!.show()
            Log.d(TAG, "Logging in user.")
            mAuth!!.signInWithEmailAndPassword(email!!, password!!)
                    .addOnCompleteListener(activity!!) { task ->
                        mProgressBar!!.hide()
                        if (task.isSuccessful) {
                            // Sign in success, update UI with signed-in user's information
                            Log.d(TAG, "signInWithEmail:success")
                            updateUI()
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.e(TAG, "signInWithEmail:failure", task.exception)
                            Toast.makeText(activity, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show()
                        }
                    }
        } else {
            Toast.makeText(activity, "Enter all details", Toast.LENGTH_SHORT).show()
        }
    }


    private fun updateUI() {
        val intent = Intent(activity, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
        activity!!.finish()
    }

}
